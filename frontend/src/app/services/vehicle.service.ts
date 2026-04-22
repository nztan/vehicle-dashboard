import { inject, Injectable } from '@angular/core';
import {
  BehaviorSubject, distinctUntilChanged,
  map,
  Observable,
  retry,
  shareReplay,
  switchMap,
  tap,
  timer
} from 'rxjs';
import { DashboardSnapshot } from '../models/dashboard-snapshot.model';
import { HttpClient } from '@angular/common/http';
import { toSignal } from '@angular/core/rxjs-interop';
import { VehicleSetting } from '../models/vehicle-setting.model';

const BASE_URI = '/api/vehicles';
const POLLING_INTERVAL = 1200;

@Injectable({
  providedIn: 'root'
})
export class VehicleService {
  private httpClient = inject(HttpClient);
  private _vehicleSetting$ = new BehaviorSubject<VehicleSetting>({motorSpeed: 0, charging: false});
  private _latestDashboardSnapshot?: DashboardSnapshot;
  private _dashboardSnapshot$ = this._vehicleSetting$.pipe(
    map(setting => this.isPolling(setting)),
    distinctUntilChanged(),
    switchMap(isPolling => isPolling
      ? timer(0, POLLING_INTERVAL).pipe(
        switchMap(() => this.getDashboardSnapshot()),
        retry({delay: 500})
      )
      : this.getDashboardSnapshot()),
    tap(snapshot => {
      this._latestDashboardSnapshot = snapshot;
    }),
    shareReplay({bufferSize: 1, refCount: true})
  );

  getVehicleSetting(): VehicleSetting {
    return this._vehicleSetting$.value;
  }

  updateVehicleSetting(vehicleSetting: VehicleSetting): void {
    this.httpClient.post<VehicleSetting>(`${BASE_URI}/setting`, vehicleSetting)
      .subscribe(setting => this._vehicleSetting$.next(setting));
  }

  readonly dashboardSnapshot = toSignal(this._dashboardSnapshot$, {initialValue: undefined});
  readonly vehicleSetting = toSignal(this._vehicleSetting$, {initialValue: {motorSpeed: 0, charging: false}});

  private getDashboardSnapshot(): Observable<DashboardSnapshot> {
    return this.httpClient.get<DashboardSnapshot>(`${BASE_URI}/snapshot`);
  }

  private isPolling(vehicleSetting: VehicleSetting): boolean {
    return vehicleSetting.motorSpeed !== 0 || (this._latestDashboardSnapshot?.motorRPM ?? 0) > 0 || vehicleSetting.charging;
  }
}
