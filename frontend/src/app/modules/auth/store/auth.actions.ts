import {createAction, props} from "@ngrx/store";
import {IUser, LoginData, RegisterData} from "../../core/models/auth.model";

// TYPY DLA LOGOWANIA
const LOGIN_TYPE = '[Auth] Login';
const LOGIN_SUCCESS = '[Auth] Login Success';
const LOGIN_FAILURE = '[Auth] Login Failure';

// TYPY DLA REJESTRACJI
const REGISTER_TYPE = '[Auth] Register';
const REGISTER_SUCCESS = '[Auth] Register Success';
const REGISTER_FAILURE = '[Auth] Register Failure';

const CLEAR_ERRORS = '[Auth] Clear Errors';

// AKCJE LOGOWANIA
export const login = createAction(
  LOGIN_TYPE,
  props<{ loginData: LoginData }>()
);

export const loginSuccess = createAction(
  LOGIN_SUCCESS,
  props<{ user: IUser }>()
);

export const loginFailure = createAction(
  LOGIN_FAILURE,
  props<{ error: string }>()
);

// AKCJE REJESTRACJI
export const register = createAction(
  REGISTER_TYPE,
  props<{ registerData: RegisterData }>()
);

export const registerSuccess = createAction(
  REGISTER_SUCCESS
);

export const registerFailure = createAction(
  REGISTER_FAILURE,
  props<{ error: string }>()
);

export const clearError = createAction(
  CLEAR_ERRORS
);
