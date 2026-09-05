import { Component, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { MnpApiService } from '../../core/services/mnp-api.service';
import { OperatorContextService } from '../../core/services/operator-context.service';
import { extractErrorMessage } from '../../core/utils/http-error.util';
import { Operator } from '../../core/models';

@Component({
  selector: 'app-operator-select',
  standalone: true,
  imports: [],
  templateUrl: './operator-select.html',
  styleUrl: './operator-select.css',
})
export class OperatorSelect implements OnInit {
  readonly operators = signal<Operator[]>([]);
  readonly loading = signal(false);
  readonly errorMessage = signal<string | null>(null);

  constructor(
    private readonly api: MnpApiService,
    private readonly router: Router,
    readonly operatorContext: OperatorContextService,
  ) {}

  ngOnInit(): void {
    this.loading.set(true);
    this.api.getOperators().subscribe({
      next: (operators) => {
        this.loading.set(false);
        this.operators.set(operators);
      },
      error: (err) => {
        this.loading.set(false);
        this.errorMessage.set(extractErrorMessage(err, 'Could not load operators.'));
      },
    });
  }

  choose(operator: Operator): void {
    this.operatorContext.setActiveOperator(operator);
    this.router.navigate(['/requests']);
  }
}
