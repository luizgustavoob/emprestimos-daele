import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UnauthorizedComponent } from './unauthorized.component';
import { environment } from './../../../../environments/environment';

const routes: Routes = [
  {
    path: '',
    component: UnauthorizedComponent,
    data: {
      title: environment.title + ' - Não autorizado'
    }
  }
];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class UnauthorizedRoutingModule { }
