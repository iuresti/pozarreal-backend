import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {NgbDateParserFormatter, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ChipsByHouseComponent} from './components/chips-by-house/chips-by-house.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {NgDragDropModule} from 'ng-drag-drop';
import {ConfirmationInputTextComponent} from './components/confirmation-input-text/confirmation-input-text.component';
import {CircuitosComponent} from './components/circuitos/circuitos.component';
import {UsersComponent} from './components/users/users.component';
import {RolesByUserComponent} from './components/roles-by-user/roles-by-user.component';
import {UserAccountComponent} from './components/user-account/user-account.component';
import {RolePipe} from './pipes/role.pipe';
import {ChipsUpdaterComponent} from './components/chips-updater/chips-updater.component';
import {PaymentsComponent} from './components/payments/payments.component';
import {PaymentFilterComponent} from './components/payment-filter/payment-filter.component';
import {PaymentModePipe} from './pipes/payment-mode.pipe';
import {NgMultiSelectDropDownModule} from 'ng-multiselect-dropdown';
import {NgbDateCustomParserFormatter} from './services/ngb-date-custom-parser-formatter';
import {PaymentAddComponent} from './components/payment-add/payment-add.component';
import {StreetComponent} from './components/street/street.component';
import {AuthInterceptorService} from './services/auth-interceptor.service';
import {CookieService} from 'ngx-cookie-service';
import {CoursesComponent} from './components/courses/courses.component';
import {CourseInfoComponent} from './components/course-info/course-info.component';
import {WeekdayLabelPipe} from './pipes/weekday-label.pipe';
import {StudentAddComponent} from './components/student-add/student-add.component';
import {
  CourseAssistantPaymentsComponent
} from './components/course-assistant-payments/course-assistant-payments.component';
import {CourseAddPaymentComponent} from './components/course-add-payment/course-add-payment.component';
import {SmallDateFormatPipe} from './pipes/small-date-format.pipe';
import {CommonDateFormatPipe} from './pipes/common-date-format.pipe';
import {RouterModule} from '@angular/router';
import {ROUTES} from './app.routes';
import {HouseInfoComponent} from './components/house-info/house-info.component';
import {VerificationPipe} from './pipes/verification.pipe';
import {NotificationsComponent} from './components/notifications/notifications.component';

@NgModule({
  declarations: [
    AppComponent,
    ChipsByHouseComponent,
    ConfirmationInputTextComponent,
    CircuitosComponent,
    UsersComponent,
    RolesByUserComponent,
    UserAccountComponent,
    RolePipe,
    ChipsUpdaterComponent,
    PaymentsComponent,
    PaymentFilterComponent,
    PaymentModePipe,
    PaymentAddComponent,
    StreetComponent,
    CoursesComponent,
    CourseInfoComponent,
    WeekdayLabelPipe,
    StudentAddComponent,
    CourseAssistantPaymentsComponent,
    CourseAddPaymentComponent,
    SmallDateFormatPipe,
    CommonDateFormatPipe,
    HouseInfoComponent,
    VerificationPipe,
    NotificationsComponent,
  ],
  imports: [
    NgbModule,
    BrowserModule,
    FormsModule,
    HttpClientModule,
    NgDragDropModule.forRoot(),
    ReactiveFormsModule,
    NgMultiSelectDropDownModule.forRoot(),
    RouterModule.forRoot(ROUTES, {useHash: false})
  ],
  providers: [
    {provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter},
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true},
    CookieService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
