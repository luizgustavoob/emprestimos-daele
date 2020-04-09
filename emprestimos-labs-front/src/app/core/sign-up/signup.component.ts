import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EmailJaCadastradoValidator } from '../../shared/validators/email-ja-cadastrado.validator';
import { Usuario } from '../../usuarios/model/usuario';
import { Permissao } from '../../usuarios/model/permissao';
import { UsuarioService } from '../../usuarios/service/usuario.service';
import { MyMessageService } from '../../shared/services/my-message.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html'
})
export class SignUpComponent implements OnInit {

  signupForm: FormGroup;
  @Input() config: ConfigSignUp;

  constructor(public activeModal: NgbActiveModal,
              private formBuilder: FormBuilder,
              private emailExisteValidator: EmailJaCadastradoValidator,
              private usuarioService: UsuarioService,
              private messageService: MyMessageService) { }
  
  ngOnInit(): void {
    this.signupForm = this.formBuilder.group({
      nome: ['', [Validators.required, Validators.minLength(5)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(50)],
        this.emailExisteValidator.verificaEmailJaCadastrado()],
      nrora: '',
      senha: ['', Validators.required],
      permissao: [Permissao.aluno, Validators.required]
    });
  }  

  private validarCadastro(usuario: Usuario): boolean {
    if (usuario.permissao === Permissao.aluno && !usuario.nrora) {
      this.messageService.showMessage('info', 'Alunos devem informar o número do RA!');
      return false;
    } else if (usuario.permissao !== Permissao.aluno && usuario.nrora) {
      this.messageService.showMessage('info', 'Apenas alunos devem informar o número do RA!');
      return false;
    }

    return true;
  }

  signup() {
    let usuario = this.signupForm.getRawValue() as Usuario;
    usuario.ativo = !this.config.openByLogin;
    
    if (!this.validarCadastro(usuario)) {
      return;
    }

    this.usuarioService.save(0, usuario)
      .subscribe(() => {
        this.signupForm.reset();
        this.activeModal.close({ok: true});
      }, 
      err => this.activeModal.close({ok: false, msg: err})
    );
  }

  getValueAluno(): number {
    return Permissao.aluno;
  }

  getValueProfessor(): number {
    return Permissao.professor;
  }

  getValueAdmin(): number {
    return Permissao.admin;
  }

  getValueLaboratorista(): number {
    return Permissao.laboratorista;
  }

}

export class ConfigSignUp {
  title: string;
  showPlaceHolders: boolean;
  openByLogin: boolean;
}