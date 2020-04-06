import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Table } from 'primeng/table';
import { LazyLoadEvent, ConfirmationService } from 'primeng/api';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Usuario } from './model/usuario';
import { UsuarioDto } from './model/usuario-dto';
import { UsuarioFiltro } from './model/usuario-filtro';
import { UsuarioService } from './service/usuario.service';
import { MyMessageService } from '../shared/services/my-message.service';
import { SignUpComponent } from '../core/sign-up/signup.component';

@Component({
  selector: 'app-usuarios',
  templateUrl: './usuarios.component.html'
})
export class UsuariosComponent implements OnInit {

  @ViewChild('tableUsuarios', {static: false}) table: Table;
  formFiltro: FormGroup;
  usuarioFiltro: UsuarioFiltro = new UsuarioFiltro();
  usuarios: UsuarioDto[] = [];
  usuariosList: Usuario[] = [];
  totalRecords = 0;
  private currentPage = 0;
  private maxRecords = 10;  

  constructor(private formBuilder: FormBuilder,
              private usuarioService: UsuarioService,
              private confirmationService: ConfirmationService,
              private messageService: MyMessageService,              
              private modalService: NgbModal) { }

  ngOnInit() {
    this.formFiltro = this.formBuilder.group({
      usuario: ''
    });
  }

  private findUsuariosByFiltros() {
    this.usuarioService.findByFiltros(this.usuarioFiltro)
      .subscribe(res => {
        this.totalRecords = res.totalElements;
        this.usuarios = res.content;
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
    this.usuarioService.findByNroraOrEmail(event.query)
      .subscribe(res => this.usuariosList = res);
  }

  alterarSituacao(idUsuario: number, ativo: boolean) {
    const msg = ativo ? 'inativar' : 'ativar';
    this.confirmationService.confirm({
      header: 'Empréstimos DAELE',
      message: 'Deseja realmente ' + msg + ' o usuário?',
      acceptLabel: 'Confirmar',
      rejectLabel: 'Cancelar',
      accept: () => {
        this.usuarioService.updateStatus(idUsuario, !ativo)
          .subscribe(() => {
            this.table.reset();
            this.messageService.showMessage('success', 'Usuário atualizado com sucesso!');
          }
        );
      }
    });
  }

  cadastrarUsuario() {
    const modalRef = this.modalService.open(SignUpComponent);
    modalRef.componentInstance.config = {
      title: 'Cadastro de Usuário',
      showPlaceHolders: false,
      openByLogin: false
    };
    modalRef.result.then(
      (res) => this.messageService.showMessage('success', 'Usuário cadastrado com sucesso!'),
      (res) => {
        if (res.msg) {
          console.log(res.msg);
          this.messageService.showMessage('info', `Atenção: ${res.msg}`);
        }
      }      
    );
  }  
}
