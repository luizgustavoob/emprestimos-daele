import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { UsuarioService } from '../../usuarios/service/usuario.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html'
})
export class ChangePasswordComponent implements OnInit {

  changePasswordForm: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              public activeModal: NgbActiveModal,
              private usuarioService: UsuarioService) { }
  
  ngOnInit(): void {
    this.changePasswordForm = this.formBuilder.group({
      senhaAtual: ['', Validators.required],
      novaSenha: ['', Validators.required]
    });
  }

  changePassword() {
    const senhaAtual = this.changePasswordForm.get('senhaAtual').value;
    const novaSenha = this.changePasswordForm.get('novaSenha').value;

    this.usuarioService.changePassword(senhaAtual, novaSenha)
      .subscribe(() => {
        this.changePasswordForm.reset();
        this.usuarioService.logout();        
        this.activeModal.close();
        this.router.navigate(['/login']);
      });
  }
}
