import { Component, Input } from '@angular/core';
import { LucideCog } from '@lucide/angular';
import { Icon } from './icon';

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
  @Input() value: string = "N/N";
}
