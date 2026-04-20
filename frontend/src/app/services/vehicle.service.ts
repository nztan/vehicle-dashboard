import { inject, Injectable } from '@angular/core';
import {
  BehaviorSubject,
  distinctUntilChanged,
  map,
  Observable,
  of,
  retry,
  shareReplay,
  switchMap,
  tap,
  timer
} from 'rxjs';
import { DashboardSnapshot, GearType } from '../models/dashboard-snapshot.model';
import { HttpClient } from '@angular/common/http';
import { toSignal } from '@angular/core/rxjs-interop';
import { VehicleSetting } from '../models/vehicle-setting.model';

const BASE_URI = './api/vehicles';
const POLLING_INTERVAL = 1200;

@Injectable({
  providedIn: 'root'
})
export class VehicleService {
  private httpClient = inject(HttpClient);
  private _vehicleSetting$ = new BehaviorSubject<VehicleSetting>({motorSpeed: 0, charging: false});
  private _latestDashboardSnapshot?: DashboardSnapshot;

  private _dashboardSnapshot$ = this._vehicleSetting$.pipe(
    map(setting => setting.motorSpeed !== 0),
    distinctUntilChanged(),
    switchMap(isPolling => isPolling
      ? timer(0, POLLING_INTERVAL).pipe(
        switchMap(() => this.getDashboardSnapshot()),
        retry({delay: 500})
      )
      : this.getStoppedDashboardSnapshot()),
    tap(snapshot => {
      this._latestDashboardSnapshot = snapshot;
    }),
    shareReplay({bufferSize: 1, refCount: true})
  );

  readonly dashboardSnapshot = toSignal(this._dashboardSnapshot$, {initialValue: undefined});

  getVehicleSetting(): VehicleSetting {
    return this._vehicleSetting$.value;
  }

  updateVehicleSetting(vehicleSetting: VehicleSetting): void {
    this._vehicleSetting$.next(vehicleSetting);
  }

  private getStoppedDashboardSnapshot(): Observable<DashboardSnapshot> {
    return of({
      motorRPM: 0,
      powerKw: 0,
      gear: GearType.P,
      batteryLevel: this._latestDashboardSnapshot?.batteryLevel ?? 0,
      batteryTemperature: this._latestDashboardSnapshot?.batteryTemperature ?? 0,
      parkingBrakeWarning: this._latestDashboardSnapshot?.parkingBrakeWarning ?? false,
      checkEngineWarning: this._latestDashboardSnapshot?.checkEngineWarning ?? false,
      motorStatusWarning: this._latestDashboardSnapshot?.motorStatusWarning ?? false,
    });
  }

  private getDashboardSnapshot(): Observable<DashboardSnapshot> {
    return of({
      motorRPM: 500,
      powerKw: 500,
      gear: GearType.D,
      batteryLevel: 50,
      batteryTemperature: 30,
      parkingBrakeWarning: false,
      checkEngineWarning: true,
      motorStatusWarning: false,
    }).pipe(tap(a => console.log(a)));
    // return this.httpClient.get<DashboardSnapshot>(`${BASE_URI}/snapshot`)
    //   .pipe(tap(a => console.log(a)));
  }
}
