import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { EntradasListaComponent } from './entradas-lista/entradas-lista.component';
import { EntradasCadastroComponent } from './entradas-cadastro/entradas-cadastro.component';
import { EntradasCadastroResolver } from './entradas-cadastro/entradas-cadastro.resolver';
import { GenericGuardService } from './../guard/generic-guard.service';
import { environment } from './../../environments/environment.prod';

const routes: Routes = [
  {
    path: '',
    component: EntradasListaComponent,
    canActivate: [ GenericGuardService ],
    data: {
      title: environment.title + ' - Entradas',
      roles: [
        'ROLE_LABORATORISTA',
        'ROLE_ADMIN'
      ]
    }
  },

  {
    path: 'nova',
    component: EntradasCadastroComponent,
    resolve: {
      entrada: EntradasCadastroResolver
    },
    data: {
      title: environment.title + ' - Nova entrada',
      roles: [
        'ROLE_LABORATORISTA',
        'ROLE_ADMIN'
      ]
    }
  },

  {
    path: ':id',
    component: EntradasCadastroComponent,
    resolve: {
      entrada: EntradasCadastroResolver
    },
    data: {
      title: environment.title + ' - Detalhes da entrada',
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
export class EntradasRoutingModule { }
