import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { UnauthorizedRoutingModule } from './unauthorized.routing.module';
import { UnauthorizedComponent } from './unauthorized.component';

@NgModule({
  declarations: [ UnauthorizedComponent ],
  imports: [
    CommonModule,
    RouterModule,
    UnauthorizedRoutingModule
  ],
  exports: [ UnauthorizedComponent ]
})
export class UnauthorizedModule { }
