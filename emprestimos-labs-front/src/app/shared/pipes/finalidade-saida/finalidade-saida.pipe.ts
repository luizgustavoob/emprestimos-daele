import { FinalidadeSaida } from '../../../saidas/model/saida';
import { PipeTransform, Pipe } from '@angular/core';

@Pipe({
  name: 'finalidadeSaida'
})
export class FinalidadeSaidaPipe implements PipeTransform {

  transform(value: any, ...args: any[]) {
    switch (value) {
      case FinalidadeSaida.projeto:
        return 'Projeto';
      case FinalidadeSaida.aulaPratica:
        return 'Aula Prática';
    }
  }
}
