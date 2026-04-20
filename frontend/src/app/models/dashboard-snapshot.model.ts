export interface DashboardSnapshot {
  motorRPM: number;
  powerKw: number;
  gear: GearType;
  batteryLevel: number;
  batteryTemperature: number;
  parkingBrakeWarning: boolean;
  checkEngineWarning: boolean;
  motorStatusWarning: boolean;
}

export enum GearType {
  D = 'D',
  P = 'P',
  N = 'N',
  R = 'R',
}
