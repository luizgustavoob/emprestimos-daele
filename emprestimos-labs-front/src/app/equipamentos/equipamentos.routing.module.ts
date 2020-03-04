import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EquipamentosListaComponent } from './equipamentos-lista/equipamentos-lista.component';
import { EquipamentosCadastroComponent } from './equipamentos-cadastro/equipamentos-cadastro.component';
import { EquipamentosCadastroResolver } from './equipamentos-cadastro/equipamentos-cadastro.resolver';
import { GenericGuardService } from '../core/guard/generic-guard.service';
import { environment } from './../../environments/environment.prod';

const routes: Routes = [
  {
    path: '',
    component: EquipamentosListaComponent,
    canActivate: [ GenericGuardService ],
    data: {
      title: environment.title + ' - Equipamentos',
      roles: [
        'ROLE_LABORATORISTA',
        'ROLE_ADMIN'
      ]
    }
  },
  {
    path: 'novo',
    component: EquipamentosCadastroComponent,
    canActivate: [ GenericGuardService ],
    resolve: {
      equipamento: EquipamentosCadastroResolver
    },
    data: {
      title: environment.title + ' - Novo equipamento',
      roles: [
        'ROLE_LABORATORISTA',
        'ROLE_ADMIN'
      ]
    }
  },
  {
    path: ':id',
    component: EquipamentosCadastroComponent,
    canActivate: [ GenericGuardService ],
    resolve: {
      equipamento: EquipamentosCadastroResolver
    },
    data: {
      title: environment.title + ' - Detalhes do equipamento',
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
export class EquipamentosRoutingModule { }
