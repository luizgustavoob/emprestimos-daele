import { Component, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ConfirmationService, LazyLoadEvent } from 'primeng/api';
import { Saida } from '../model/saida';
import { SaidaService } from '../service/saida.service';
import { MyMessageService } from '../../shared/services/my-message.service';
import { Table } from 'primeng/table';

@Component({
  selector: 'app-baixas-estoque-lista',
  templateUrl: './baixas-estoque-lista.component.html'
})
export class BaixasEstoqueListaComponent {

  @ViewChild('tableBaixas') table: Table;
  baixasDeEstoque: Saida[] = [];
  totalRecords = 0;
  private currentPage = 0;
  private maxRecords = 10;

  constructor(private saidaService: SaidaService,
              private router: Router,
              private confirmationService: ConfirmationService,
              private messageService: MyMessageService) { }

  findBaixasDeEstoque() {
    this.saidaService.getBaixasDeEstoque(this.currentPage, this.maxRecords).subscribe(
      (resp) => {
        this.totalRecords = resp.totalElements;
        this.baixasDeEstoque = resp.content;
      }
    );
  }

  loadLazy(event: LazyLoadEvent) {
    this.currentPage = event.first / event.rows;
    this.maxRecords = event.rows;
    setTimeout(() => this.findBaixasDeEstoque());
  }

  edit(idBaixa: number ) {
    this.router.navigate(['/baixas-estoque', idBaixa]);
  }

  remove(idBaixa: number) {
    this.confirmationService.confirm({
      header: 'Deseja realmente excluir o registro?',
      message: 'Essa ação não poderá ser desfeita.',
      acceptLabel: 'Excluir',
      rejectLabel: 'Cancelar',
      accept: () => {
        this.saidaService.delete(idBaixa).subscribe(
          () => {
            this.table.reset();
            this.messageService.showMessage('success', 'Baixa de estoque removida com sucesso!');
          }
        );
      }
    });
  }
}
