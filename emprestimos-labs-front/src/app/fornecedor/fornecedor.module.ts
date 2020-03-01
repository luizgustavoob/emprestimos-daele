import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';

import { FornecedorListaComponent } from './fornecedor-lista/fornecedor-lista.component';
import { FornecedorCadastroComponent } from './fornecedor-cadastro/fornecedor-cadastro.component';

import { FornecedorRoutingModule } from './fornecedor.routing.module';
import { MessageModule } from '../shared/components/message/message.module';
import { CNPJPipeModule } from '../shared/pipes/cnpj/cnpj-pipe.module';

//primeng
import { ConfirmationService } from 'primeng/api';
import { TableModule } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { InputTextModule } from 'primeng/inputtext';
import { InputMaskModule } from 'primeng/inputmask';

@NgModule({
  declarations: [
    FornecedorListaComponent,
    FornecedorCadastroComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule.withConfig({warnOnNgModelWithFormControl: 'never'}),
    HttpClientModule,
    MessageModule,
    TableModule,
    TooltipModule,
    ConfirmDialogModule,
    AutoCompleteModule,
    InputTextModule,
    InputMaskModule,
    CNPJPipeModule,
    FornecedorRoutingModule
  ],
  providers: [
    ConfirmationService
  ],
  exports: [
    FornecedorListaComponent,
    FornecedorCadastroComponent
  ]
})
export class FornecedorModule { }
