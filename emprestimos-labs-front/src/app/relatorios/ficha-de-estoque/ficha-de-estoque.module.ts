import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { FichaDeEstoqueComponent } from './ficha-de-estoque.component';

import { FichaDeEstoqueRoutingModule } from './ficha-de-estoque.routing.module';
import { MessageModule } from '../../shared/components/message/message.module';

// primeng
import { MultiSelectModule } from 'primeng/multiselect';
import { CalendarModule } from 'primeng/calendar';

@NgModule({
  declarations: [ FichaDeEstoqueComponent ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule.withConfig({warnOnNgModelWithFormControl: 'never'}),
    RouterModule,
    CalendarModule,
    MultiSelectModule,
    HttpClientModule,
    MessageModule,
    FichaDeEstoqueRoutingModule
  ],
  exports: [ FichaDeEstoqueComponent ]
})
export class FichaDeEstoqueModule { }
