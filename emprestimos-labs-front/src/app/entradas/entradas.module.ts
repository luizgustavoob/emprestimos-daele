import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { EntradasFiltroComponent } from './entradas-lista/filtro/entradas-filtro.component';
import { EntradasTabelaComponent } from './entradas-lista/tabela/entradas-tabela.component';
import { EntradasListaComponent } from './entradas-lista/entradas-lista.component';
import { EntradasCadastroComponent } from './entradas-cadastro/entradas-cadastro.component';
import { EntradaItemComponent } from './entradas-cadastro/entrada-item/entrada-item.component';

import { EntradasRoutingModule } from './entradas.routing.module';
import { MessageModule } from '../shared/components/message/message.module';
import { CNPJPipeModule } from '../shared/pipes/cnpj/cnpj-pipe.module';

import { CurrencyMaskModule } from 'ng2-currency-mask';
import { CurrencyMaskConfig, CURRENCY_MASK_CONFIG } from 'ng2-currency-mask/src/currency-mask.config';

// primeng
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { TableModule } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { CalendarModule } from 'primeng/calendar';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { ConfirmationService, DialogService } from 'primeng/api';

export const CustomCurrencyMaskConfig: CurrencyMaskConfig = {
  align: 'right',
  allowNegative: false,
  decimal: '.',
  precision: 2,
  prefix: 'R$ ',
  suffix: '',
  thousands: ','
};

@NgModule({
  declarations: [
    EntradasFiltroComponent,
    EntradasTabelaComponent,
    EntradasListaComponent,
    EntradasCadastroComponent,
    EntradaItemComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule.withConfig({warnOnNgModelWithFormControl: 'never'}),
    FormsModule,
    HttpClientModule,
    CalendarModule,
    AutoCompleteModule,
    TooltipModule,
    TableModule,
    ConfirmDialogModule,
    MessageModule,
    DialogModule,
    InputTextModule,
    CNPJPipeModule,
    CurrencyMaskModule,
    EntradasRoutingModule
  ],
  providers: [
    ConfirmationService,
    DialogService,
    {
      provide: CURRENCY_MASK_CONFIG,
      useValue: CustomCurrencyMaskConfig
    }
  ],
  exports: [
    EntradasListaComponent,
    EntradasCadastroComponent
  ]
})
export class EntradasModule { }
