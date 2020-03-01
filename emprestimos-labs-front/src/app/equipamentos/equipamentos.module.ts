import { ConfirmationService } from 'primeng/api';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { EquipamentosFiltroComponent } from './equipamentos-lista/equipamentos-filtro/equipamentos-filtro.component';
import { EquipamentosTabelaComponent } from './equipamentos-lista/equipamentos-tabela/equipamentos-tabela.component';
import { EquipamentosListaComponent } from './equipamentos-lista/equipamentos-lista.component';
import { EquipamentosCadastroComponent } from './equipamentos-cadastro/equipamentos-cadastro.component';

import { EquipamentosRoutingModule } from './equipamentos.routing.module';
import { MessageModule } from '../shared/components/message/message.module';

// primeng
import { TableModule } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { CheckboxModule } from 'primeng/checkbox';
import { RadioButtonModule } from 'primeng/radiobutton';
import { InputTextModule } from 'primeng/inputtext';

@NgModule({
  declarations: [
    EquipamentosFiltroComponent,
    EquipamentosTabelaComponent,
    EquipamentosListaComponent,
    EquipamentosCadastroComponent
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
    InputTextModule,
    CheckboxModule,
    RadioButtonModule,
    EquipamentosRoutingModule
  ],
  providers: [
    ConfirmationService
  ],
  exports: [
    EquipamentosListaComponent,
    EquipamentosCadastroComponent
  ]
})
export class EquipamentosModule { }
