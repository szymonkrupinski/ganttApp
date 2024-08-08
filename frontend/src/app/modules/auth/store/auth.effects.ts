import {Injectable} from "@angular/core";
import {AuthService} from "../../core/services/auth.service";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import * as AuthActions from './auth.actions';
import {catchError, map, of, switchMap} from "rxjs";
import {Router} from "@angular/router";


@Injectable()
export class AuthEffects {
  constructor(
    private actions$: Actions,
    private authService: AuthService,
    private router: Router) {
  }


  login$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.login),
      switchMap(action => {
        return this.authService
          .login(action.loginData)
          .pipe(
            map((user) =>
              AuthActions.loginSuccess({user: {...user}}),
            ),
            catchError(error =>
              of(AuthActions.loginFailure({error: 'ERROR'}))
            )
          )
      })
    )
  );


  register$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.register),
      switchMap(action => {
        return this.authService
          .register(action.registerData).pipe(
            map((user) => {
              this.router.navigate(['/login-page']);
              return AuthActions.registerSuccess();
            }),
            catchError(error =>
              of(AuthActions.registerFailure({error: 'ERROR'})))
          )
      })
    )
  );


}
