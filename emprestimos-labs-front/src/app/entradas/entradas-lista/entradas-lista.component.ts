import { Component } from '@angular/core';
import { EntradaService } from '../service/entrada.service';
import { EntradaFiltro } from '../model/entrada-filtro';
import { Entrada } from '../model/entrada';
import { Page } from './../../shared/page';

@Component({
  selector: 'app-entradas-lista',
  templateUrl: './entradas-lista.component.html'
})
export class EntradasListaComponent {

  private entradaFiltro: EntradaFiltro;
  entradas: Page<Entrada> = Object.assign({});

  constructor(private entradaService: EntradaService) { }

  private findEntradasByFiltros() {
    this.entradaService.findEntradasByFiltros(this.entradaFiltro)
      .subscribe( (res) => this.entradas = res );
  }

  onPesquisarEntradas(event) {
    this.entradaFiltro = event.entradaFiltro;
    this.entradaFiltro.page = 0;
    this.entradaFiltro.size = 10;

    this.findEntradasByFiltros();
  }

  onLazyLoadTabela(event) {
    this.entradaFiltro.page = event.page;
    this.entradaFiltro.size = event.size;

    setTimeout(() => this.findEntradasByFiltros(), 200);
  }  
}
