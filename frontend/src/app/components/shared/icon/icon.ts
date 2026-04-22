import { Component, Input } from '@angular/core';
import { getDefaultIconColor } from '../../../utils/get-theme.util';

@Component({
  selector: 'app-icon',
  template: `
    <div class="flex flex-col items-center" [style.color]="color ? color : getIconColor()">
      <ng-content select="[icon]"/>
      @if (value !== null && value !== undefined) {
        <span>{{ value }}</span>
      }
      @if (unit) {
        <span class="opacity-50">{{ unit }}</span>
      }
    </div>
  `
})
export class Icon {
  @Input() value?: string | undefined;
  @Input() unit?: string;
  @Input() color?: string;

  protected readonly getIconColor = getDefaultIconColor;
}
