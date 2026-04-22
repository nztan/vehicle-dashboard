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
  host: {
    class: 'block h-full min-h-0'
  }
})
export class IndicatorBar {
  private vehicleService = inject(VehicleService);

  private snapshot = this.vehicleService.dashboardSnapshot;

  batteryLowWarning = computed(() => this.snapshot()?.batteryLow);
  parkingBrakeWarning = computed(() => this.snapshot()?.parkingBrake);
  checkEngineWarning = computed(() => this.snapshot()?.checkEngine);
  motorStatusWarning = computed(() => this.snapshot()?.motorStatusWarning);
}
