import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable, of } from 'rxjs';
import { Fornecedor } from '../model/fornecedor';
import { FornecedorService } from '../service/fornecedor.service';

@Injectable({
  providedIn: 'root'
})
export class FornecedorCadastroResolver implements Resolve<Observable<Fornecedor>> {

  constructor(private fornecedorService: FornecedorService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Fornecedor> {
    if (route.params.id) {
      const idFornecedor = route.params.id;
      return this.fornecedorService.findById(idFornecedor);
    } else {
      return of(new Fornecedor());
    }
  }

}
