import { Component } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { UsuarioDto } from './model/usuario-dto';
import { UsuarioFiltro } from './model/usuario-filtro';
import { UsuarioService } from './service/usuario.service';
import { MyMessageService } from '../shared/services/my-message.service';

import { SignUpComponent } from '../core/sign-up/signup.component';
import { Page } from '../shared/page';

@Component({
  selector: 'app-usuarios',
  templateUrl: './usuarios.component.html'
})
export class UsuariosComponent {

  usuarioFiltro: UsuarioFiltro = new UsuarioFiltro();
  usuarios: Page<UsuarioDto> = Object.assign({});

  constructor(private usuarioService: UsuarioService,
              private modalService: NgbModal,
              private messageService: MyMessageService) { }
  
  onPesquisarUsuarios(event) {
    this.usuarioFiltro = event.usuarioFiltro;
    this.usuarioFiltro.page = 0;
    this.usuarioFiltro.size = 10;
    this.findUsuariosByFiltros();
  }

  private findUsuariosByFiltros() {
    this.usuarioService.findByFiltros(this.usuarioFiltro)
      .subscribe( res => this.usuarios = res );
  }

  onCadastrarUsuario(event) {
    const modalRef = this.modalService.open(SignUpComponent, {windowClass: 'fadeIn'});
    modalRef.componentInstance.config = {
      title: 'Cadastro de Usuário',
      showPlaceHolders: false,
      openByLogin: false
    };
    modalRef.result.then(
      (res) => {
        this.messageService.showMessage('success', 'Usuário cadastrado com sucesso. Aguardando aprovação.')
        this.findUsuariosByFiltros();
      },
      (res) => {
        if (res.msg) {
          console.log(res.msg);
          this.messageService.showMessage('info', `Atenção: ${res.msg}`);
        }
      }      
    );
  }

  onLazyLoad(event) {
    this.usuarioFiltro.page = event.page;
    this.usuarioFiltro.size = event.size
    setTimeout(() => this.findUsuariosByFiltros(), 200);
  }
}
