import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { EquipamentoFiltro } from '../../model/equipamento-filtro';

@Component({
  selector: 'app-equipamentos-filtro',
  templateUrl: './equipamentos-filtro.component.html'
})
export class EquipamentosFiltroComponent implements OnInit {
  
  formFiltro: FormGroup;
  @Output() onPesquisarEquipamentos = new EventEmitter();

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.formFiltro = this.formBuilder.group({
      patrimonio: '',
      nome: '',
      localizacao: ''
    });    
  }

  devePesquisarEquipamentos() {
    const equipamentoFiltro = this.formFiltro.getRawValue() as EquipamentoFiltro;
    this.onPesquisarEquipamentos.emit({equipamentoFiltro});
  }
}