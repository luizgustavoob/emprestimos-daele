import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenService } from '../token/token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor {

  constructor(private router: Router, private tokenService: TokenService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.tokenService.isTokenExpired()) {
      this.tokenService.removeToken();
      this.router.navigate(['/login']);
      return;
    }

    req = req.clone({
      headers: req.headers
        .append('Content-Type', 'application/json')
        .append('Accept', 'application/json')
    });

    if (this.tokenService.hasToken()) {
      const token = this.tokenService.getToken();
      req = req.clone({
        headers: req.headers.append('Authorization', 'Bearer ' + token)
      });
    }

    return next.handle(req);
  }
}
