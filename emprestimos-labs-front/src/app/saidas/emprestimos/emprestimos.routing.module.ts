import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EmprestimosListaComponent } from './emprestimos-lista.component';
import { SaidasCadastroComponent } from '../cadastro/saidas-cadastro.component';
import { SaidasCadastroResolver } from '../cadastro/saidas-cadastro.resolver';

import { GenericGuardService } from '../../core/guard/generic-guard.service';
import { environment } from '../../../environments/environment';

const routes: Routes = [
  {
    path: '',
    component: EmprestimosListaComponent,
    canActivate: [ GenericGuardService ],
    data: {
      title: environment.title + ' - Empréstimos',
      roles: [
        'ROLE_LABORATORISTA',
        'ROLE_ADMIN'
      ]
    }
  },
  {
    path: 'novo',
    component: SaidasCadastroComponent,
    canActivate: [ GenericGuardService ],
    resolve: {
      saida: SaidasCadastroResolver
    },
    data: {
      title: environment.title + ' - Novo empréstimo',
    }
  },

  {
    path: ':id',
    component: SaidasCadastroComponent,
    canActivate: [ GenericGuardService ],
    resolve: {
      saida: SaidasCadastroResolver
    },
    data: {
      title: environment.title + ' - Detalhes empréstimo',
    }
  },
];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class EmprestimosRoutingModule { }
