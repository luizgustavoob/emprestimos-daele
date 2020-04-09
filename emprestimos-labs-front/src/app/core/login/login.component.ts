import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from '../auth/auth.service';
import { MyMessageService } from '../../shared/services/my-message.service';
import { SignUpComponent } from '../sign-up/signup.component';
import { ForgotPasswordComponent } from '../forgot-password/forgotpassword.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  private fromUrl: string;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private authService: AuthService,
              private messageService: MyMessageService,
              private activatedRoute: ActivatedRoute,
              private modalService: NgbModal) { }

  ngOnInit() {
    this.activatedRoute.queryParams
      .subscribe(params => this.fromUrl = params.fromUrl);
    
    this.loginForm = this.formBuilder.group({
      loginEmail: ['', [Validators.required, Validators.email]],
      loginSenha: ['', Validators.required]
    });
  }

  login() {
    const email = this.loginForm.get('loginEmail').value;
    const senha = this.loginForm.get('loginSenha').value;

    this.authService.authenticate(email, senha)
      .subscribe(() => {
        if (this.fromUrl) {
          this.router.navigateByUrl(this.fromUrl, {replaceUrl: true});
        } else {
          this.router.navigate(['/home'], {replaceUrl: true});
        }
      }
    );
  }

  signup() {
    const modalRef = this.modalService.open(SignUpComponent, {windowClass: 'fadeIn'});
    modalRef.componentInstance.config = {
      title: 'Quero me cadastrar!',
      showPlaceHolders: true,
      openByLogin: true
    };
    modalRef.result.then(
      res => this.messageService.showMessage('success', 'Usuário cadastrado com sucesso. Aguardando aprovação.'),
      res => {
        if (res.msg) {
          console.log(res.msg);
          this.messageService.showMessage('info', `Atenção: ${res.msg}`);
        }
      }      
    );
  }

  forgotPassword() {
    const modalRef = this.modalService.open(ForgotPasswordComponent, {windowClass: 'fadeIn'});
    modalRef.result.then(
      res => this.messageService.showMessage('success', 'Uma nova senha foi enviada para o seu email.'),
      res => {
        if (res.msg) {
          console.log(res.msg);
          this.messageService.showMessage('info', `Atenção: ${res.msg}`);
        }
      }
    );
  }

}
