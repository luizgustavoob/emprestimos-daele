import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Saida, SituacaoSaida, TipoSaida } from '../model/saida';
import { SaidaService } from '../service/saida.service';
import { UsuarioService } from '../../usuarios/service/usuario.service';

@Injectable({
  providedIn: 'root'
})
export class SaidasCadastroResolver implements Resolve<Observable<Saida>> {

  constructor(private saidaService: SaidaService, private usuarioService: UsuarioService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Saida> {
    const isBaixa = state.url.trim().toLocaleLowerCase().search('baixa') >= 0;

    if (route.params.id) {
      const idSaida = route.params.id;
      return this.saidaService.findById(idSaida)
        .pipe(
          map( (saida: Saida) => {
            const situacao = saida.situacao;
            const alunoOuProfessor = this.usuarioService.isAluno() || this.usuarioService.isProfessor();
            const alunoOuProfessorConsultando = ( alunoOuProfessor && situacao !== SituacaoSaida.pendente );

            saida.enable = (situacao !== SituacaoSaida.reprovada
              && situacao !== SituacaoSaida.encerrada
              && !alunoOuProfessorConsultando) || isBaixa;

            saida.unauthorized = (alunoOuProfessor && saida.usuario.email !== this.usuarioService.getEmail());

            saida.tipoSaida = isBaixa ? TipoSaida.baixa : TipoSaida.emprestimo;

            return saida;
          })
        );
    } else {
      const saida = new Saida();
      saida.tipoSaida = isBaixa ? TipoSaida.baixa : TipoSaida.emprestimo;
      return of(saida);
    }
  }
}
