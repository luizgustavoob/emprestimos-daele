import { Component, ViewChild, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { Table } from 'primeng/table';
import { LazyLoadEvent, ConfirmationService, SelectItem } from 'primeng/api';
import { CalendarBase } from '../../shared/calendar.base';
import { Saida, SituacaoSaida } from '../model/saida';
import { EmprestimoFiltro } from './emprestimo-filtro';
import { SituacaoSaidaDescricao } from '../model/situacao-saida-descricao';
import { Usuario } from '../../usuarios/model/usuario';
import { SaidaService } from '../service/saida.service';
import { UsuarioService } from '../../usuarios/service/usuario.service';
import { MyMessageService } from '../../shared/services/my-message.service';
import { getBadgeClass } from '../../shared/situacao-saida-badge-class';

@Component({
  selector: 'app-emprestimos-lista',
  templateUrl: './emprestimos-lista.component.html'
})
export class EmprestimosListaComponent extends CalendarBase implements OnInit {

  @ViewChild('tableEmprestimos', {static: false}) table: Table;
  formFiltro: FormGroup;
  saidaFiltro: EmprestimoFiltro = new EmprestimoFiltro();
  emprestimos: Saida[] = [];
  usuarios: Usuario[] = [];
  situacoes: SelectItem[] = [];
  totalRecords = 0;
  private currentPage = 0;
  private maxRecords = 10;

  constructor(private formBuilder: FormBuilder,
              private saidaService: SaidaService,
              private router: Router,
              private usuarioService: UsuarioService,
              private confirmationService: ConfirmationService,
              private messageService: MyMessageService) {
    super();
  }

  ngOnInit(): void {
    this.formFiltro = this.formBuilder.group({
      data: '',
      usuario: '',
      situacao: ''
    });
    this.carregarSituacoes();
  }

  private carregarSituacoes() {
    const situacoesTemp = [];

    situacoesTemp.push(new SituacaoSaidaDescricao(SituacaoSaida.pendente, 'Pendente'));
    situacoesTemp.push(new SituacaoSaidaDescricao(SituacaoSaida.aprovada, 'Aprovado'));
    situacoesTemp.push(new SituacaoSaidaDescricao(SituacaoSaida.encerrada, 'Encerrado'));
    situacoesTemp.push(new SituacaoSaidaDescricao(SituacaoSaida.reprovada, 'Reprovado'));

    situacoesTemp.map(sit => this.situacoes.push({label: sit.descricao, value: sit.situacao}));
  }

  private findEmprestimosByFiltros() {
    this.saidaService.findEmprestimosByFiltros(this.saidaFiltro)
      .subscribe(res => {
        this.totalRecords = res.totalElements;
        this.emprestimos = res.content;
      }
    );
  }

  findEmprestimos() {
    this.saidaFiltro = this.formFiltro.getRawValue() as EmprestimoFiltro;
    this.saidaFiltro.page = this.currentPage;
    this.saidaFiltro.size = this.maxRecords;
    this.findEmprestimosByFiltros();
  }

  loadLazy(event: LazyLoadEvent) {
    this.currentPage = event.first / event.rows;
    this.maxRecords = event.rows;
    this.saidaFiltro.page = this.currentPage;
    this.saidaFiltro.size = this.maxRecords;
    setTimeout(() => this.findEmprestimosByFiltros(), 200);
  }

  loadUsuarios(event) {
    this.usuarioService.findByNroraOrEmail(event.query)
      .subscribe(res => this.usuarios = res);
  }

  edit(idEmprestimo: number) {
    this.router.navigate(['/emprestimos', idEmprestimo]);
  }

  remove(idEmprestimo: number) {
    this.confirmationService.confirm({
      header: 'Deseja realmente excluir o registro?',
      message: 'Essa ação não poderá ser desfeita.',
      acceptLabel: 'Excluir',
      rejectLabel: 'Cancelar',
      accept: () => {
        this.saidaService.delete(idEmprestimo)
          .subscribe(() => {
            this.table.reset();
            this.messageService.showMessage('success', 'Empréstimo removido com sucesso!');
          }
        );
      }
    });
  }

  getBadgeClass(situacao: SituacaoSaida): string {
    return getBadgeClass(situacao);
  }

  encerrarEmprestimo(idEmprestimo: number) {
    this.confirmationService.confirm({
      header: 'Empréstimos DAELE',
      message: 'Deseja realmente encerrar este empréstimo?',
      acceptLabel: 'Encerrar',
      rejectLabel: 'Cancelar',
      accept: () => {
        this.saidaService.updateSituacao(idEmprestimo, SituacaoSaida.encerrada)
          .subscribe(() => {
            this.table.reset();
            this.messageService.showMessage('success', 'Empréstimo atualizado com sucesso!');
          }
        );
      }
    });
  }

  emprestimoAprovado(situacao: SituacaoSaida): boolean {
    return situacao === SituacaoSaida.aprovada;
  }

  emprestimoNaoEncerrado(situacao: SituacaoSaida): boolean {
    return situacao !== SituacaoSaida.encerrada;
  }
}
