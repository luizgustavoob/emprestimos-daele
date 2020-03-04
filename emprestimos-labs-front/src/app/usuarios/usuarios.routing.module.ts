import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UsuariosComponent } from './usuarios.component';
import { GenericGuardService } from '../core/guard/generic-guard.service';
import { environment } from './../../environments/environment.prod';

const routes: Routes = [
  {
    path: '',
    component: UsuariosComponent,
    canActivate: [ GenericGuardService ],
    data: {
      title: environment.title + ' - Usuários',
      roles: [
        'ROLE_LABORATORISTA',
        'ROLE_ADMIN'
      ]
    }
  }
];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class UsuariosRoutingModule { }
