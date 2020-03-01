import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

import { HomeComponent } from './home.component';
import { HomeAlunoProfessorComponent } from './home-aluno-professor/home-aluno-professor.component';
import { HomeLaboratoristaAdminComponent } from './home-lab-admin/home-lab-admin.component';
import { UsuariosPendentesComponent } from './home-lab-admin/usuarios-pendentes/usuarios-pendentes.component';
import { EmprestimosPendentesComponent } from './home-lab-admin/emprestimos-pendentes/emprestimos-pendentes.component';
import { EstoqueEsgotandoComponent } from './home-lab-admin/estoque-esgotando/estoque-esgotando.component';

import { HomeRoutingModule } from './home.routing.module';
import { FinalidadeSaidaPipeModule } from './../shared/pipes/finalidade-saida/finalidade-saida-pipe.module';
import { SituacaoSaidaPipeModule } from './../shared/pipes/situacao-saida/situacao-saida-pipe.module';

// primeng
import { TableModule } from 'primeng/table';
import { CardModule } from 'primeng/card';
import { TooltipModule } from 'primeng/tooltip';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

@NgModule({
  declarations: [
    HomeComponent,
    HomeAlunoProfessorComponent,
    HomeLaboratoristaAdminComponent,
    UsuariosPendentesComponent,
    EmprestimosPendentesComponent,
    EstoqueEsgotandoComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    HttpClientModule,
    CardModule,
    TableModule,
    TooltipModule,
    FinalidadeSaidaPipeModule,
    SituacaoSaidaPipeModule,
    ConfirmDialogModule,
    HomeRoutingModule
  ],
  providers: [
    ConfirmationService
  ],
  exports: [
    HomeComponent,
    HomeAlunoProfessorComponent,
    HomeLaboratoristaAdminComponent
  ]
})
export class HomeModule { }
