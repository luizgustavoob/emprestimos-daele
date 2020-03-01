import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CrudService } from '../../shared/services/crud.service';
import { Fornecedor } from '../model/fornecedor';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FornecedorService extends CrudService<Fornecedor, number> {

  constructor(http: HttpClient) {
    super(environment.api_fornecedores, http);
  }

  findByRazaoSocialOuCNPJ(razaoSocialOuCNPJ: string): Observable<Fornecedor[]> {
    const url = `${this.getUrl()}/search?`;
    const params = new HttpParams().set('chavePesquisa', razaoSocialOuCNPJ);

    return this.http.get<Fornecedor[]>(url, { params });
  }
}
