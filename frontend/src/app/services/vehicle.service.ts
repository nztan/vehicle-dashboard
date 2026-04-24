import { computed, DestroyRef, inject, Injectable, Injector, signal } from '@angular/core';
import { distinctUntilChanged, Observable, retry, shareReplay, switchMap, tap, timer } from 'rxjs';
import { DashboardSnapshot } from '../models/dashboard-snapshot.model';
import { HttpClient } from '@angular/common/http';
import { VehicleSetting } from '../models/vehicle-setting.model';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { VehicleReading } from '../models/vehicle-reading.model';

const BASE_URI = '/api/vehicles';
const POLLING_INTERVAL = 1500;
const SESSION_VEHICLE_ID_KEY = 'vehicle_id';

@Injectable({
  providedIn: 'root'
})
export class VehicleService {
  private httpClient = inject(HttpClient);
  private destroyRef = inject(DestroyRef);
  private injector = inject(Injector);

  private readonly _vehicleId$ = signal<string | null>(
    sessionStorage.getItem(SESSION_VEHICLE_ID_KEY) ?? null
  )
  private readonly _dashboardSnapshot$ = signal<DashboardSnapshot | null>(null);
  // stop polling when vehicle is fully stopped and not charging
  private readonly shouldPoll = computed(() => {
    const setting = this.vehicleSetting();
    const reading = this.vehicleReading();
    return (setting?.motorSpeed ?? 0) > 0
      || (setting?.charging ?? false)
      || (reading?.motorRPM ?? 0) > 0;
  });

  readonly vehicleReading = computed<VehicleReading | undefined>(
    () => this._dashboardSnapshot$()?.vehicleReading
  );
  readonly vehicleSetting = computed<VehicleSetting | undefined>(
    () => this._dashboardSnapshot$()?.vehicleSetting
  );

  constructor() {
    if (this._vehicleId$()) {
      this.startPolling();
    } else {
      this.createVehicleSetting({charging: false, motorSpeed: 0} as VehicleSetting).subscribe(_ => this.startPolling());
    }
  }

  createVehicleSetting(setting: VehicleSetting): Observable<VehicleSetting> {
    return this.httpClient.post<VehicleSetting>(`${BASE_URI}/setting`, setting)
      .pipe(
        tap(setting => {
          sessionStorage.setItem(SESSION_VEHICLE_ID_KEY, setting.vehicleId!);
          this._vehicleId$.set(setting.vehicleId!);
          this._dashboardSnapshot$.set({
            vehicleSetting: setting,
            vehicleReading: this._dashboardSnapshot$()?.vehicleReading ?? {
              motorRPM: 0, powerKw: 0, gearRatio: 'N/N',
              batteryLevel: 100, batteryTemperature: 0,
              parkingBrake: true, checkEngine: false,
              batteryLow: false, motorStatusWarning: false
            }
          })
        })
      );
  }

  updateVehicleSetting(setting: VehicleSetting): void {
    const vehicleId = this._vehicleId$();
    if (!vehicleId) {
      return;
    }

    this.httpClient.put<VehicleSetting>(`${BASE_URI}/setting/${vehicleId}`, setting).subscribe(setting => {
      this._dashboardSnapshot$.update(prev => (
        prev ? {...prev, vehicleSetting: setting} : null
      ));
    });
  }

  private fetchDashboardSnapshot(): Observable<DashboardSnapshot> {
    return this.httpClient.get<DashboardSnapshot>(`${BASE_URI}/snapshot/${this._vehicleId$()}`);
  }

  private startPolling() {
    toObservable(this.shouldPoll, {injector: this.injector}).pipe(
        distinctUntilChanged(),
        switchMap(isPolling => isPolling
          ? timer(0, POLLING_INTERVAL).pipe(
            switchMap(() => this.fetchDashboardSnapshot()),
            retry({delay: 500}))
          : this.fetchDashboardSnapshot()),
        shareReplay({bufferSize: 1, refCount: true}),
        takeUntilDestroyed(this.destroyRef))
      .subscribe(snapshot => {
        this._dashboardSnapshot$.update(prev => ({
          vehicleSetting: prev?.vehicleSetting ?? snapshot.vehicleSetting,
          vehicleReading: snapshot.vehicleReading
        }));
      })
  }
}
