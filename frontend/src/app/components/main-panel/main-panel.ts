import { Component } from '@angular/core';
import { Gauge } from '../shared/gauge';
import { Battery } from '../shared/icon/battery';
import { BatteryTemperature } from '../shared/icon/battery-temperature';
import { Motor } from '../shared/icon/motor';
import { Gear } from '../shared/icon/gear';
import { SliderModule } from 'primeng/slider'
import { FormsModule } from '@angular/forms';
import { Slider } from '../shared/slider';

@Component({
  selector: 'app-main-panel',
  imports: [
    Gauge,
    Battery,
    BatteryTemperature,
    Battery,
    Motor,
    Gear,
    SliderModule,
    FormsModule,
    Slider
  ],
  templateUrl: './main-panel.html',
  styleUrl: './main-panel.css',
})
export class MainPanel {
  value: number = 20;
}
