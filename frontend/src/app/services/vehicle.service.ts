import { computed, DestroyRef, inject, Injectable, signal } from '@angular/core';
import { Observable, retry, shareReplay, switchMap, tap, timer } from 'rxjs';
import { DashboardSnapshot } from '../models/dashboard-snapshot.model';
import { HttpClient } from '@angular/common/http';
import { VehicleSetting } from '../models/vehicle-setting.model';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { VehicleReading } from '../models/vehicle-reading.model';

const BASE_URI = '/api/vehicles';
const POLLING_INTERVAL = 1200;

@Injectable({
  providedIn: 'root'
})
export class VehicleService {
  private httpClient = inject(HttpClient);
  private destroyRef = inject(DestroyRef);
  private readonly _dashboardSnapshot$ = signal<DashboardSnapshot | null>(null);

  readonly vehicleReading = computed<VehicleReading | undefined>(
    () => this._dashboardSnapshot$()?.vehicleReading
  );
  readonly vehicleSetting = computed<VehicleSetting | undefined>(
    () => this._dashboardSnapshot$()?.vehicleSetting
  );

  constructor() {
    this.startPolling();
  }

  startPolling() {
    timer(0, POLLING_INTERVAL).pipe(
      switchMap(() => this.fetchDashboardSnapshot()),
      retry({delay: POLLING_INTERVAL}),
      shareReplay({bufferSize: 1, refCount: true}),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(snapshot => {
      this._dashboardSnapshot$.update(prev => ({
        // only load vehicle setting on init, otherwise always take user's input
        vehicleSetting: prev?.vehicleSetting ?? snapshot.vehicleSetting,
        vehicleReading: snapshot.vehicleReading
      }));
    });
  }

  updateVehicleSetting(setting: VehicleSetting) {
    this._dashboardSnapshot$.update(prev => prev
      ? {...prev, vehicleSetting: setting}
      : null
    );
    this.httpClient.post<VehicleSetting>(`${BASE_URI}/setting`, setting).subscribe(setting => {
      this._dashboardSnapshot$.update(prev => (
        prev ? {...prev, vehicleSetting: setting} : null
      ));
    });
  }

  private fetchDashboardSnapshot(): Observable<DashboardSnapshot> {
    return this.httpClient.get<DashboardSnapshot>(`${BASE_URI}/snapshot`);
  }
}
