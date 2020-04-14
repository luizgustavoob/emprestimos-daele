import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { CrudService } from '../../shared/services/crud.service';
import { Saida, SituacaoSaida, TipoSaida } from '../model/saida';
import { Page } from '../../shared/page';
import { environment } from '../../../environments/environment';
import { EmprestimoFiltro } from '../emprestimos/model/emprestimo-filtro';
import { MyMessageService } from './../../shared/services/my-message.service';
import * as moment from 'moment';

@Injectable({
  providedIn: 'root'
})
export class SaidaService extends CrudService<Saida, number> {

  constructor(http: HttpClient,
              private messageService: MyMessageService) {
    super(environment.api_saidas, http);
  }

  findById(id: number): Observable<Saida> {
    const url = `${this.getUrl()}/${id}`;
    return this.http.get<Saida>(url).pipe(
      map(saida => {
        saida.data = moment(saida.data).toDate();
        saida.itens.map(item => {
          if (item.dataDevolucao) {
            item.dataDevolucao = moment(item.dataDevolucao).toDate();
          }
        });
        return saida;
      })
    );
  }

  findEmprestimosByFiltros(emprestimosFiltro: EmprestimoFiltro): Observable<Page<Saida>> {
    const url = `${this.getUrl()}/filter?`;
    let params = new HttpParams();
    params = params.set('page', emprestimosFiltro.page.toString());
    params = params.set('size', emprestimosFiltro.size.toString());

    if (emprestimosFiltro.data) {
      params = params.set('data', moment(emprestimosFiltro.data).format('YYYY-MM-DD'));
    }

    if (emprestimosFiltro.usuario) {
      params = params.set('nrora', emprestimosFiltro.usuario.nrora.toString());
    }

    if (emprestimosFiltro.situacao && emprestimosFiltro.situacao.length > 0) {
      params = params.set('situacao', emprestimosFiltro.situacao.toString());
    }

    return this.http.get<Page<Saida>>(url, { params }).pipe(
      map((resp: Page<Saida>) => {
        resp.content.map(saida => {
          saida.data = moment(saida.data).toDate();
          saida.itens.map(item => {
            if (item.dataDevolucao) {
              item.dataDevolucao = moment(item.dataDevolucao).toDate();
            }
          });
        });
        return resp;
      })
    );
  }

  getEmprestimosDoUsuario(page: number, size: number): Observable<Page<Saida>> {
    let params = new HttpParams();
    params = params.set('page', page.toString());
    params = params.set('size', size.toString());

    const url = `${this.getUrl()}/meus-emprestimos?`;
    return this.http.get<Page<Saida>>(url, { params }).pipe(
      map((resp: Page<Saida>) => {
        resp.content.map(saida => {
          saida.data = moment(saida.data).toDate();
          saida.itens.map(item => {
            if (item.dataDevolucao) {
              item.dataDevolucao = moment(item.dataDevolucao).toDate();
            }
          });
        });
        return resp;
      })
    );
  }

  getEmprestimosPendentes(): Observable<Saida[]> {
    const url = `${this.getUrl()}/emprestimos-pendentes`;
    return this.http.get<Saida[]>(url).pipe(
      map(saidas => {
        saidas.map(saida => {
          saida.data = moment(saida.data).toDate();
          saida.itens.map(item => {
            if (item.dataDevolucao) {
              item.dataDevolucao = moment(item.dataDevolucao).toDate();
            }
          });
        });
        return saidas;
      })
    );
  }

  updateSituacao(idSaida: number, situacao: SituacaoSaida) {
    const url = `${this.getUrl()}/${idSaida}/situacao`;
    const situacaoForm = {
      situacao: situacao.valueOf()
    };
    return this.http.patch(url, situacaoForm);
  }

  save(id: number, saida: Saida): Observable<Saida> {
    if (saida.tipoSaida === TipoSaida.baixa) {
      saida.situacao = SituacaoSaida.encerrada;
      saida.finalidadeSaida = null;
    }

    saida.data = moment(saida.data).add(environment.moment_fuso_horario, 'hours').toDate();
    saida.itens.map(item => {
      if (item.dataDevolucao) {
        item.dataDevolucao = moment(item.dataDevolucao).add(environment.moment_fuso_horario, 'hours').toDate();
      }
    });

    if (id) {
      return super.edit(id, saida);
    } else {
      return super.insert(saida);
    }
  }

  getBaixasDeEstoque(page: number, size: number): Observable<Page<Saida>> {
    const url = `${this.getUrl()}/baixas`;
    let params = new HttpParams();
    params = params.set('page', page.toString());
    params = params.set('size', size.toString());
    return this.http.get<Page<Saida>>(url, {params}).pipe(
      map((resp: Page<Saida>) => {
        resp.content.map(saida => saida.data = moment(saida.data).toDate());
        return resp;
      })
    );
  }

  podeEncerrarEmprestimo(saida: Saida): boolean {
    let retorno = true;

    for (const item of saida.itens) {
      if (!item.quantidadeDevolvida || !item.dataDevolucao) {
        retorno = false;
        this.messageService.showMessage('info',
          'Informe a quantidade devolvida e a data de devolução do ' + item.equipamento.nome);
        break;
      }
    }

    return retorno;
  }
}
