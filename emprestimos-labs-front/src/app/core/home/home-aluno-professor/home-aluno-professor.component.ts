import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LazyLoadEvent } from 'primeng/api';
import { Saida, SituacaoSaida } from '../../../saidas/model/saida';
import { UsuarioService } from '../../../usuarios/service/usuario.service';
import { SaidaService } from '../../../saidas/service/saida.service';
import { MyWebSocket } from '../../websocket/websocket.service';
import { getBadgeClass } from '../../../shared/situacao-saida-badge-class';
import { Queue } from 'src/app/core/websocket/queue';

@Component({
  selector: 'app-home-aluno-professor',
  templateUrl: './home-aluno-professor.component.html',
})
export class HomeAlunoProfessorComponent implements OnInit {

  meusEmprestimos: Saida[] = [];
  totalRecords = 0;
  private maxRecords = 10;
  private currentPage = 0;

  constructor(private saidaService: SaidaService,
              private router: Router,
              private webSocket: MyWebSocket,
              private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.getEmprestimosDoUsuario(this.currentPage, this.maxRecords);
    
    this.webSocket.subscribe(Queue.getQueueUser(this.usuarioService.getEmail()), function(msg) {
      const isHome = this.router.url.trim().toLowerCase().search('home') > 0;
      if (msg.body === 'emprestimo' && isHome) {        
        this.getEmprestimosDoUsuario(this.currentPage, this.maxRecords);
      } else if (msg.body === 'logout') {
        this.usuarioService.logout();
        this.router.navigate(['/login']);
      }
    }.bind(this));
  }

  private getEmprestimosDoUsuario(page: number, size: number) {
    this.saidaService.getEmprestimosDoUsuario(page, size).subscribe(
      (resp) => {
        this.meusEmprestimos = resp.content;
        this.maxRecords = resp.totalElements;
      }
    );
  }

  loadLazy(event: LazyLoadEvent) {
    this.currentPage = event.first / event.rows;
    this.maxRecords = event.rows;
    setTimeout(() => this.getEmprestimosDoUsuario(this.currentPage, this.maxRecords), 200);
  }

  exibirEmprestimo(idEmprestimo: number) {
    this.router.navigate(['emprestimos', idEmprestimo]);
  }

  getBadgeClass(situacao: SituacaoSaida): string {
    return getBadgeClass(situacao);
  }
}
