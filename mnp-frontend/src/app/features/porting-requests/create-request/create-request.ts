import { Component, EventEmitter, Output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MnpApiService } from '../../../core/services/mnp-api.service';
import { OperatorContextService } from '../../../core/services/operator-context.service';
import { extractErrorMessage } from '../../../core/utils/http-error.util';
import { PortingRequest } from '../../../core/models';

@Component({
  selector: 'app-create-request',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './create-request.html',
  styleUrl: './create-request.css',
})
export class CreateRequest {
  @Output() created = new EventEmitter<PortingRequest>();
  @Output() cancel = new EventEmitter<void>();

  readonly submitting = signal(false);
  readonly errorMessage = signal<string | null>(null);

  phoneNumber = '';

  constructor(
    private readonly api: MnpApiService,
    readonly operatorContext: OperatorContextService,
  ) {}

  submit(): void {
    this.errorMessage.set(null);

    if (!this.phoneNumber.trim()) {
      this.errorMessage.set('Enter a phone number to port.');
      return;
    }

    this.submitting.set(true);
    this.api.createPortingRequest(this.phoneNumber.trim()).subscribe({
      next: (request) => {
        this.submitting.set(false);
        this.phoneNumber = '';
        this.created.emit(request);
      },
      error: (err) => {
        this.submitting.set(false);
        this.errorMessage.set(extractErrorMessage(err, 'Could not submit the porting request.'));
      },
    });
  }
}
