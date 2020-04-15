import { Component, ViewChild, Input, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { Table } from 'primeng/table';
import { ConfirmationService, LazyLoadEvent } from 'primeng/api';
import { MyMessageService } from '../../../shared/services/my-message.service';
import { EntradaService } from '../../service/entrada.service';
import { Entrada } from '../../model/entrada';
import { Page } from './../../../shared/page';

@Component({
  selector: 'app-entradas-tabela',
  templateUrl: './entradas-tabela.component.html'
})
export class EntradasTabelaComponent {

  @ViewChild('tableEntradas') table: Table;
  @Input() entradas: Page<Entrada>;
  @Output() onLazyLoad = new EventEmitter();

  constructor(private router: Router,
              private entradaService: EntradaService,
              private confirmationService: ConfirmationService,
              private messageService: MyMessageService) { }

  lazyLoad(event: LazyLoadEvent) {
    const page = event.first / event.rows;
    const size = event.rows;    
    this.onLazyLoad.emit({page, size});        
  }

  edit(idEntrada: number) {
    this.router.navigate(['entradas', idEntrada]);
  }

  remove(idEntrada: number) {
    this.confirmationService.confirm({
      header: 'Deseja realmente excluir o registro?',
      message: 'Essa ação não poderá ser desfeita.',
      acceptLabel: 'Excluir',
      rejectLabel: 'Cancelar',
      accept: () => {
        this.entradaService.delete(idEntrada).subscribe(
          () => {
            this.table.reset();
            this.messageService.showMessage('success', 'Lançamento de entrada removido com sucesso!');
          }
        );
      }
    });
  }

}