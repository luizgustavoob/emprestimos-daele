import { Directive, OnInit, Input, HostBinding } from '@angular/core';
import { UsuarioService } from 'src/app/usuarios/service/usuario.service';

@Directive({
  selector: '[appUserRole]'
})
export class UserRoleDirective implements OnInit {

  @Input('appUserRole') roles: string[];
  @HostBinding('style.display') display: string;

  constructor(private usuarioService: UsuarioService) { }

  ngOnInit(): void {
    this.display =
      this.usuarioService.hasAnyRole(this.roles) ? 'block' : 'none';
  }
}
