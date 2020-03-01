import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { CalendarBase } from './../../../shared/calendar.base';
import { EntradaFiltro } from '../../model/entrada-filtro';
import { FornecedorService } from './../../../fornecedor/service/fornecedor.service';
import { Fornecedor } from './../../../fornecedor/model/fornecedor';

@Component({
  selector: 'app-entradas-filtro',
  templateUrl: './entradas-filtro.component.html'
})
export class EntradasFiltroComponent extends CalendarBase implements OnInit {

  formFiltro: FormGroup;
  fornecedores: Fornecedor[];
  @Output() onPesquisarEntradas = new EventEmitter();

  constructor(private formBuilder: FormBuilder, private fornecedorService: FornecedorService) {
    super();
  }

  ngOnInit() {
    this.formFiltro = this.formBuilder.group({
      data: '',
      fornecedor: ''
    });
  }

  loadFornecedores(event) {
    this.fornecedorService.findByRazaoSocialOuCNPJ(event.query)
      .subscribe( (res) => this.fornecedores = res );
  }

  devePesquisarEntradas() {
    const entradaFiltro = this.formFiltro.getRawValue() as EntradaFiltro;
    this.onPesquisarEntradas.emit({entradaFiltro});
  }

}