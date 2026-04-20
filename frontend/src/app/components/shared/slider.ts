import { Component, Input } from '@angular/core';
import { SliderModule } from 'primeng/slider';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-slider',
  imports: [SliderModule, FormsModule],
  template: `
    <div class="flex flex-col items-center slider-dark">
      <p-slider [(ngModel)]="value" step="1" min="0" max="4" class="w-full"/>
      <div class="mt-2 flex justify-between text-xs text-gray-400 w-full">
        @for (step of steps; track step) {
          <span>{{ step === 0 ? 'OFF' : step }}</span>
        }
      </div>
    </div>
  `,
  styles: [
    `
      .slider-dark {
        --p-slider-range-background: #696969;
        --p-slider-handle-background: #696969;
        --p-slider-handle-focus-ring-color: #A9A9A9;
      }
    `
  ],
  host: {
    class: 'block w-full'
  }
})
export class Slider {
  @Input() min: number = 0;
  @Input() max: number = 4;
  @Input() step: number = 1;
  @Input() value: number = 0;

  get steps() {
    return Array.from({length: this.max - this.min + 1}, (_, i) => i + this.min);
  }
}
