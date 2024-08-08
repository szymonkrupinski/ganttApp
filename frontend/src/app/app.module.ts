import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {AuthModule} from "./modules/auth/auth.module";
import {CoreModule} from "./modules/core/core.module";
import {StoreModule} from "@ngrx/store";
import {authReducer} from "./modules/auth/store/auth.reducer";
import {HttpClientModule} from "@angular/common/http";


@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AuthModule,
    CoreModule,
    StoreModule.forRoot({auth: authReducer}, {}),
    HttpClientModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
