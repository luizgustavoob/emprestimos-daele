import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserLogged } from '../usuarios/model/user-logged';
import { UsuarioService } from '../usuarios/service/usuario.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {
  
  userLogged$: Observable<UserLogged>;
  
  constructor(private router: Router, private usuarioService: UsuarioService) {}
  
  ngOnInit() {
    this.userLogged$ = this.usuarioService.getUser();
  }

  logout() {
    this.usuarioService.logout();    
    this.router.navigate(['/login']);
  }
}
