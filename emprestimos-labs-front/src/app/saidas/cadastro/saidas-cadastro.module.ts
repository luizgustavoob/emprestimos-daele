import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { CalendarModule } from 'primeng/calendar';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { DropdownModule } from 'primeng/dropdown';
import { DialogModule } from 'primeng/dialog';
import { TableModule } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { InputTextModule } from 'primeng/inputtext';

import { SaidasCadastroComponent } from './saidas-cadastro.component';
import { MessageModule } from 'src/app/shared/components/message/message.module';

@NgModule({
  declarations: [ SaidasCadastroComponent ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule.withConfig({warnOnNgModelWithFormControl: 'never'}),
    FormsModule,
    HttpClientModule,
    CalendarModule,
    AutoCompleteModule,
    TableModule,
    TooltipModule,
    InputTextModule,
    MessageModule,
    DropdownModule,
    DialogModule,
  ],
  exports: [ SaidasCadastroComponent ]
})
export class SaidaCadastroModule { }
