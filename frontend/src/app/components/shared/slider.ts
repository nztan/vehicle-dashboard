import { Component, DestroyRef, forwardRef, inject, Input, OnInit } from '@angular/core';
import { SliderModule } from 'primeng/slider';
import { ControlValueAccessor, FormControl, FormsModule, NG_VALUE_ACCESSOR, ReactiveFormsModule } from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-slider',
  imports: [SliderModule, FormsModule, ReactiveFormsModule],
  template: `
    <div class="flex flex-col items-center slider-dark">
      <p-slider [formControl]="formControl" step="1" min="0" max="4" class="w-full"/>
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
  },
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => Slider),
      multi: true
    }
  ]
})
export class Slider implements OnInit, ControlValueAccessor {
  @Input() min: number = 0;
  @Input() max: number = 4;
  @Input() step: number = 1;

  formControl: FormControl<number | null> = new FormControl<number>(0);

  private onChange: (value: number | null) => void = () => {
  };
  private onTouched: () => void = () => {
  };

  private destroyRef = inject(DestroyRef);

  writeValue(value: number): void {
    this.formControl.setValue(value);
  }

  registerOnChange(fn: (value: number | null) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    if (isDisabled) {
      this.formControl.disable();
    } else {
      this.formControl.enable();
    }
  }

  ngOnInit() {
    this.formControl.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        if (this.onChange) {
          this.onChange(this.formControl.value);
        }
      });
  }

  get steps() {
    return Array.from({length: this.max - this.min + 1}, (_, i) => i + this.min);
  }
}
