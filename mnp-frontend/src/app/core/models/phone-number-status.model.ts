export interface PhoneNumberStatus {
  phoneNumber: string;
  portingStatus: string | null;
  isPorted: boolean;
  currentHolder: string;
}
