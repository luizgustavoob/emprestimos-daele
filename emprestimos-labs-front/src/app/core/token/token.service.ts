import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

const KEY = 'labs-token';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  private jwtHelper: JwtHelperService;

  constructor() {
    this.jwtHelper = new JwtHelperService();
  }

  hasToken() {
    const token = this.getToken();
    return !!token;
  }

  isTokenExpired() {
    const token = this.getToken();
    return !!token && this.jwtHelper.isTokenExpired(token);
  }

  setToken(token: string) {
    localStorage.setItem(KEY, token);
  }

  getToken(): string {
    return localStorage.getItem(KEY);
  }

  removeToken() {
    if (this.getToken()) {
      localStorage.removeItem(KEY);
    }
  }

  getPayload() {
    const payload = JSON.stringify(this.jwtHelper.decodeToken(this.getToken()));
    return JSON.parse(payload);
  }
}
