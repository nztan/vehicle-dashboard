import { Component } from '@angular/core';
import { Icon } from '../shared/icon/icon';
import { LucideCog, LucideMenu, LucidePlugZap, LucideSmartphoneCharging } from '@lucide/angular';
import { BatteryTemperature } from '../shared/icon/battery-temperature';

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
  styleUrl: './menu-bar.css',
})
export class MenuBar {
}
