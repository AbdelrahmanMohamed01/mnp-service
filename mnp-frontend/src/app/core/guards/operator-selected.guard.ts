import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { OperatorContextService } from '../services/operator-context.service';

export const operatorSelectedGuard: CanActivateFn = () => {
  const operatorContext = inject(OperatorContextService);
  const router = inject(Router);

  if (operatorContext.activeOperatorCode()) {
    return true;
  }

  return router.createUrlTree(['/']);
};
