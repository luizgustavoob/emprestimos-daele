import { PipeTransform, Pipe } from '@angular/core';

@Pipe({
  name: 'ativo'
})
export class AtivoPipe implements PipeTransform {

  transform(value: boolean, ...args: any[]) {
    return value ? 'Ativo' : 'Inativo';
  }
}
