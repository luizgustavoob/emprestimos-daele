import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Table } from 'primeng/table';
import { LazyLoadEvent, ConfirmationService } from 'primeng/api';
import { Usuario } from './model/usuario';
import { UsuarioDto } from './model/usuario-dto';
import { UsuarioFiltro } from './model/usuario-filtro';
import { UsuarioService } from './service/usuario.service';
import { MyMessageService } from '../shared/services/my-message.service';
import { EmailJaCadastradoValidator } from '../shared/validators/email-ja-cadastrado.validator';
import { Permissao } from './model/permissao';

@Component({
  selector: 'app-usuarios',
  templateUrl: './usuarios.component.html'
})
export class UsuariosComponent implements OnInit {

  @ViewChild('tableUsuarios', {static: false}) table: Table;
  formFiltro: FormGroup;
  formUsuario: FormGroup;
  usuarioFiltro: UsuarioFiltro = new UsuarioFiltro();
  usuarios: UsuarioDto[] = [];
  usuariosList: Usuario[] = [];
  totalRecords = 0;
  private currentPage = 0;
  private maxRecords = 10;
  showDialog = false;

  constructor(private formBuilder: FormBuilder,
              private usuarioService: UsuarioService,
              private confirmationService: ConfirmationService,
              private messageService: MyMessageService,
              private emailExisteValidator: EmailJaCadastradoValidator) { }

  ngOnInit() {
    this.formFiltro = this.formBuilder.group({
      usuario: ['']
    });

    this.formUsuario = this.formBuilder.group({
      nome: ['', [Validators.required, Validators.minLength(5)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(50)],
        this.emailExisteValidator.verificaEmailJaCadastrado()],
      nrora: [''],
      senha: ['', [Validators.required]],
      permissao: [Permissao.aluno]
    });
  }

  private findUsuariosByFiltros() {
    this.usuarioService.findByFiltros(this.usuarioFiltro).subscribe(
      (resp) => {
        this.totalRecords = resp.totalElements;
        this.usuarios = resp.content;
      }
    );
  }

  findUsuarios() {
    this.usuarioFiltro.email = this.formFiltro.get('usuario').value.email;
    this.usuarioFiltro.page = this.currentPage;
    this.usuarioFiltro.size = this.maxRecords;
    this.findUsuariosByFiltros();
  }

  loadLazy(event: LazyLoadEvent) {
    this.currentPage = event.first / event.rows;
    this.maxRecords = event.rows;
    this.usuarioFiltro.page = this.currentPage;
    this.usuarioFiltro.size = this.maxRecords;
    setTimeout(() => this.findUsuariosByFiltros(), 200);
  }

  loadUsuarios(event) {
    this.usuarioService.findByNroraOrEmail(event.query).subscribe(
      resp => this.usuariosList = resp
    );
  }

  alterarSituacao(idUsuario: number, ativo: boolean) {
    const msg = ativo ? 'inativar' : 'ativar';
    this.confirmationService.confirm({
      header: 'Empréstimos DAELE',
      message: 'Deseja realmente ' + msg + ' o usuário?',
      acceptLabel: 'Confirmar',
      rejectLabel: 'Cancelar',
      accept: () => {
        this.usuarioService.updateStatus(idUsuario, !ativo).subscribe(
          () => {
            this.table.reset();
            this.messageService.showMessage('success', 'Usuário atualizado com sucesso!');
          }
        );
      }
    });
  }

  cadastrarUsuario() {
    this.showDialog = true;
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
