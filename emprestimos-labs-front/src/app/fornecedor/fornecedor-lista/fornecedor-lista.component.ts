import { Component, ViewChild, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Table } from 'primeng/table';
import { LazyLoadEvent, ConfirmationService } from 'primeng/api';
import { Fornecedor } from '../model/fornecedor';
import { Cidade } from '../../shared/cidade';
import { FornecedorService } from '../service/fornecedor.service';
import { MyMessageService } from '../../shared/services/my-message.service';

@Component({
  selector: 'app-fornecedor-lista',
  templateUrl: './fornecedor-lista.component.html'
})
export class FornecedorListaComponent implements OnInit {

  @ViewChild('tableFornecedores') table: Table;
  fornecedores: Fornecedor[] = [];
  cidades: Cidade[] = [];
  totalRecords = 0;
  private currentPage = 0;
  private maxRecords = 10;

  constructor(private fornecedorService: FornecedorService,
              private router: Router,
              private confirmationService: ConfirmationService,
              private messageService: MyMessageService) { }

  ngOnInit(): void {
    this.findFornecedores(this.currentPage, this.maxRecords);
  }

  private findFornecedores(page: number, size: number) {
    this.fornecedorService.findAllPaginated(page, size).subscribe(
      (resp) => {
        this.totalRecords = resp.totalElements;
        this.fornecedores = resp.content;
      }
    );
  }

  loadLazy(event: LazyLoadEvent) {
    this.currentPage = event.first / event.rows;
    this.maxRecords = event.rows;
    setTimeout(() => this.findFornecedores(this.currentPage, this.maxRecords), 200);
  }

  edit(idFornecedor: number) {
    this.router.navigate(['/fornecedores/', idFornecedor]);
  }

  remove(idFornecedor: number) {
    this.confirmationService.confirm({
      header: 'Deseja realmente excluir o registro?',
      message: 'Essa ação não poderá ser desfeita.',
      acceptLabel: 'Excluir',
      rejectLabel: 'Cancelar',
      accept: () => {
        this.fornecedorService.delete(idFornecedor).subscribe(
          () => {
            this.table.reset();
            this.messageService.showMessage('success', 'Fornecedor removido com sucesso!');
          }
        );
      }
    });
  }
}
