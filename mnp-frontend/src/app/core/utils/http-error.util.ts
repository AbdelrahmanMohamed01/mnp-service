import { HttpErrorResponse } from '@angular/common/http';
import { ApiErrorResponse } from '../models';

export function extractErrorMessage(err: unknown, fallback: string): string {
  if (err instanceof HttpErrorResponse) {
    const body = err.error as Partial<ApiErrorResponse> | string | null;

    if (body && typeof body === 'object' && typeof body.message === 'string' && body.message.trim()) {
      return body.message;
    }
    if (typeof body === 'string' && body.trim()) {
      return body;
    }
    if (err.status === 0) {
      return 'Could not reach the server. Is the backend running?';
    }
  }
  return fallback;
}
