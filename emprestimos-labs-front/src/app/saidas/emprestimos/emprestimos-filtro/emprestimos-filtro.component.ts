import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SelectItem } from 'primeng/api';

import { Usuario } from '../../../usuarios/model/usuario';
import { SituacaoSaidaDescricao } from '../../model/situacao-saida-descricao';
import { SituacaoSaida } from '../../model/saida';
import { CalendarBase } from '../../../shared/calendar.base';
import { UsuarioService } from 'src/app/usuarios/service/usuario.service';
import { EmprestimoFiltro } from '../model/emprestimo-filtro';

@Component({
  selector: 'app-emprestimos-filtro',
  templateUrl: './emprestimos-filtro.component.html'
})
export class EmprestimosFiltroComponent extends CalendarBase implements OnInit {
  
  formFiltro: FormGroup;
  usuarios: Usuario[] = [];
  situacoes: SelectItem[] = [];
  @Output() onPesquisarEmprestimos = new EventEmitter();
  
  constructor(private formBuilder: FormBuilder, private usuarioService: UsuarioService) {
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


  loadUsuarios(event) {
    this.usuarioService.findByNroraOrEmail(event.query)
      .subscribe( res => this.usuarios = res );
  }

  devePesquisarEmprestimos() {
    const emprestimosFiltro = this.formFiltro.getRawValue() as EmprestimoFiltro;
    this.onPesquisarEmprestimos.emit({emprestimosFiltro});
  }

}