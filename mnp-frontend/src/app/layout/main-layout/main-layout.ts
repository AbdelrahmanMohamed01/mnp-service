import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { Router } from '@angular/router';
import { OperatorContextService } from '../../core/services/operator-context.service';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css',
})
export class MainLayout {
  constructor(
    private readonly router: Router,
    readonly operatorContext: OperatorContextService,
  ) {}

  switchOperator(): void {
    this.router.navigate(['/']);
  }
}
