import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { OperatorContextService } from '../services/operator-context.service';

export const organizationHeaderInterceptor: HttpInterceptorFn = (req, next) => {
  const operatorContext = inject(OperatorContextService);
  const code = operatorContext.activeOperatorCode();

  if (!code) {
    return next(req);
  }

  return next(req.clone({ setHeaders: { organization: code } }));
};
