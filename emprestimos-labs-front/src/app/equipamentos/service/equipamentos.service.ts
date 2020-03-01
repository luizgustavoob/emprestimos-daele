import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CrudService } from '../../shared/services/crud.service';
import { Equipamento } from '../model/equipamento';
import { EquipamentoFiltro } from '../model/equipamento-filtro';
import { environment } from 'src/environments/environment';
import { Page } from '../../shared/page';
import { EquipamentoDto } from '../model/equipamento-dto';

@Injectable({
  providedIn: 'root'
})
export class EquipamentoService extends CrudService<Equipamento, number> {

  constructor(http: HttpClient) {
    super(environment.api_equipamentos, http);
  }

  findByNomeOrPatrimonioOrSiorg(nomePatrimonioOuSiorg: string): Observable<Equipamento[]> {
    const url = `${this.getUrl()}/search?`;
    const params = new HttpParams().set('chavePesquisa', nomePatrimonioOuSiorg);
    return this.http.get<Equipamento[]>(url, {params});
  }

  findEquipamentosByFiltros(equipamentoFiltro: EquipamentoFiltro): Observable<Page<Equipamento>> {
    const url = `${this.getUrl()}/filter?`;
    let params = new HttpParams();
    params = params.set('page', equipamentoFiltro.page.toString());
    params = params.set('size', equipamentoFiltro.size.toString());

    if (equipamentoFiltro.patrimonio) {
      params = params.set('patrimonio', equipamentoFiltro.patrimonio.toString());
    }

    if (equipamentoFiltro.nome) {
      params = params.set('nome', equipamentoFiltro.nome);
    }

    if (equipamentoFiltro.localizacao) {
      params = params.set('localizacao', equipamentoFiltro.localizacao);
    }

    return this.http.get<Page<Equipamento>>(url, {params});
  }

  findEquipamentosComEstoqueEsgotando(): Observable<EquipamentoDto[]> {
    const url = `${environment.api_estoque}/estoque-esgotando`;
    return this.http.get<EquipamentoDto[]>(url);
  }
}
