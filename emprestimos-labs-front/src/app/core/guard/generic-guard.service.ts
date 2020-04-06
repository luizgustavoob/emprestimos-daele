import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { UsuarioService } from '../../usuarios/service/usuario.service';

@Injectable({
  providedIn: 'root'
})
export class GenericGuardService implements CanActivate {

  constructor(private usuarioService: UsuarioService, 
              private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (!this.usuarioService.isLogged()) {
      this.usuarioService.removeToken();
      this.router.navigate(['login'], {queryParams: {fromUrl: state.url}});
      return false;
    }

    if (route.data.roles && !this.usuarioService.hasAnyRole(route.data.roles)) {
      this.router.navigate(['nao-autorizado']);
      return false;
    }

    return true;
  }
}
