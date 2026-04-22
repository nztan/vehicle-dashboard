import { Component, computed, inject } from '@angular/core';
import { Icon } from '../shared/icon/icon';
import { LucideCog, LucideMenu, LucidePlugZap, LucideSmartphoneCharging } from '@lucide/angular';
import { BatteryTemperature } from '../shared/icon/battery-temperature';
import { VehicleService } from '../../services/vehicle.service';
import { getChargingColor, getDefaultIconColor } from '../../utils/get-theme.util';

@Component({
  selector: 'app-menu-bar',
  imports: [
    Icon,
    LucideCog,
    LucideMenu,
    LucideSmartphoneCharging,
    LucidePlugZap,
    BatteryTemperature
  ],
  templateUrl: './menu-bar.html',
  host: {
    class: 'block h-full min-h-0'
  }
})
export class MenuBar {
  private vehicleService = inject(VehicleService);
  setting = this.vehicleService.vehicleSetting;
  charging = computed(() => this.setting()?.charging ?? false);
  motorSpeed = computed(() => this.setting()?.motorSpeed ?? 0);

  toggleCharging() {
    this.vehicleService.updateVehicleSetting({
      charging: !this.charging(),
      motorSpeed: this.motorSpeed()
    });
    console.log(this.setting);
  }

  get iconColor(): string {
    return this.charging() ? getChargingColor() : getDefaultIconColor();
  }

  get canCharge(): boolean {
    return this.motorSpeed() !== 0;
  }
}
