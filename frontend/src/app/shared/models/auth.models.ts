export type Role = 'USER' | 'ADMIN';

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface AuthSession {
  token: string;
  role: Role;
}
