import { Injectable, computed, signal } from '@angular/core';
import { Operator } from '../models';

const STORAGE_KEY = 'mnp.activeOperator';

@Injectable({ providedIn: 'root' })
export class OperatorContextService {
  private readonly stored = this.readStored();

  readonly activeOperator = signal<Operator | null>(this.stored);
  readonly activeOperatorCode = computed(() => this.activeOperator()?.code ?? null);
  readonly activeOperatorName = computed(() => this.activeOperator()?.name ?? null);

  setActiveOperator(operator: Operator | null): void {
    this.activeOperator.set(operator);
    if (typeof localStorage === 'undefined') {
      return;
    }
    if (operator) {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(operator));
    } else {
      localStorage.removeItem(STORAGE_KEY);
    }
  }

  private readStored(): Operator | null {
    if (typeof localStorage === 'undefined') {
      return null;
    }
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return null;
    }
    try {
      return JSON.parse(raw) as Operator;
    } catch {
      return null;
    }
  }
}
