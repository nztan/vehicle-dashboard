import { Component, Input } from '@angular/core';
import { LucideBattery, LucideThermometer } from '@lucide/angular';
import { Icon } from './icon';

@Component({
  selector: 'app-battery-temperature',
  imports: [Icon, LucideBattery, LucideThermometer],
  template: `
    <app-icon [value]="value?.toString()" [unit]="value !== undefined ? '℃' : ''">
      <div icon class="flex -space-x-12">
        <svg lucideBattery class="rotate-270 size-24"/>
        <svg lucideThermometer class="size-24"/>
      </div>
    </app-icon>
  `
})
export class BatteryTemperature {
  @Input() value?: number | undefined = 0;
}
