import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { CrudService } from '../../shared/services/crud.service';
import { Entrada } from '../model/entrada';
import { EntradaFiltro } from '../model/entrada-filtro';
import { Page } from '../../shared/page';
import { environment } from '../../../environments/environment';
import * as moment from 'moment';

@Injectable({
  providedIn: 'root'
})
export class EntradaService extends CrudService<Entrada, number> {

  constructor(http: HttpClient) {
    super(environment.api_entradas, http);
  }

  findById(id: number): Observable<Entrada> {
    const url = `${this.getUrl()}/${id}`;
    return this.http.get<Entrada>(url).pipe(
      map(entrada => {
        entrada.data = moment(entrada.data).toDate();
        entrada.valorTotal = 0;
        for (const item of entrada.itens) {
          item.valorTotal = item.valorUnitario * item.quantidade;
          entrada.valorTotal += item.valorTotal;
        }
        return entrada;
      })
    );
  }

  findAll(): Observable<Entrada[]> {
    const url = `${this.getUrl()}`;
    return this.http.get<Entrada[]>(url).pipe(
      map((resp: Entrada[]) => {
        resp.map(entrada => {
          entrada.data = moment(entrada.data).toDate();
          entrada.valorTotal = 0;
          for (const item of entrada.itens) {
            item.valorTotal = item.valorUnitario * item.quantidade;
            entrada.valorTotal += item.valorTotal;
          }
        });
        return resp;
      })
    );
  }

  findAllPaginated(page: number, size: number): Observable<Page<Entrada>> {
    const url = `${this.getUrl()}`;

    let params = new HttpParams();
    params = params.set('page', page.toString());
    params = params.set('size', size.toString());

    return this.http.get<Page<Entrada>>(url, { params }).pipe(
      map((resp: Page<Entrada>) => {
        resp.content.map(entrada => {
          entrada.data = moment(entrada.data).toDate();
          entrada.valorTotal = 0;
          for (const item of entrada.itens) {
            item.valorTotal = item.valorUnitario * item.quantidade;
            entrada.valorTotal += item.valorTotal;
          }
        });
        return resp;
      })
    );
  }

  findEntradasByFiltros(entradaFiltro: EntradaFiltro): Observable<Page<Entrada>> {
    const url = `${this.getUrl()}/filter?`;
    let params = new HttpParams();

    params = params.set('page', entradaFiltro.page.toString());
    params = params.set('size', entradaFiltro.size.toString());

    if (entradaFiltro.data) {
      params = params.set('data', moment(entradaFiltro.data).format('YYYY-MM-DD'));
    }

    if (entradaFiltro.fornecedor) {
      params = params.set('fornecedor', entradaFiltro.fornecedor);
    }

    return this.http.get<Page<Entrada>>(url, { params }).pipe(
      map((resp: Page<Entrada>) => {
        resp.content.map(entrada => {
          entrada.data = moment(entrada.data).toDate();
          entrada.valorTotal = 0;
          for (const item of entrada.itens) {
            item.valorTotal = item.valorUnitario * item.quantidade;
            entrada.valorTotal += item.valorTotal;
          }
        });
        return resp;
      })
    );
  }

  save(id: number, entrada: Entrada): Observable<Entrada> {
    entrada.data = moment(entrada.data).add(environment.moment_fuso_horario, 'hours').toDate();
    if (id) {
      return super.edit(id, entrada);
    } else {
      return super.insert(entrada);
    }
  }
}
