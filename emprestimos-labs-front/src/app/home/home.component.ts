import { Component } from '@angular/core';
import { UsuarioService } from '../usuarios/service/usuario.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent {
  
  constructor(private usuarioService: UsuarioService) {}

  isAdminOrLaboratorista(): boolean {
    return this.usuarioService.isAdmin() || this.usuarioService.isLaboratorista();
  }
}
