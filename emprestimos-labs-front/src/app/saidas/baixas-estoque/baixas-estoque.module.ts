import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { TableModule } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

import { BaixasEstoqueListaComponent } from './baixas-estoque-lista.component';

import { BaixasEstoqueRoutingModule } from './baixas-estoque.routing.module';
import { SaidaCadastroModule } from '../cadastro/saidas-cadastro.module';
import { SituacaoSaidaPipeModule } from './../../shared/pipes/situacao-saida/situacao-saida-pipe.module';
import { FinalidadeSaidaPipeModule } from './../../shared/pipes/finalidade-saida/finalidade-saida-pipe.module';

@NgModule({
  declarations: [ BaixasEstoqueListaComponent ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule.withConfig({warnOnNgModelWithFormControl: 'never'}),
    FormsModule,
    HttpClientModule,
    SaidaCadastroModule,
    TableModule,
    TooltipModule,
    ConfirmDialogModule,
    FinalidadeSaidaPipeModule,
    SituacaoSaidaPipeModule,
    BaixasEstoqueRoutingModule
  ],
  exports: [ BaixasEstoqueListaComponent ],
  providers: [
    ConfirmationService
  ]
})
export class BaixasEstoqueModule { }
