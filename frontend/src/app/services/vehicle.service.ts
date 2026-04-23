import { computed, inject, Injectable } from '@angular/core';
import { BehaviorSubject, distinctUntilChanged, Observable, retry, shareReplay, switchMap, tap, timer } from 'rxjs';
import { DashboardSnapshot } from '../models/dashboard-snapshot.model';
import { HttpClient } from '@angular/common/http';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { VehicleSetting } from '../models/vehicle-setting.model';

const BASE_URI = '/api/vehicles';
const POLLING_INTERVAL = 1200;

@Injectable({
  providedIn: 'root'
})
export class VehicleService {
  private httpClient = inject(HttpClient);
  private _vehicleSetting$ = new BehaviorSubject<VehicleSetting>({motorSpeed: 0, charging: false});
  private latestDashboardSnapshot?: DashboardSnapshot;

  readonly vehicleSetting = toSignal(this._vehicleSetting$, {initialValue: {motorSpeed: 0, charging: false}});
  readonly shouldPoll = computed(() => {
    const setting = this.vehicleSetting();
    return (setting.motorSpeed !== 0 || setting.charging || (this.latestDashboardSnapshot?.motorRPM ?? 0) > 0)
  });
  readonly dashboardSnapshot$ = toObservable(this.shouldPoll).pipe(
    distinctUntilChanged(),
    switchMap(isPolling => isPolling
      ? timer(0, POLLING_INTERVAL).pipe(
        switchMap(() => this.getDashboardSnapshot()),
        retry({delay: 500}))
      : this.getDashboardSnapshot()),
    tap(snapshot => this.latestDashboardSnapshot = snapshot),
    shareReplay({bufferSize: 1, refCount: true})
  );

  readonly dashboardSnapshot = toSignal(this.dashboardSnapshot$, {initialValue: undefined});

  updateVehicleSetting(vehicleSetting: VehicleSetting): void {
    this.httpClient.post<VehicleSetting>(`${BASE_URI}/setting`, vehicleSetting)
      .subscribe(setting => this._vehicleSetting$.next(setting));
  }

  private getDashboardSnapshot(): Observable<DashboardSnapshot> {
    return this.httpClient.get<DashboardSnapshot>(`${BASE_URI}/snapshot`);
  }
}
