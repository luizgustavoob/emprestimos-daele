import { PipeTransform, Pipe } from '@angular/core';
import { SituacaoSaida } from '../../../saidas/model/saida';

@Pipe({
  name: 'situacaoSaida'
})
export class SituacaoSaidaPipe implements PipeTransform {

  transform(value: any, ...args: any[]) {
    switch (value) {
      case SituacaoSaida.pendente:
        return 'Pendente';
      case SituacaoSaida.aprovada:
        return 'Aprovado';
      case SituacaoSaida.encerrada:
        return 'Encerrado';
      case SituacaoSaida.reprovada:
        return 'Reprovado';
    }
  }
}
