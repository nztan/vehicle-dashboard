import { Component, computed, DestroyRef, effect, inject, OnInit } from '@angular/core';
import { Gauge } from '../shared/gauge';
import { Battery } from '../shared/icon/battery';
import { BatteryTemperature } from '../shared/icon/battery-temperature';
import { Motor } from '../shared/icon/motor';
import { SliderModule } from 'primeng/slider'
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Slider } from '../shared/slider';
import { VehicleService } from '../../services/vehicle.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { distinctUntilChanged } from 'rxjs';
import { Gear } from '../shared/icon/gear';
import { VehicleSetting } from '../../models/vehicle-setting.model';

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
    Slider,
    ReactiveFormsModule
  ],
  templateUrl: './main-panel.html',
  host: {
    class: 'block h-full min-h-0'
  }
})
export class MainPanel implements OnInit {
  private destroyRef = inject(DestroyRef);
  private vehicleService = inject(VehicleService);

  private reading = this.vehicleService.vehicleReading;
  private setting = this.vehicleService.vehicleSetting;
  motorRpm = computed(() => this.reading()?.motorRPM ?? 0);
  powerKw = computed(() => this.reading()?.powerKw ?? 0);
  gearRatio = computed(() => this.reading()?.gearRatio ?? 'N/N');
  batteryLevel = computed(() => this.reading()?.batteryLevel ?? 0);
  batteryTemperature = computed(() => this.reading()?.batteryTemperature ?? 0);
  isMotorSpeedControlDisabled = computed(() => {
    const setting = this.setting();
    const reading = this.reading();
    return setting?.charging || reading?.batteryLevel === 0;
  });

  motorSpeedControl = new FormControl<number>(0, {nonNullable: true});

  constructor() {
    effect(() => {
      if (this.isMotorSpeedControlDisabled()) {
        this.motorSpeedControl.setValue(0);
        this.motorSpeedControl.disable({emitEvent: false});
      } else {
        this.motorSpeedControl.enable({emitEvent: false});
      }
    });

    effect(() => {
      const speed = this.setting()?.motorSpeed ?? 0;
      if (this.motorSpeedControl.value !== speed) {
        this.motorSpeedControl.setValue(speed, {emitEvent: false});
      }
    });
  }

  ngOnInit(): void {
    this.subscribeToFormChanges();
  }

  private subscribeToFormChanges(): void {
    this.motorSpeedControl.valueChanges
      .pipe(
        distinctUntilChanged(),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe((motorSpeed) => {
        const currentSetting = this.vehicleService.vehicleSetting();
        this.vehicleService.updateVehicleSetting({
          motorSpeed: motorSpeed,
          charging: currentSetting?.charging ?? false
        } as VehicleSetting);
      });
  }
}
