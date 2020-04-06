import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { UsuarioService } from 'src/app/usuarios/service/usuario.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgotpassword.component.html'
})
export class ForgotPasswordComponent implements OnInit {
  
  forgotPasswordForm: FormGroup;

  constructor(public activeModal: NgbActiveModal,
              private formBuilder: FormBuilder,
              private usuarioService: UsuarioService) { }

  ngOnInit(): void {
    this.forgotPasswordForm = this.formBuilder.group({
      forgotEmail: [ '', [Validators.required, Validators.email]]
    });
  }

  forgotPassword() {
    const email = this.forgotPasswordForm.get('forgotEmail').value;

    this.usuarioService.forgotPassword(email)
      .subscribe(() => {
        this.forgotPasswordForm.reset();
        this.activeModal.close({ok: true});
      },
      err => this.activeModal.close({ok: false, msg: err})
    );
  }
}