import { Component, ViewChild, Input, Output, EventEmitter } from '@angular/core';
import { Table } from 'primeng/table';
import { Page } from '../../shared/page';
import { UsuarioDto } from '../model/usuario-dto';
import { LazyLoadEvent, ConfirmationService } from 'primeng/api';
import { UsuarioService } from '../service/usuario.service';
import { MyMessageService } from '../../shared/services/my-message.service';

@Component({
  selector: 'app-usuarios-tabela',
  templateUrl: './usuarios-tabela.component.html'
})
export class UsuariosTabelaComponent {
  
  @ViewChild('tableUsuarios') table: Table;  
  @Input() usuarios: Page<UsuarioDto[]>;
  @Output() onLazyLoad = new EventEmitter();

  constructor(private usuarioService: UsuarioService, 
              private confirmationService: ConfirmationService, 
              private messageService: MyMessageService) { }
  
  lazyLoad(event: LazyLoadEvent) {
    const page = event.first / event.rows;
    const size = event.rows;    
    this.onLazyLoad.emit({page, size}); 
  }

  alterarSituacao(idUsuario: number, ativo: boolean) {
    const operacao = ativo ? 'inativar' : 'ativar';
    this.confirmationService.confirm({
      header: 'Empréstimos DAELE',
      message: `Deseja realmente ${operacao} o usuário?`,
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
}