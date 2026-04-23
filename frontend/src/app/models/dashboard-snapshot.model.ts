import { VehicleSetting } from './vehicle-setting.model';
import { VehicleReading } from './vehicle-reading.model';

export interface DashboardSnapshot {
  vehicleSetting: VehicleSetting;
  vehicleReading: VehicleReading;
}
