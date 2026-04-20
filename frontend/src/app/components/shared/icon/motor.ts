import { Component, Input } from '@angular/core';
import { LucideSmartphoneCharging } from '@lucide/angular';
import { Icon } from './icon';

@Component({
  selector: 'app-motor',
  imports: [Icon, LucideSmartphoneCharging],
  template: `
    <app-icon [value]="value.toString()" [unit]="'RPM'">
      <svg icon lucideSmartphoneCharging class="size-24"/>
    </app-icon>
  `
})
export class Motor {
  @Input() value: number = 0;
}
