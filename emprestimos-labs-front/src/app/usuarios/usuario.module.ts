import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { UsuariosFiltroComponent } from './usuarios-filtro/usuarios-filtro.component';
import { UsuariosTabelaComponent } from './usuarios-tabela/usuarios-tabela.component';
import { UsuariosComponent } from './usuarios.component';
import { AtivoPipe } from '../shared/pipes/ativo/ativo.pipe';
import { PermissaoUsuarioPipe } from '../shared/pipes/permissao-usuario/permissao-usuario.pipe';

import { UsuariosRoutingModule } from './usuarios.routing.module';
import { MessageModule } from '../shared/components/message/message.module';
import { SignUpModule } from '../core/sign-up/signup.module';

// primeng
import { AutoCompleteModule } from 'primeng/autocomplete';
import { TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { TooltipModule } from 'primeng/tooltip';
import { ConfirmationService } from 'primeng/api';
import { SignUpComponent } from '../core/sign-up/signup.component';

@NgModule({
  declarations: [
    UsuariosFiltroComponent,
    UsuariosTabelaComponent,
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
    InputTextModule,
    ConfirmDialogModule,
    UsuariosRoutingModule,
    SignUpModule
  ],
  providers: [
    ConfirmationService
  ],
  exports: [ UsuariosComponent ]
})
export class UsuarioModule { }
