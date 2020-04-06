import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { FornecedorListaComponent } from './fornecedor-lista/fornecedor-lista.component';
import { FornecedorCadastroComponent } from './fornecedor-cadastro/fornecedor-cadastro.component';
import { FornecedorCadastroResolver } from './fornecedor-cadastro/fornecedor-cadastro.resolver';
import { GenericGuardService } from '../core/guard/generic-guard.service';
import { environment } from './../../environments/environment';

const routes: Routes = [
  {
    path: '',
    component: FornecedorListaComponent,
    canActivate: [ GenericGuardService ],
    data: {
      title: environment.title + ' - Fornecedores',
      roles: [
        'ROLE_LABORATORISTA',
        'ROLE_ADMIN'
      ]
    }
  },

  {
    path: 'novo',
    component: FornecedorCadastroComponent,
    canActivate: [ GenericGuardService ],
    resolve: {
      fornecedor: FornecedorCadastroResolver
    },
    data: {
      title: environment.title + ' - Novo fornecedor',
      roles: [
        'ROLE_LABORATORISTA',
        'ROLE_ADMIN'
      ]
    }
  },

  {
    path: ':id',
    component: FornecedorCadastroComponent,
    canActivate: [ GenericGuardService ],
    resolve: {
      fornecedor: FornecedorCadastroResolver
    },
    data: {
      title: environment.title + ' - Detalhes do fornecedor',
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
export class FornecedorRoutingModule { }
