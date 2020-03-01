import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable, of } from 'rxjs';
import { Equipamento } from '../model/equipamento';
import { EquipamentoService } from '../service/equipamentos.service';

@Injectable({
  providedIn: 'root'
})
export class EquipamentosCadastroResolver implements Resolve<Observable<Equipamento>> {

  constructor(private equipamentoService: EquipamentoService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Equipamento> {
    if (route.params.id) {
      const idEquipamento = route.params.id;
      return this.equipamentoService.findById(idEquipamento);
    } else {
      return of(new Equipamento());
    }
  }
}
