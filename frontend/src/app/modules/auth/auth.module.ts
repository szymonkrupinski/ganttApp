import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {AuthRoutingModule} from './auth-routing.module';
import {LoginComponent} from './components/login/login.component';
import {RegisterComponent} from './components/register/register.component';
import {ReactiveFormsModule} from "@angular/forms";
import {ActivationComponent} from './components/activation/activation.component';
import {RecoveryPasswordComponent} from "./components/recovery-password/recovery-password.component";
import {RecoveryFormComponent} from './components/recovery-form/recovery-form.component';


@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent,
    ActivationComponent,
    RecoveryPasswordComponent,
    RecoveryFormComponent
  ],
  imports: [
    CommonModule,
    AuthRoutingModule,
    ReactiveFormsModule
  ]
})
export class AuthModule {
}
