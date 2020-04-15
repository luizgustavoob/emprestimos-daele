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
    loadChildren: () => import('./fornecedor/fornecedor.module').then(m => m.FornecedorModule)
  },

  {
    path: 'equipamentos',
    loadChildren: () => import('./equipamentos/equipamentos.module').then(m => m.EquipamentosModule)
  },

  {
    path: 'entradas',
    loadChildren: () => import('./entradas/entradas.module').then(m => m.EntradasModule)
  },

  {
    path: 'emprestimos',
    loadChildren: () => import('./saidas/emprestimos/emprestimos.module').then(m => m.EmprestimosModule)
  },

  {
    path: 'baixas-estoque',
    loadChildren: () => import('./saidas/baixas-estoque/baixas-estoque.module').then(m => m.BaixasEstoqueModule)
  },

  {
    path: 'usuarios',
    loadChildren: () => import('./usuarios/usuario.module').then(m => m.UsuarioModule)
  },

  {
    path: 'ficha-de-estoque',
    loadChildren: () => import('./relatorios/ficha-de-estoque/ficha-de-estoque.module').then(m => m.FichaDeEstoqueModule)
  },

  {
    path: 'nao-autorizado',
    loadChildren: () => import('./shared/components/unauthorized/unauthorized.module').then(m => m.UnauthorizedModule)
  },

  {
    path: 'nao-encontrado',
    loadChildren: () => import('./shared/components/not-found/not-found.module').then(m => m.NotFoundModule)
  },

  { path: '', pathMatch: 'full', redirectTo: 'home' },

  { path: '**', redirectTo: 'nao-encontrado' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
