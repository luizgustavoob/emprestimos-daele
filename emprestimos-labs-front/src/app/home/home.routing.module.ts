import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './home.component';
import { GenericGuardService } from '../guard/generic-guard.service';
import { environment } from './../../environments/environment.prod';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    canActivate: [ GenericGuardService ],
    data: {
      title: environment.title + ' - Home'
    }
  }
];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class HomeRoutingModule { }
