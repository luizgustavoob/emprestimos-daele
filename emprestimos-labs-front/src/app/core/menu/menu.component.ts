import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UsuarioService } from '../../usuarios/service/usuario.service';
import { ChangePasswordComponent } from '../change-password/change-password.component';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent {

  isNavbarCollapsed: boolean = true;

  constructor(private router: Router, 
              private usuarioService: UsuarioService,
              private modalService: NgbModal) {}
  
  logout() {
    this.usuarioService.logout();    
    this.router.navigate(['/login']);
  }

  changePassword() {
    this.modalService.open(ChangePasswordComponent);
  }

}