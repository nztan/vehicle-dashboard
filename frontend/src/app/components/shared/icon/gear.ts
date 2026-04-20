import { Component, Input } from '@angular/core';
import { LucideCog } from '@lucide/angular';
import { Icon } from './icon';
import { GearType } from '../../../models/dashboard-snapshot.model';

@Component({
  selector: 'app-gear',
  imports: [Icon, LucideCog],
  template: `
    <app-icon [value]="value">
      <svg icon lucideCog class="size-24"/>
    </app-icon>
  `
})
export class Gear {
  @Input() value: GearType = GearType.P;
}
