import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Operator, PhoneNumberStatus, PortingRequest } from '../models';

const API_BASE = '/api';

@Injectable({ providedIn: 'root' })
export class MnpApiService {
  constructor(private readonly http: HttpClient) {}

  getOperators(): Observable<Operator[]> {
    return this.http.get<Operator[]>(`${API_BASE}/operators`);
  }

  getPortingRequests(): Observable<PortingRequest[]> {
    return this.http.get<PortingRequest[]>(`${API_BASE}/porting-requests`);
  }

  createPortingRequest(phoneNumber: string): Observable<PortingRequest> {
    return this.http.post<PortingRequest>(`${API_BASE}/porting-requests`, { phoneNumber });
  }

  decidePortingRequest(
    id: number,
    action: 'ACCEPTED' | 'REJECTED',
    rejectionReason?: string,
  ): Observable<PortingRequest> {
    return this.http.patch<PortingRequest>(`${API_BASE}/porting-requests/${id}/decision`, {
      action,
      rejectionReason: rejectionReason ?? null,
    });
  }

  getPhoneNumberStatus(phoneNumber: string): Observable<PhoneNumberStatus> {
    return this.http.get<PhoneNumberStatus>(`${API_BASE}/phone-numbers/${phoneNumber}/status`);
  }
}
