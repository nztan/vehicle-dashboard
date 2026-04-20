import { Component, inject } from '@angular/core';
import { IndicatorBar } from '../indicator-bar/indicator-bar';
import { MainPanel } from '../main-panel/main-panel';
import { MenuBar } from '../menu-bar/menu-bar';
import { VehicleService } from '../../services/vehicle.service';

@Component({
  selector: 'app-dashboard',
  imports: [
    IndicatorBar,
    MainPanel,
    MenuBar
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  private vehicleService = inject(VehicleService);
}
