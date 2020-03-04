import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Usuario } from '../../usuarios/model/usuario';
import { UsuarioService } from '../../usuarios/service/usuario.service';
import { AuthService } from '../auth/auth.service';
import { MyMessageService } from '../../shared/services/my-message.service';
import { EmailJaCadastradoValidator } from '../../shared/validators/email-ja-cadastrado.validator';
import { Permissao } from '../../usuarios/model/permissao';

declare var $: any;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  signupForm: FormGroup;
  forgotPasswordForm: FormGroup;
  private fromUrl: string;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private authService: AuthService,
              private usuarioService: UsuarioService,
              private messageService: MyMessageService,
              private emailExisteValidator: EmailJaCadastradoValidator,
              private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe(params => this.fromUrl = params.fromUrl);
    this.configurarLogin();
    this.configurarSignup();
    this.configurarForgotPassword();
  }

  private configurarLogin() {
    this.loginForm = this.formBuilder.group({
      loginEmail: ['', [Validators.required, Validators.email]],
      loginSenha: ['', Validators.required]
    });
  }

  private configurarSignup() {
    this.signupForm = this.formBuilder.group({
      nome: ['', [Validators.required, Validators.minLength(5)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(50)],
        this.emailExisteValidator.verificaEmailJaCadastrado()],
      nrora: '',
      senha: ['', Validators.required],
      permissao: [Permissao.aluno, Validators.required]
    });

    $('#signup').on('hidden.bs.modal', function() {
      $(this).find('form')[0].reset();
    });
  }

  private configurarForgotPassword() {
    this.forgotPasswordForm = this.formBuilder.group({
      forgotEmail: [ '', [Validators.required, Validators.email]]
    });
  }

  login() {
    const email = this.loginForm.get('loginEmail').value;
    const senha = this.loginForm.get('loginSenha').value;

    this.authService.authenticate(email, senha).subscribe(
      () => {
        if (this.fromUrl) {
          this.router.navigateByUrl(this.fromUrl, {replaceUrl: true});
        } else {
          this.router.navigate(['/home'], {replaceUrl: true});
        }
      }
    );
  }

  signup() {
    const usuario = this.signupForm.getRawValue() as Usuario;

    if (!this.usuarioService.validarCadastro(usuario)) {
      return;
    }

    this.usuarioService.save(0, usuario).subscribe(
      () => {
        this.signupForm.reset();
        this.signupForm.get('permissao').setValue(Permissao.aluno);
        $('#signup').modal('hide');
        this.messageService.showMessage('success', 'Usuário cadastrado com sucesso. Aguardando aprovação.');
      }
    );
  }

  forgotPassword() {
    const email = this.forgotPasswordForm.get('forgotEmail').value;

    this.usuarioService.forgotPassword(email).subscribe(
      () => {
        this.forgotPasswordForm.reset();
        $('#forgotPassword').modal('hide');
        this.messageService.showMessage('success', 'Uma nova senha foi enviada para o seu email.');
      }
    );
  }

  getValueAluno(): number {
    return Permissao.aluno;
  }

  getValueProfessor(): number {
    return Permissao.professor;
  }
}
