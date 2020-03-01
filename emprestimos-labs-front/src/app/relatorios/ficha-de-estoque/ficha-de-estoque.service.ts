import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';

import * as moment from 'moment';

const URL = environment.api_estoque;

@Injectable({
  providedIn: 'root'
})
export class FichaDeEstoqueService {

  constructor(private http: HttpClient) { }

  relatorioFichaDeEstoque(data: Date, idsEquipamentos: number[]) {
    const url = `${URL}/ficha-estoque`;
    let params = new HttpParams();
    params = params.set('data', moment(data).format('YYYY-MM-DD'));
    if (idsEquipamentos && idsEquipamentos.length > 0) {
      params = params.set('idsEquipamentos', idsEquipamentos.toString());
    }
    return this.http.get(url, {params, responseType: 'blob'});
  }
}
