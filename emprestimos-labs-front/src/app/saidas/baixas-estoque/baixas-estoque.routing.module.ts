import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BaixasEstoqueListaComponent } from './baixas-estoque-lista.component';
import { SaidasCadastroComponent } from '../cadastro/saidas-cadastro.component';
import { SaidasCadastroResolver } from '../cadastro/saidas-cadastro.resolver';
import { GenericGuardService } from '../../core/guard/generic-guard.service';
import { environment } from './../../../environments/environment';

const routes: Routes = [
  {
    path: '',
    component: BaixasEstoqueListaComponent,
    canActivate: [ GenericGuardService ],
    data: {
      title: environment.title + ' - Baixas de Estoque',
      roles: [
        'ROLE_LABORATORISTA',
        'ROLE_ADMIN'
      ]
    }
  },

  {
    path: 'nova',
    component: SaidasCadastroComponent,
    canActivate: [ GenericGuardService ],
    resolve: {
      saida: SaidasCadastroResolver
    },
    data: {
      title: environment.title + ' - Nova baixa de estoque',
      roles: [
        'ROLE_LABORATORISTA',
        'ROLE_ADMIN'
      ]
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
      title: environment.title + ' - Detalhes da baixa de estoque',
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
export class BaixasEstoqueRoutingModule { }
