import { Component, computed, inject } from '@angular/core';
import { Icon } from '../shared/icon/icon';
import {
  LucideBatteryWarning,
  LucideCircleParking,
  LucideOctagonAlert,
  LucideSmartphoneCharging
} from '@lucide/angular';
import { VehicleService } from '../../services/vehicle.service';

@Component({
  selector: 'app-indicator-bar',
  imports: [
    Icon,
    LucideOctagonAlert,
    LucideSmartphoneCharging,
    LucideBatteryWarning,
    LucideCircleParking
  ],
  templateUrl: './indicator-bar.html',
  styleUrl: './indicator-bar.css',
})
export class IndicatorBar {
  private vehicleService = inject(VehicleService);

  private snapshot = this.vehicleService.dashboardSnapshot;

  batteryLevel = computed(() => this.snapshot()?.batteryLevel ?? 0);
  parkingBrakeWarning = computed(() => this.snapshot()?.parkingBrakeWarning ?? false);
  checkEngineWarning = computed(() => this.snapshot()?.checkEngineWarning ?? false);
  motorStatusWarning = computed(() => this.snapshot()?.motorStatusWarning ?? false);

  get isBatteryLow() {
    return this.batteryLevel() <= 20;
  }
}
