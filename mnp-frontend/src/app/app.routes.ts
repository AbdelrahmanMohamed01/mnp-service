import { Routes } from '@angular/router';
import { OperatorSelect } from './features/operator-select/operator-select';
import { MainLayout } from './layout/main-layout/main-layout';
import { RequestList } from './features/porting-requests/request-list/request-list';
import { PhoneStatus } from './features/phone-status/phone-status';
import { operatorSelectedGuard } from './core/guards/operator-selected.guard';

export const routes: Routes = [
  { path: '', component: OperatorSelect },
  {
    path: '',
    component: MainLayout,
    canActivate: [operatorSelectedGuard],
    children: [
      { path: 'requests', component: RequestList },
      { path: 'phone-status', component: PhoneStatus },
    ],
  },
  { path: '**', redirectTo: '' },
];
