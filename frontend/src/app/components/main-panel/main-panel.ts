import { Component } from '@angular/core';
import { Gauge } from '../shared/gauge/gauge';

@Component({
  selector: 'app-main-panel',
  imports: [
    Gauge
  ],
  templateUrl: './main-panel.html',
  styleUrl: './main-panel.css',
})
export class MainPanel {

}
