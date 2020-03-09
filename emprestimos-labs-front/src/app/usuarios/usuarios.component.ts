import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Usuario } from './model/usuario';
import { UsuarioDto } from './model/usuario-dto';
import { UsuarioFiltro } from './model/usuario-filtro';
import { UsuarioService } from './service/usuario.service';
import { MyMessageService } from '../shared/services/my-message.service';
import { EmailJaCadastradoValidator } from '../shared/validators/email-ja-cadastrado.validator';
import { Permissao } from './model/permissao';
import { Page } from '../shared/page';

@Component({
  selector: 'app-usuarios',
  templateUrl: './usuarios.component.html'
})
export class UsuariosComponent implements OnInit {
  
  usuarioFiltro: UsuarioFiltro;
  usuarios: Page<UsuarioDto> = Object.assign({});
  formUsuario: FormGroup;
  showDialog = false;

  constructor(private formBuilder: FormBuilder,
              private usuarioService: UsuarioService,
              private messageService: MyMessageService,
              private emailExisteValidator: EmailJaCadastradoValidator) { }

  ngOnInit() {
    this.formUsuario = this.formBuilder.group({
      nome: ['', [Validators.required, Validators.minLength(5)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(50)],
        this.emailExisteValidator.verificaEmailJaCadastrado()],
      nrora: '',
      senha: ['', [Validators.required]],
      permissao: [Permissao.aluno]
    });
  }

  private findUsuariosByFiltros() {
    this.usuarioService.findByFiltros(this.usuarioFiltro).subscribe( res => this.usuarios = res; );
  }

  onPesquisarUsuarios(event) {
    this.usuarioFiltro = event.usuarioFiltro;
    this.usuarioFiltro.page = 0;
    this.usuarioFiltro.size = 10;

    this.findUsuariosByFiltros();
  }

  onCadastrarUsuario(event) {
    this.showDialog = event.novoUsuario;
  }

  onLazyLoad(event) {
    this.usuarioFiltro.page = event.page;
    this.usuarioFiltro.size = event.size
    setTimeout(() => this.findUsuariosByFiltros(), 200);
  }

  salvarUsuario() {
    const usuario = this.formUsuario.getRawValue() as Usuario;
    usuario.ativo = true;
    
    if (!this.usuarioService.validarCadastro(usuario)) {
      return;
    }   

    this.usuarioService.save(0, usuario).subscribe(
      () => {
        this.formUsuario.reset();
        this.showDialog = false;
        this.messageService.showMessage('success', 'Usuário cadastrado com sucesso!');
      }
    );
  }

  getValueAluno(): number {
    return Permissao.aluno;
  }

  getValueLaboratorista(): number {
    return Permissao.laboratorista;
  }

  getValueAdmin(): number {
    return Permissao.admin;
  }

  getValueProfessor(): number {
    return Permissao.professor;
  }
}
