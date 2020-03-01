import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotFoundComponent } from './not-found.component';
import { environment } from './../../../../environments/environment';

const routes: Routes = [
  {
    path: '',
    component: NotFoundComponent,
    data: {
      title: environment.title + ' - Not found'
    }
  }
];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class NotFoundRoutingModule { }
