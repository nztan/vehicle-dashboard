export interface DashboardSnapshot {
  motorRPM: number;
  powerKw: number;
  gearRatio: string;
  batteryLevel: number;
  batteryTemperature: number;
  parkingBrake: boolean;
  checkEngine: boolean;
  batteryLow: boolean;
  motorStatusWarning: boolean;
}
