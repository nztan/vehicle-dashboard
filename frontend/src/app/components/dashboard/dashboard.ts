import { Component, inject, OnInit } from '@angular/core';
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
  host: {
    class: 'block h-full min-h-0'
  }
})
export class Dashboard implements OnInit {
  private vehicleService = inject(VehicleService);

  ngOnInit(): void {
    this.vehicleService.dashboardSnapshot
  }
}
