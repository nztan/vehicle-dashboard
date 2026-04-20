import { Component } from '@angular/core';
import { Icon } from '../shared/icon/icon';
import {
  LucideBatteryWarning,
  LucideCircleParking,
  LucideOctagonAlert,
  LucideSmartphoneCharging
} from '@lucide/angular';

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

  get isParkingBrakeOn() {
    return true;
  }

  get isCheckEngineOn() {
    return true;
  }

  get isMotorStatusOn() {
    return true;
  }

  get isBatteryLowOn() {
    return true;
  }
}
