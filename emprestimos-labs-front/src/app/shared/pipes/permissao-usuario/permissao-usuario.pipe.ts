import { Pipe, PipeTransform } from '@angular/core';
import { Permissao } from './../../../usuarios/model/permissao';

@Pipe({
  name: 'permissao'
})
export class PermissaoUsuarioPipe implements PipeTransform {

  transform(value: Permissao, ...args: any[]) {
    switch (value) {
      case Permissao.aluno:
        return 'Aluno';
      case Permissao.laboratorista:
        return 'Laboratorista';
      case Permissao.admin:
        return 'Administrador';
      case Permissao.professor:
        return 'Professor';
    }
  }
}
