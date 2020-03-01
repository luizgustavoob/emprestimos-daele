import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { CalendarBase } from 'src/app/shared/calendar.base';
import { SelectItem } from 'primeng/api';
import { EquipamentoService } from '../../equipamentos/service/equipamentos.service';
import { FichaDeEstoqueService } from './ficha-de-estoque.service';

@Component({
  selector: 'app-ficha-de-estoque',
  templateUrl: './ficha-de-estoque.component.html'
})
export class FichaDeEstoqueComponent extends CalendarBase implements OnInit {

  formRelatorio: FormGroup;
  equipamentos: SelectItem[] = [];

  constructor(private formBuilder: FormBuilder,
              private equipamentoService: EquipamentoService,
              private estoqueService: FichaDeEstoqueService) {
    super();
  }

  ngOnInit(): void {
    this.configurarForm();
    this.carregarEquipamentos();
  }

  private configurarForm() {
    this.formRelatorio = this.formBuilder.group({
      data: [new Date(), Validators.required],
      equipamentos: []
    });
  }

  private carregarEquipamentos() {
    this.equipamentoService.findAll().subscribe(
      (resp) => resp.map(equip => this.equipamentos.push({label: equip.nome, value: equip.idEquipamento}))
    );
  }

  visualizar() {
    const data = this.formRelatorio.get('data').value;
    const equipamentos = this.formRelatorio.get('equipamentos').value;

    this.estoqueService.relatorioFichaDeEstoque(data, equipamentos).subscribe(
      (relatorio) => {
        const url = window.URL.createObjectURL(relatorio);
        window.open(url);
      });
  }
}
