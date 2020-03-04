import { Component, OnInit, OnDestroy } from '@angular/core';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { ConfirmationService } from 'primeng/api';
import { SaidaService } from '../../../../saidas/service/saida.service';
import { Saida, SituacaoSaida } from '../../../../saidas/model/saida';
import { MyWebSocket } from '../../../websocket/websocket.service';
import { Queue } from '../../../websocket/queue';

@Component({
  selector: 'app-emprestimos-pendentes',
  templateUrl: './emprestimos-pendentes.component.html'
})
export class EmprestimosPendentesComponent implements OnInit, OnDestroy {
   
  emprestimos$: Observable<Saida[]>;

  constructor(private saidaService: SaidaService,
              private confirmationService: ConfirmationService,
              private webSocket: MyWebSocket) {}

  ngOnInit() {
    this.emprestimos$ = this.getEmprestimosPendentes();

    this.webSocket.subscribe(Queue.NOVOS_EMPRESTIMOS, function(msg) {
      if (msg.body === 'update') {
        console.log('mensagem emp', msg.body);
        this.emprestimos$ = this.getEmprestimosPendentes();
      }
    }.bind(this));
  }

  private getEmprestimosPendentes() {
    return this.saidaService.getEmprestimosPendentes();
  }

  updateEmprestimo(aprovar: boolean, idSaida: number) {
    const novaSituacao = aprovar ? SituacaoSaida.aprovada : SituacaoSaida.reprovada;
    const msg = aprovar ? 'aprovar' : 'reprovar';
    
    this.confirmationService.confirm({
      header: 'Empréstimos DAELE',
      message: 'Deseja realmente ' + msg + ' este empréstimo?',
      acceptLabel: 'Confirmar',
      rejectLabel: 'Cancelar',
      accept: function() {
        this.emprestimos$ = this.saidaService.updateSituacao(idSaida, novaSituacao)
          .pipe(switchMap(() => this.getEmprestimosPendentes()));
      }.bind(this)
    });
  }

  ngOnDestroy() {
    this.webSocket.unsubscribe(Queue.NOVOS_EMPRESTIMOS);
  }

}