import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MnpApiService } from '../../core/services/mnp-api.service';
import { OperatorContextService } from '../../core/services/operator-context.service';
import { extractErrorMessage } from '../../core/utils/http-error.util';
import { PhoneNumberStatus } from '../../core/models';

@Component({
  selector: 'app-phone-status',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './phone-status.html',
  styleUrl: './phone-status.css',
})
export class PhoneStatus {
  phoneNumber = '';
  readonly result = signal<PhoneNumberStatus | null>(null);
  readonly loading = signal(false);
  readonly errorMessage = signal<string | null>(null);

  constructor(
    private readonly api: MnpApiService,
    readonly operatorContext: OperatorContextService,
  ) {}

  lookup(): void {
    this.errorMessage.set(null);
    this.result.set(null);

    if (!this.phoneNumber.trim()) {
      this.errorMessage.set('Enter a phone number to look up.');
      return;
    }

    this.loading.set(true);
    this.api.getPhoneNumberStatus(this.phoneNumber.trim()).subscribe({
      next: (status) => {
        this.loading.set(false);
        this.result.set(status);
      },
      error: (err) => {
        this.loading.set(false);
        this.errorMessage.set(extractErrorMessage(err, 'Could not look up this phone number.'));
      },
    });
  }
}
