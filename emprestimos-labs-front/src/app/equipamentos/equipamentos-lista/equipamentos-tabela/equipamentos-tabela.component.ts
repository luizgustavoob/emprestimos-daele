import { Component, Input, Output, EventEmitter, ViewChild } from '@angular/core';
import { LazyLoadEvent, ConfirmationService } from 'primeng/api';
import { Table } from 'primeng/table';
import { EquipamentoService } from '../../service/equipamentos.service';
import { Equipamento } from '../../model/equipamento';
import { Page } from './../../../shared/page';
import { Router } from '@angular/router';
import { MyMessageService } from './../../../shared/services/my-message.service';

@Component({
  selector: 'app-equipamentos-tabela',
  templateUrl: './equipamentos-tabela.component.html'
})
export class EquipamentosTabelaComponent {
  
  @ViewChild('tableEquipamentos', {static: false}) table: Table;
  @Input() equipamentos: Page<Equipamento[]>;
  @Output() onLazyLoad = new EventEmitter();

  constructor(private router: Router, 
              private confirmationService: ConfirmationService,
              private equipamentoService: EquipamentoService,
              private messageService: MyMessageService) { }

  lazyLoad(event: LazyLoadEvent) {
    const page = event.first / event.rows;
    const size = event.rows;    
    this.onLazyLoad.emit({page, size}); 
  }

  edit(idEquipamento: number) {
    this.router.navigate(['/equipamentos/', idEquipamento]);
  }

  remove(idFornecedor: number) {
    this.confirmationService.confirm({
      header: 'Deseja realmente excluir o registro?',
      message: 'Essa ação não poderá ser desfeita.',
      acceptLabel: 'Excluir',
      rejectLabel: 'Cancelar',
      accept: () => {
        this.equipamentoService.delete(idFornecedor).subscribe(
          () => {
            this.table.reset();
            this.messageService.showMessage('success', 'Equipamento removido com sucesso!');
          }
        );
      }
    });
  }
}