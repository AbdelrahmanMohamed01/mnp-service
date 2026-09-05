export type PortingStatus = 'PENDING' | 'ACCEPTED' | 'REJECTED' | 'CANCELLED';

export interface PortingRequest {
  id: number;
  phoneNumber: string;
  status: PortingStatus;
  recipient: string;
  donor: string;
  createdAt: string;
  updatedAt: string;
  rejectionReason: string | null;
}
