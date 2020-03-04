import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './core/login/login.component';
import { HomeComponent } from './core/home/home.component';
import { GenericGuardService } from './core/guard/generic-guard.service';
import { environment } from '../environments/environment';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
    data: {
      title: environment.title + ' - Login'
    }
  },

  {
    path: 'home',    
    component: HomeComponent,
    canActivate: [ GenericGuardService ],
    data: {
      title: environment.title + ' - Home'
    }
  },

  {
    path: 'fornecedores',
    loadChildren: './fornecedor/fornecedor.module#FornecedorModule'
  },

  {
    path: 'equipamentos',
    loadChildren: './equipamentos/equipamentos.module#EquipamentosModule'
  },

  {
    path: 'entradas',
    loadChildren: './entradas/entradas.module#EntradasModule'
  },

  {
    path: 'emprestimos',
    loadChildren: './saidas/emprestimos/emprestimos.module#EmprestimosModule'
  },

  {
    path: 'baixas-estoque',
    loadChildren: './saidas/baixas-estoque/baixas-estoque.module#BaixasEstoqueModule'
  },

  {
    path: 'usuarios',
    loadChildren: './usuarios/usuario.module#UsuarioModule'
  },

  {
    path: 'ficha-de-estoque',
    loadChildren: './relatorios/ficha-de-estoque/ficha-de-estoque.module#FichaDeEstoqueModule'
  },

  {
    path: 'nao-autorizado',
    loadChildren: './shared/components/unauthorized/unauthorized.module#UnauthorizedModule'
  },

  {
    path: 'nao-encontrado',
    loadChildren: './shared/components/not-found/not-found.module#NotFoundModule'
  },

  { path: '', pathMatch: 'full', redirectTo: 'home' },

  { path: '**', redirectTo: 'nao-encontrado' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
