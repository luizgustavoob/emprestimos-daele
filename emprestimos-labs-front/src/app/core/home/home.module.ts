import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';

import { MessageModule } from '../../shared/components/message/message.module';
import { FinalidadeSaidaPipeModule } from '../../shared/pipes/finalidade-saida/finalidade-saida-pipe.module';
import { SituacaoSaidaPipeModule } from '../../shared/pipes/situacao-saida/situacao-saida-pipe.module';

import { HomeComponent } from './home.component';
import { HomeAlunoProfessorComponent } from './home-aluno-professor/home-aluno-professor.component';
import { HomeLaboratoristaAdminComponent } from './home-lab-admin/home-lab-admin.component';
import { EmprestimosPendentesComponent } from './home-lab-admin/emprestimos-pendentes/emprestimos-pendentes.component';
import { UsuariosPendentesComponent } from './home-lab-admin/usuarios-pendentes/usuarios-pendentes.component';
import { EstoqueEsgotandoComponent } from './home-lab-admin/estoque-esgotando/estoque-esgotando.component';

import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

@NgModule({
  declarations: [
    HomeComponent,
    HomeAlunoProfessorComponent,
    HomeLaboratoristaAdminComponent,
    EmprestimosPendentesComponent,
    UsuariosPendentesComponent,
    EstoqueEsgotandoComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    HttpClientModule,
    MessageModule,
    CardModule,
    TableModule,
    TooltipModule,
    FinalidadeSaidaPipeModule,
    SituacaoSaidaPipeModule,
    ConfirmDialogModule,
  ],
  providers: [
    ConfirmationService
  ],
  exports: [
    HomeComponent
  ]
})
export class HomeModule { }