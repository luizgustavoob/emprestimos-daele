import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable, of } from 'rxjs';
import { Entrada } from '../model/entrada';
import { EntradaService } from '../service/entrada.service';

@Injectable({
  providedIn: 'root'
})
export class EntradasCadastroResolver implements Resolve<Observable<Entrada>> {

  constructor(private entradaService: EntradaService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Entrada>  {
    if (route.params.id) {
      const idEntrada = route.params.id;
      return this.entradaService.findById(idEntrada);
    } else {
      return of(new Entrada());
    }
  }
}
