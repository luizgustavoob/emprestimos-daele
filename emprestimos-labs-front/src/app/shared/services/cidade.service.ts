import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CrudService } from './crud.service';
import { Cidade } from '../cidade';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CidadeService extends CrudService<Cidade, number> {

  constructor(http: HttpClient) {
    super(environment.api_cidades, http);
  }

  findByNomeOrUF(nomeOuUF: string): Observable<Cidade[]> {
    const url = `${this.getUrl()}/search?`;
    const params = new HttpParams().set('chavePesquisa', nomeOuUF);

    return this.http.get<Cidade[]>(url, { params });
  }
}
