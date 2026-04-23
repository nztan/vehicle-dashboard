import { Component } from '@angular/core';
import { IndicatorBar } from '../indicator-bar/indicator-bar';
import { MainPanel } from '../main-panel/main-panel';
import { MenuBar } from '../menu-bar/menu-bar';

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
export class Dashboard {
}
