export interface IUser {
  login: string;
  email: string;
  role: string;
}


export class User implements IUser {
  constructor(
    public login: string,
    public email: string,
    public role: string) {
  }
}

export interface LoginData {
  login: string;
  password: string;
}

export interface RegisterData {
  login: string;
  password: string;
  email: string;
}

export interface AuthResponse {
  timestamp: string;
  message: string;
  code: string;
}

export interface ResetPasswordData {
  email: string;
}

export interface ChangePasswordData {
  password: string;
  uid: string;
}
