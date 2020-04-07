import { Component, OnInit, OnDestroy } from '@angular/core';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { ConfirmationService } from 'primeng/api';
import { UsuarioService } from '../../../../usuarios/service/usuario.service';
import { Usuario } from '../../../../usuarios/model/usuario';
import { MyWebSocket } from '../../../websocket/websocket.service';
import { QueueUtils } from '../../../websocket/queue-util';

@Component({
  selector: 'app-usuarios-pendentes',
  templateUrl: './usuarios-pendentes.component.html'
})
export class UsuariosPendentesComponent implements OnInit, OnDestroy {
 
  usuarios$: Observable<Usuario[]>;

  constructor(private usuarioService: UsuarioService, 
              private webSocket: MyWebSocket,
              private confirmationService: ConfirmationService) { }

  ngOnInit() {
    this.usuarios$ = this.getUsuariosPendentes();

    this.webSocket.subscribe(QueueUtils.NOVOS_USUARIOS, 
      (msg) => {
        if (msg.body === 'update') {
          this.usuarios$ = this.getUsuariosPendentes();
        }
      });
  }

  private getUsuariosPendentes() {
    return this.usuarioService.getUsuariosPendentes();
  }

  updateUsuario(aprovar: boolean, idUsuario: number) {
    const msg = aprovar ? 'aprovar' : 'reprovar';
    
    this.confirmationService.confirm({
      header: 'Empréstimos DAELE',
      message: 'Deseja realmente ' + msg + ' este usuário?',
      acceptLabel: 'Confirmar',
      rejectLabel: 'Cancelar',
      accept: () => this.usuarios$ = this.usuarioService.updateStatus(idUsuario, aprovar)
         .pipe(switchMap(() => this.getUsuariosPendentes()))
    });
  }

  ngOnDestroy() {
    this.webSocket.unsubscribe(QueueUtils.NOVOS_USUARIOS);
  }
}