import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { UsuariosComponent } from './usuarios.component';
import { AtivoPipe } from '../shared/pipes/ativo/ativo.pipe';
import { PermissaoUsuarioPipe } from '../shared/pipes/permissao-usuario/permissao-usuario.pipe';

import { UsuariosRoutingModule } from './usuarios.routing.module';
import { MessageModule } from '../shared/components/message/message.module';

// primeng
import { AutoCompleteModule } from 'primeng/autocomplete';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { RadioButtonModule } from 'primeng/radiobutton';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { TooltipModule } from 'primeng/tooltip';
import { ConfirmationService } from 'primeng/api';

@NgModule({
  declarations: [
    UsuariosComponent,
    AtivoPipe,
    PermissaoUsuarioPipe
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule.withConfig({warnOnNgModelWithFormControl: 'never'}),
    FormsModule,
    RouterModule,
    HttpClientModule,
    MessageModule,
    AutoCompleteModule,
    TableModule,
    TooltipModule,
    DialogModule,
    InputTextModule,
    RadioButtonModule,
    ConfirmDialogModule,
    UsuariosRoutingModule
  ],
  providers: [
    ConfirmationService
  ],
  exports: [ UsuariosComponent ]
})
export class UsuarioModule { }
