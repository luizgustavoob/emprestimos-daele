import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { FichaDeEstoqueComponent } from './ficha-de-estoque.component';
import { GenericGuardService } from '../../guard/generic-guard.service';
import { environment } from '../../../environments/environment.prod';

const routes: Routes = [
  {
    path: '',
    component: FichaDeEstoqueComponent,
    canActivate: [ GenericGuardService ],
    data: {
      title: environment.title + ' - Ficha de Estoque',
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
export class FichaDeEstoqueRoutingModule { }
