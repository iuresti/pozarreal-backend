import {Routes} from '@angular/router';
import {UserAccountComponent} from './components/user-account/user-account.component';
import {CircuitosComponent} from './components/circuitos/circuitos.component';
import {PaymentsComponent} from './components/payments/payments.component';
import {ChipsUpdaterComponent} from './components/chips-updater/chips-updater.component';
import {CoursesComponent} from './components/courses/courses.component';
import {UsersComponent} from './components/users/users.component';
import {HouseInfoComponent} from './components/house-info/house-info.component';
import {AuthGuard} from './guards/auth.guard';
import {StreetsGuard} from './guards/streets.guard';
import {PaymentsGuard} from './guards/payments.guard';
import {ChipsGuard} from './guards/chips.guard';
import {ClassesGuard} from './guards/classes.guard';
import {UsersGuard} from './guards/users.guard';

export const ROUTES: Routes = [
  {
    path: 'account',
    component: UserAccountComponent,
    canLoad: [AuthGuard]
  },
  {
    path: 'streets',
    component: CircuitosComponent,
    canActivate: [StreetsGuard]
  },
  {
    path: 'payments',
    component: PaymentsComponent,
    canActivate: [PaymentsGuard]
  },
  {
    path: 'chipsUpdates',
    component: ChipsUpdaterComponent,
    canActivate: [ChipsGuard]
  },
  {
    path: 'classes',
    component: CoursesComponent,
    canActivate: [ClassesGuard]
  },
  {
    path: 'users',
    component: UsersComponent,
    canActivate: [UsersGuard]
  },
  {
    path: 'house/:houseId',
    component: HouseInfoComponent
  },
  {
    path: 'streets/:streetId',
    component: CircuitosComponent
  },
  {
    path: '**', redirectTo: 'account',
    pathMatch: 'full'
  }
];

