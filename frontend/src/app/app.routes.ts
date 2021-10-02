import {Routes} from '@angular/router';
import {UserAccountComponent} from './components/user-account/user-account.component';
import {CircuitosComponent} from './components/circuitos/circuitos.component';
import {PaymentsComponent} from './components/payments/payments.component';
import {ChipsUpdaterComponent} from './components/chips-updater/chips-updater.component';
import {CoursesComponent} from './components/courses/courses.component';
import {UsersComponent} from './components/users/users.component';

export const ROUTES: Routes = [
  {path: 'account', component: UserAccountComponent},
  {path: 'streets', component: CircuitosComponent},
  {path: 'payments', component: PaymentsComponent},
  {path: 'chipsUpdates', component: ChipsUpdaterComponent},
  {path: 'classes', component: CoursesComponent},
  {path: 'users', component: UsersComponent},
  {path: '**', redirectTo: 'account', pathMatch: 'full'}
];

