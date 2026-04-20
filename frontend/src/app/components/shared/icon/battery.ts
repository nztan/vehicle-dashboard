import { Component, Input } from '@angular/core';
import { LucideBattery } from '@lucide/angular';
import { Icon } from './icon';

@Component({
  selector: 'app-battery',
  imports: [Icon, LucideBattery],
  template: `
    <app-icon [value]="value.toString()" [unit]="'%'">
      <svg icon lucideBattery class="rotate-270 size-24"/>
    </app-icon>
  `
})
export class Battery {
  @Input() value: number = 0;
}
