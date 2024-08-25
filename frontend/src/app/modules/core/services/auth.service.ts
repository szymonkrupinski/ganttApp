import {Injectable} from '@angular/core';
import {environment} from "../../../../environments/environment";
import {HttpClient, HttpParams} from "@angular/common/http";
import {
  AuthResponse,
  ChangePasswordData,
  IUser,
  LoginData,
  RegisterData,
  ResetPasswordData
} from "../models/auth.model";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  apiUrl = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) {
  }

  login(body: LoginData): Observable<IUser> {
    return this.http.post<IUser>(`${this.apiUrl}/login`, body)
  }

  logout(): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(`${this.apiUrl}/logout`)
  }

  register(body: RegisterData): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, body)
  }

  activateAcc(uid: string): Observable<AuthResponse> {
    const params = new HttpParams().set("uid", uid)
    return this.http.get<AuthResponse>(`${this.apiUrl}/activate`, {params: params})
  }

  resetPassword(body: ResetPasswordData): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/reset-password`, body)
  }

  changePassword(body: ChangePasswordData): Observable<AuthResponse> {
    return this.http.patch<AuthResponse>(`${this.apiUrl}/reset-password/:uid`, body)
  }


}
