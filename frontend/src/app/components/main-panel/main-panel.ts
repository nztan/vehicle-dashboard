import { Component, computed, DestroyRef, inject, OnInit } from '@angular/core';
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
import { GearType } from '../../models/dashboard-snapshot.model';
import { Gear } from '../shared/icon/gear';

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
  styleUrl: './main-panel.css',
})
export class MainPanel implements OnInit {
  private destroyRef = inject(DestroyRef);
  private vehicleService = inject(VehicleService);

  private snapshot = this.vehicleService.dashboardSnapshot;

  motorRpm = computed(() => this.snapshot()?.motorRPM ?? 0);
  powerKw = computed(() => this.snapshot()?.powerKw ?? 0);
  gear = computed(() => this.snapshot()?.gear ?? GearType.P);
  batteryLevel = computed(() => this.snapshot()?.batteryLevel ?? 0);
  batteryTemperature = computed(() => this.snapshot()?.batteryTemperature ?? 0);

  motorSpeedControl = new FormControl<number>(0, {nonNullable: true});

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
        const currentSetting = this.vehicleService.getVehicleSetting();
        this.vehicleService.updateVehicleSetting({
          ...currentSetting,
          motorSpeed
        });
      });
  }
}
