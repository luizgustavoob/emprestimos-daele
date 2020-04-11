import { Component, ViewChild, Output, EventEmitter, Input } from '@angular/core';
import { Table } from 'primeng/table';
import { Router } from '@angular/router';

import { ConfirmationService, LazyLoadEvent } from 'primeng/api';
import { MyMessageService } from '../../../shared/services/my-message.service';
import { SaidaService } from '../../service/saida.service';
import { SituacaoSaida, Saida } from '../../model/saida';
import { getBadgeClass } from '../../../shared/situacao-saida-badge-class';
import { Page } from '../../../shared/page';

@Component({
  selector: 'app-emprestimos-tabela',
  templateUrl: './emprestimos-tabela.component.html'
})
export class EmprestimosTabelaComponent {

  @ViewChild('tableEmprestimos', {static: false}) table: Table;
  @Input() emprestimos: Page<Saida>;
  @Output() onLazyLoad = new EventEmitter();

  constructor(private router: Router,
              private saidaService: SaidaService,
              private confirmationService: ConfirmationService,
              private messageService: MyMessageService) { }

  lazyLoad(event: LazyLoadEvent) {
    const page = event.first / event.rows;
    const size = event.rows;    
    this.onLazyLoad.emit({page, size});
  }

  edit(idEmprestimo: number) {
    this.router.navigate(['/emprestimos', idEmprestimo]);
  }

  remove(idEmprestimo: number) {
    this.confirmationService.confirm({
      header: 'Deseja realmente excluir o registro?',
      message: 'Essa ação não poderá ser desfeita.',
      acceptLabel: 'Excluir',
      rejectLabel: 'Cancelar',
      accept: () => this.saidaService.delete(idEmprestimo)
        .subscribe(() => {
          this.table.reset();
          this.messageService.showMessage('success', 'Empréstimo removido com sucesso!');
        })
    });
  }

  finalize(idEmprestimo: number) {
    this.confirmationService.confirm({
      header: 'Empréstimos DAELE',
      message: 'Deseja realmente encerrar este empréstimo?',
      acceptLabel: 'Encerrar',
      rejectLabel: 'Cancelar',
      accept: () => this.saidaService.updateSituacao(idEmprestimo, SituacaoSaida.encerrada)
        .subscribe(() => {
          this.table.reset();
          this.messageService.showMessage('success', 'Empréstimo atualizado com sucesso!');
        })      
    });
  }

  getBadgeClass(situacao: SituacaoSaida): string {
    return getBadgeClass(situacao); 
  }

  emprestimoAprovado(situacao: SituacaoSaida): boolean {
    return situacao === SituacaoSaida.aprovada;
  }

  emprestimoNaoEncerrado(situacao: SituacaoSaida): boolean {
    return situacao !== SituacaoSaida.encerrada;
  }
  
}