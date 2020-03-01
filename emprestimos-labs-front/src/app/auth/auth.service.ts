import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UsuarioService } from '../usuarios/service/usuario.service';
import { environment } from 'src/environments/environment';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient, private usuarioService: UsuarioService) { }

  authenticate(username: string, password: string) {
    return this.httpClient.post(environment.api_auth, { username, password }, { observe: 'response'} )
      .pipe(
        tap(resp => {
          const token = resp.headers.get('Authorization');
          this.usuarioService.setToken(token);
        })
      );
  }
}
