import { Component } from '@angular/core';
import { Equipamento } from '../model/equipamento';
import { EquipamentoFiltro } from '../model/equipamento-filtro';
import { EquipamentoService } from '../service/equipamentos.service';
import { Page } from 'src/app/shared/page';

@Component({
  selector: 'app-equipamentos-lista',
  templateUrl: './equipamentos-lista.component.html'
})
export class EquipamentosListaComponent {

  equipamentoFiltro: EquipamentoFiltro;
  equipamentos: Page<Equipamento> = Object.assign({});

  constructor(private equipamentoService: EquipamentoService) { }
  
  private findEquipamentosByFiltros() {
    this.equipamentoService.findEquipamentosByFiltros(this.equipamentoFiltro)
      .subscribe( (res) => this.equipamentos = res );
  }

  onPesquisarEquipamentos(event) {
    this.equipamentoFiltro = event.equipamentoFiltro;
    this.equipamentoFiltro.page = 0;
    this.equipamentoFiltro.size = 10;

    this.findEquipamentosByFiltros();
  }

  onLazyLoadTabela(event) {
    this.equipamentoFiltro.page = event.page;
    this.equipamentoFiltro.size = event.size;
    
    setTimeout(() => this.findEquipamentosByFiltros(), 200);
  }  
}
