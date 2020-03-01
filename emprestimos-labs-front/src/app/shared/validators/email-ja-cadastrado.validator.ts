import { Injectable } from '@angular/core';
import { AbstractControl } from '@angular/forms';
import { UsuarioService } from '../../usuarios/service/usuario.service';
import { debounceTime, switchMap, map, first } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class EmailJaCadastradoValidator {

  constructor(private usuarioService: UsuarioService) { }

  verificaEmailJaCadastrado() {
    return (control: AbstractControl) => {
      return control
          .valueChanges
          .pipe(debounceTime(500))
          .pipe(switchMap(email => this.usuarioService.emailExiste(email)))
          .pipe(map(existe => existe ? { existe: true} : null))
          .pipe(first());
    };
  }
}
