import { Component, Input } from '@angular/core';
import { PortingStatus } from '../../../core/models';

@Component({
  selector: 'app-status-badge',
  standalone: true,
  templateUrl: './status-badge.html',
  styleUrl: './status-badge.css',
})
export class StatusBadge {
  @Input({ required: true }) status!: PortingStatus;

  get cssClass(): string {
    return this.status.toLowerCase();
  }
}
