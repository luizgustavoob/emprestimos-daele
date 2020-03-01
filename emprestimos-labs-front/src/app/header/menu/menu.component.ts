import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsuarioService } from 'src/app/usuarios/service/usuario.service';
import { Router } from '@angular/router';

declare var $: any;

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  changePasswordForm: FormGroup;

  constructor(private formBuilder: FormBuilder, 
              private router: Router, 
              private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.changePasswordForm = this.formBuilder.group({
      senhaAtual: ['', Validators.required],
      novaSenha: ['', Validators.required]
    });

    $('#changePassword').on('hidden.bs.modal', function() {
      $(this).find('form')[0].reset();
    });
  }
  
  logout() {
    this.usuarioService.logout();    
    this.router.navigate(['/login']);
  }

  changePassword() {
    const senhaAtual = this.changePasswordForm.get('senhaAtual').value;
    const novaSenha = this.changePasswordForm.get('novaSenha').value;

    this.usuarioService.changePassword(senhaAtual, novaSenha).subscribe(
      () => {
        this.changePasswordForm.reset();
        $('#changePassword').modal('hide');
        this.logout();
      }
    );
  }
}