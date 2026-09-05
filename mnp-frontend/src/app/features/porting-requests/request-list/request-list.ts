import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MnpApiService } from '../../../core/services/mnp-api.service';
import { OperatorContextService } from '../../../core/services/operator-context.service';
import { extractErrorMessage } from '../../../core/utils/http-error.util';
import { CreateRequest } from '../create-request/create-request';
import { StatusBadge } from '../../../shared/components/status-badge/status-badge';
import { LocalDateTimePipe } from '../../../shared/pipes/local-date-time.pipe';
import { PortingRequest } from '../../../core/models';

@Component({
  selector: 'app-request-list',
  standalone: true,
  imports: [FormsModule, CreateRequest, StatusBadge, LocalDateTimePipe],
  templateUrl: './request-list.html',
  styleUrl: './request-list.css',
})
export class RequestList implements OnInit {
  readonly requests = signal<PortingRequest[]>([]);
  readonly loading = signal(false);
  readonly errorMessage = signal<string | null>(null);
  readonly processingId = signal<number | null>(null);
  readonly showCreateForm = signal(false);

  readonly rejectingId = signal<number | null>(null);
  rejectionReason = '';

  constructor(
    private readonly api: MnpApiService,
    readonly operatorContext: OperatorContextService,
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.errorMessage.set(null);
    this.api.getPortingRequests().subscribe({
      next: (requests) => {
        this.loading.set(false);
        this.requests.set(requests);
      },
      error: (err) => {
        this.loading.set(false);
        this.errorMessage.set(extractErrorMessage(err, 'Could not load porting requests.'));
      },
    });
  }

  onCreated(request: PortingRequest): void {
    this.showCreateForm.set(false);
    this.requests.set([request, ...this.requests()]);
  }

  isDonor(request: PortingRequest): boolean {
    const name = this.operatorContext.activeOperatorName();
    return !!name && request.donor === name;
  }

  accept(request: PortingRequest): void {
    this.runDecision(request, 'ACCEPTED');
  }

  startReject(request: PortingRequest): void {
    this.errorMessage.set(null);
    this.rejectionReason = '';
    this.rejectingId.set(request.id);
  }

  cancelReject(): void {
    this.rejectingId.set(null);
    this.rejectionReason = '';
  }

  confirmReject(request: PortingRequest): void {
    if (!this.rejectionReason.trim()) {
      this.errorMessage.set('Enter a reason for rejecting this request.');
      return;
    }
    this.runDecision(request, 'REJECTED', this.rejectionReason.trim());
  }

  private runDecision(request: PortingRequest, action: 'ACCEPTED' | 'REJECTED', rejectionReason?: string): void {
    this.processingId.set(request.id);
    this.errorMessage.set(null);
    this.api.decidePortingRequest(request.id, action, rejectionReason).subscribe({
      next: (updated) => {
        this.processingId.set(null);
        this.rejectingId.set(null);
        this.rejectionReason = '';
        this.requests.set(this.requests().map((r) => (r.id === updated.id ? updated : r)));
      },
      error: (err) => {
        this.processingId.set(null);
        this.errorMessage.set(extractErrorMessage(err, 'Could not process the decision.'));
      },
    });
  }
}
