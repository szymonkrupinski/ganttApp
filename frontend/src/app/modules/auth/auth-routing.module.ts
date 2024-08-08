import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {RegisterComponent} from "./components/register/register.component";
import {ActivationComponent} from "./components/activation/activation.component";
import {RecoveryPasswordComponent} from "./components/recovery-password/recovery-password.component";
import {RecoveryFormComponent} from "./components/recovery-form/recovery-form.component";

const routes: Routes = [
  {path: 'login-page', component: LoginComponent, title: 'Strona logowania'},
  {path: 'register-page', component: RegisterComponent, title: 'Utwórz konto'},
  {path: 'aktywacja/:uid', component: ActivationComponent, title: "Aktywuj konto"},
  {path: 'odzyskiwanie-hasla', component: RecoveryPasswordComponent, title: "Odzyskaj hasło"},
  {path: 'odzyskiwanie-hasla/:uid', component: RecoveryFormComponent, title: "Nowe hasło"},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule {
}


