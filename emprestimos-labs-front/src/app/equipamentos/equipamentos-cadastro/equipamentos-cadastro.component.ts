import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Equipamento, GrupoItem } from '../model/equipamento';
import { EquipamentoService } from '../service/equipamentos.service';
import { MyMessageService } from '../../shared/services/my-message.service';

@Component({
  selector: 'app-equipamentos-cadastro',
  templateUrl: './equipamentos-cadastro.component.html'
})
export class EquipamentosCadastroComponent implements OnInit {

  formEquipamento: FormGroup;

  constructor(private activatedRoute: ActivatedRoute,
              private router: Router,
              private formBuilder: FormBuilder,
              private equipamentoService: EquipamentoService,
              private messageService: MyMessageService) { }

  ngOnInit() {
    this.configurarForm();
    this.activatedRoute.params.subscribe(
      () => this.atualizarFormByEquipamento(this.activatedRoute.snapshot.data.equipamento)
    );
  }

  private configurarForm() {
    this.formEquipamento = this.formBuilder.group({
      idEquipamento: [],
      nome: ['', [Validators.required, Validators.maxLength(20)]],
      descricao: ['', Validators.maxLength(100)],
      patrimonio: ['', Validators.required],
      quantidadeMinima: ['', [Validators.required, Validators.min(1)]],
      localizacao: [],
      siorg: ['', Validators.required],
      valorUltimaCompra: [],
      devolucaoObrigatoria: [],
      grupo: []
    });
  }

  private atualizarFormByEquipamento(equipamento) {
    this.formEquipamento.patchValue(equipamento);
  }

  save() {
    const equipamento = this.formEquipamento.getRawValue() as Equipamento;

    this.equipamentoService.save(equipamento.idEquipamento, equipamento).subscribe(
      () => {
        this.formEquipamento.reset();
        this.messageService.showMessage('success', 'Equipamento salvo com sucesso!');
        this.router.navigate(['/equipamentos']);
      }
    );
  }

  getValueConsumo(): string {
    return GrupoItem.consumo;
  }

  getValuePermanente(): string {
    return GrupoItem.permanente;
  }
}
