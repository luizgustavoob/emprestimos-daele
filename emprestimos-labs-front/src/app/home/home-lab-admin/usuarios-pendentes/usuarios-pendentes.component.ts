import { Component, OnInit, OnDestroy } from '@angular/core';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { ConfirmationService } from 'primeng/api';
import { UsuarioService } from '../../../usuarios/service/usuario.service';
import { Usuario } from '../../../usuarios/model/usuario';
import { MyWebSocket } from '../../../websocket/websocket.service';
import { Queue } from './../../../websocket/queue';

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
    
    this.webSocket.subscribe(Queue.NOVOS_USUARIOS, function(msg) {
      if (msg.body === 'update') {
        this.usuarios$ = this.getUsuariosPendentes();
      }
    }.bind(this));
  }

  getUsuariosPendentes() {
    return this.usuarioService.getUsuariosPendentes();
  }

  updateUsuario(aprovar: boolean, idUsuario: number) {
    const msg = aprovar ? 'aprovar' : 'reprovar';
    
    this.confirmationService.confirm({
      header: 'Empréstimos DAELE',
      message: 'Deseja realmente ' + msg + ' este usuário?',
      acceptLabel: 'Confirmar',
      rejectLabel: 'Cancelar',
      accept: function() {
        this.usuarios$ = this.usuarioService.updateStatus(idUsuario, aprovar)
         .pipe(switchMap(() => this.getUsuariosPendentes()));
      }.bind(this)
    });
  }

  ngOnDestroy() {
    this.webSocket.unsubscribe(Queue.NOVOS_USUARIOS);
  }
}