import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { EmprestimosListaComponent } from './emprestimos-lista.component';
import { EmprestimosFiltroComponent } from './emprestimos-filtro/emprestimos-filtro.component';
import { EmprestimosTabelaComponent } from './emprestimos-tabela/emprestimos-tabela.component';

import { SaidaCadastroModule } from './../cadastro/saidas-cadastro.module';
import { SituacaoSaidaPipeModule } from './../../shared/pipes/situacao-saida/situacao-saida-pipe.module';
import { FinalidadeSaidaPipeModule } from './../../shared/pipes/finalidade-saida/finalidade-saida-pipe.module';

// primeng
import { CalendarModule } from 'primeng/calendar';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { MultiSelectModule } from 'primeng/multiselect';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { TooltipModule } from 'primeng/tooltip';
import { TableModule } from 'primeng/table';

import { EmprestimosRoutingModule } from './emprestimos.routing.module';
import { ConfirmationService } from 'primeng/api';

@NgModule({
  declarations: [ 
    EmprestimosListaComponent,
    EmprestimosFiltroComponent,
    EmprestimosTabelaComponent
   ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule.withConfig({warnOnNgModelWithFormControl: 'never'}),
    FormsModule,
    HttpClientModule,
    SaidaCadastroModule,
    CalendarModule,
    AutoCompleteModule,
    MultiSelectModule,
    TableModule,
    TooltipModule,
    ConfirmDialogModule,
    FinalidadeSaidaPipeModule,
    SituacaoSaidaPipeModule,
    EmprestimosRoutingModule
  ],
  providers: [
    ConfirmationService
  ],
  exports: [ EmprestimosListaComponent ]
})
export class EmprestimosModule { }
