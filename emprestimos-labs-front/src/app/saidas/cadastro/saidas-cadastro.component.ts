import { Component, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl, NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Table } from 'primeng/table';
import { Dropdown } from 'primeng/dropdown';
import { CalendarBase } from './../../shared/calendar.base';
import { Saida, SituacaoSaida, FinalidadeSaida, TipoSaida } from '../model/saida';
import { SituacaoSaidaDescricao } from '../model/situacao-saida-descricao';
import { SaidaItem } from '../model/saida-item';
import { Equipamento } from '../../equipamentos/model/equipamento';
import { SaidaService } from '../service/saida.service';
import { EquipamentoService } from '../../equipamentos/service/equipamentos.service';
import { UsuarioService } from '../../usuarios/service/usuario.service';
import { MyMessageService } from '../../shared/services/my-message.service';

@Component({
  selector: 'app-emprestimos-cadastro',
  templateUrl: './saidas-cadastro.component.html'
})
export class SaidasCadastroComponent extends CalendarBase implements OnInit {

  @ViewChild('tableItens', {static: false}) tableItens: Table;
  saidaForm: FormGroup;
  saida: Saida = Object.assign({});
  itemSaida: SaidaItem = Object.assign({});
  equipamentos: Equipamento[] = [];
  situacoes: SituacaoSaidaDescricao[] = [];
  finalidades: FinalidadeSaidaDescricao[] = [];
  showDialog = false;
  private encerrando = false;
  private editando = false;
  titulo = '';

  constructor(private activatedRoute: ActivatedRoute,
              private router: Router,
              private formBuilder: FormBuilder,
              private usuarioService: UsuarioService,
              private saidaService: SaidaService,
              private equipamentoService: EquipamentoService,
              private messageService: MyMessageService) {
    super();
  }

  ngOnInit() {
    this.activatedRoute.params.subscribe(
      () => {
        this.saida = this.activatedRoute.snapshot.data.saida;
        this.configurarForm();
        this.setTitulo();
        this.carregarFinalidades();
        this.carregarDropdowns();
        if (this.saida.unauthorized) {
          this.router.navigate(['/nao-autorizado']);
        } else if (!this.saida.enable) {
          this.saidaForm.disable();
        }
      }
    );
  }

  private configurarForm() {
    this.saidaForm = this.formBuilder.group({
      idSaida: new FormControl({value: '', disabled: true}),
      situacao: new FormControl({value: '', disabled: !this.isAdminOrLaboratorista()},
        this.isEmprestimo() ? Validators.required : null),
      finalidadeSaida: new FormControl('', this.isEmprestimo() ? Validators.required : null),
      data: new FormControl('', Validators.required),
      observacao: new FormControl('', Validators.maxLength(200))
    });
  }

  private carregarDropdowns() {
    if (this.isEmprestimo()) {
      this.carregarSituacoesListBySituacaoAtual(this.saida.situacao);
      this.saidaForm.get('situacao').setValue(this.getSituacaoDescricao(this.saida.situacao));
      this.saidaForm.get('finalidadeSaida').setValue(this.getFinalidadeDescricao(this.saida.finalidadeSaida));
    }
  }

  private setTitulo() {
    if (this.isEmprestimo()) {
      this.titulo = 'Empréstimo de Equipamentos';
    } else {
      this.titulo = 'Baixa de Estoque';
    }
  }

  private carregarSituacoesListBySituacaoAtual(situacaoAtual: SituacaoSaida) {
    this.situacoes = [];

    switch (situacaoAtual) {
      case SituacaoSaida.pendente:
        this.situacoes.push(
          new SituacaoSaidaDescricao(SituacaoSaida.pendente, 'Pendente'));
        this.situacoes.push(
          new SituacaoSaidaDescricao(SituacaoSaida.aprovada, 'Aprovado'));
        this.situacoes.push(
          new SituacaoSaidaDescricao(SituacaoSaida.reprovada, 'Reprovado'));
        break;
      case SituacaoSaida.aprovada:
        this.situacoes.push(
          new SituacaoSaidaDescricao(SituacaoSaida.aprovada, 'Aprovado'));
        this.situacoes.push(
          new SituacaoSaidaDescricao(SituacaoSaida.encerrada, 'Encerrado'));
        break;
      case SituacaoSaida.encerrada:
        this.situacoes.push(
          new SituacaoSaidaDescricao(SituacaoSaida.encerrada, 'Encerrado'));
        break;
      case SituacaoSaida.reprovada:
        this.situacoes.push(
          new SituacaoSaidaDescricao(SituacaoSaida.reprovada, 'Reprovado'));
        break;
    }
  }

  private carregarFinalidades() {
    this.finalidades.push(
      new FinalidadeSaidaDescricao(FinalidadeSaida.projeto, 'Projeto'));
    this.finalidades.push(
      new FinalidadeSaidaDescricao(FinalidadeSaida.aulaPratica, 'Aula Prática'));
  }

  private getSituacaoDescricao(situacao: SituacaoSaida): SituacaoSaidaDescricao {
    return this.situacoes.find( (sit) => sit.situacao === situacao);
  }

  private getFinalidadeDescricao(finalidade: FinalidadeSaida): FinalidadeSaidaDescricao {
    return this.finalidades.find( (fin) => fin.finalidade === finalidade );
  }

  addItem() {
    this.itemSaida = new SaidaItem();
    this.editando = false;
    this.showDialog = true;
  }

  editItem(equipamento: Equipamento) {
    const itemEmp = this.saida.itens.find(
      item => item.equipamento.idEquipamento === equipamento.idEquipamento
    );
    this.itemSaida = this.cloneItem(itemEmp);
    this.editando = true;
    this.showDialog = true;
  }

  private cloneItem(item: SaidaItem): SaidaItem {
    const itemRetorno = new SaidaItem();
    // tslint:disable-next-line: forin
    for (const prop in item) {
      itemRetorno[prop] = item[prop];
    }
    return itemRetorno;
  }

  removeItem(equipamento: Equipamento) {
    this.saida.itens = this.saida.itens.filter(
      item => item.equipamento.idEquipamento !== equipamento.idEquipamento
    );
    this.tableItens.reset();
  }

  private podeSalvarItem(): boolean {
    let retorno = true;

    if (!this.itemSaida.equipamento.idEquipamento) {
      retorno = false;
      this.messageService.showMessage('info', 'Necessário selecionar o item desejado!');
    } else if (!this.editando &&
        this.saida.itens.find((item) => item.equipamento.idEquipamento === this.itemSaida.equipamento.idEquipamento)) {
      retorno = false;
      this.messageService.showMessage('info', 'Item ' + this.itemSaida.equipamento.nome + ' já adicionado!');
    }

    return retorno;
  }

  saveItem(formItem: NgForm) {
    if (!this.podeSalvarItem()) {
      return;
    }

    const saidaItem = this.saida.itens.find(
      (item) => item.equipamento.idEquipamento === this.itemSaida.equipamento.idEquipamento
    );

    if (saidaItem) {
      saidaItem.equipamento = Object.assign(saidaItem.equipamento, this.itemSaida.equipamento);
      saidaItem.quantidade = this.itemSaida.quantidade;
      saidaItem.quantidadeDevolvida = this.itemSaida.quantidadeDevolvida;
      saidaItem.dataDevolucao = this.itemSaida.dataDevolucao;
    } else {
      let novoitem = new SaidaItem();
      novoitem = Object.assign(novoitem, this.itemSaida);
      this.saida.itens.push(this.itemSaida);
    }

    this.itemSaida = Object.assign({});
    this.showDialog = false;
    this.tableItens.reset();
    formItem.reset();
  }

  save() {
    if (this.encerrandoEmprestimo() && !this.saidaService.podeEncerrarEmprestimo(this.saida)) {
      return;
    }

    this.saidaService.save(this.saida.idSaida, this.saida).subscribe(
      () => {
        this.saidaForm.reset();

        if (this.isEmprestimo()) {
          this.messageService.showMessage('success', 'Empréstimo salvo com sucesso!');
          this.router.navigate(this.isAdminOrLaboratorista() ? ['/emprestimos'] : ['/home']);
        } else {
          this.messageService.showMessage('success', 'Baixa de estoque salva com sucesso!');
          this.router.navigate(['/baixas-estoque']);
        }
      }
    );
  }

  loadEquipamentos(event) {
    this.equipamentoService.findByNomeOrPatrimonioOrSiorg(event.query).subscribe(
      (resp) => this.equipamentos = resp
    );
  }

  disabledSave(): boolean {
    return (this.saidaForm.invalid || this.saida.itens.length === 0 || 
      (this.encerrandoEmprestimo() && this.saida.itens.filter(item => !item.dataDevolucao || !item.quantidadeDevolvida).length > 0));
  }

  disabledSaveItem(): boolean {
    if (this.encerrandoEmprestimo()) {
      return !this.itemSaida.quantidadeDevolvida || !this.itemSaida.dataDevolucao ||
        (this.itemSaida.quantidadeDevolvida > this.itemSaida.quantidade);
    } else if (typeof this.itemSaida.equipamento === 'string') {
      return true;
    } else {
      return !this.itemSaida.equipamento || !this.itemSaida.quantidade;
    }
  }

  isAdminOrLaboratorista(): boolean {
    return this.usuarioService.isAdmin() || this.usuarioService.isLaboratorista();
  }

  setSituacaoSaida(dropdown: Dropdown) {
    this.saida.situacao = dropdown.value.situacao;
    this.encerrando = (this.saida.situacao === SituacaoSaida.encerrada);
  }

  setFinalidadeSaida(dropdown: Dropdown) {
    this.saida.finalidadeSaida = dropdown.value.finalidade;
  }

  saidaHabilitada(): boolean {
    return this.saida.enable;
  }

  isEmprestimo(): boolean {
    return this.saida.tipoSaida === TipoSaida.emprestimo;
  }

  encerrandoEmprestimo(): boolean {
    return this.isEmprestimo() && this.encerrando;
  }

  onHideDialog(formItem: NgForm) {
    if (!formItem.submitted) {
      this.itemSaida = Object.assign(this.itemSaida, {});
      formItem.reset();
    }
    this.editando = false;
  }  

  voltarParaLista() {
    if (this.isEmprestimo()) {
      this.router.navigate(['/emprestimos']);
    } else {
      this.router.navigate(['/baixas-estoque']);
    }
  }
}

class FinalidadeSaidaDescricao {

  constructor(public readonly finalidade: FinalidadeSaida,
              public readonly descricao: string) { }
}
