import { Component } from '@angular/core';
import { Saida } from '../model/saida';
import { SaidaService } from '../service/saida.service';
import { EmprestimoFiltro } from './model/emprestimo-filtro';
import { Page } from '../../shared/page';

@Component({
  selector: 'app-emprestimos-lista',
  templateUrl: './emprestimos-lista.component.html'
})
export class EmprestimosListaComponent {

  emprestimosFiltro: EmprestimoFiltro;  
  emprestimos: Page<Saida> = Object.assign({});
  
  constructor(private saidaService: SaidaService) {  }

  private findEmprestimosByFiltros() {
    this.saidaService.findEmprestimosByFiltros(this.emprestimosFiltro)
      .subscribe( res => this.emprestimos = res );
  }

  onPesquisarEmprestimos(event) {
    console.log('Filtro', event.emprestimosFiltro);
    this.emprestimosFiltro = event.emprestimosFiltro;
    this.emprestimosFiltro.page = 0;
    this.emprestimosFiltro.size = 10;
    this.findEmprestimosByFiltros();
  }

  onLazyLoadTabela(event) {
    this.emprestimosFiltro.page = event.page;
    this.emprestimosFiltro.size = event.size;
    setTimeout(() => this.findEmprestimosByFiltros(), 200);
  }

}
