import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { CrudService } from '../../shared/services/crud.service';
import { Usuario } from '../model/usuario';
import { UsuarioDto } from '../model/usuario-dto';
import { UsuarioFiltro } from '../model/usuario-filtro';
import { UserLogged } from '../model/user-logged';
import { Page } from '../../shared/page';
import { TokenService } from '../../core/token/token.service';
import { environment } from '../../../environments/environment';
import { MyMessageService } from './../../shared/services/my-message.service';
import { Permissao } from '../model/permissao';
import { MyWebSocket } from '../../core/websocket/websocket.service';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService extends CrudService<Usuario, number> {

  private userLoggedSubject = new BehaviorSubject<String>(null); // armazena a emissão até que algum consumidor apareça

  constructor(http: HttpClient,
              private tokenService: TokenService,
              private messageService: MyMessageService,
              private webSocket: MyWebSocket) {
    super(environment.api_usuarios, http);
    if (this.tokenService.hasToken()) {
      this.decodeAndNotify();
    }
  }

  private decodeAndNotify() {    
    const userName = this.getUserName();
    this.userLoggedSubject.next(userName);
  }

  private getUserName(): string {
    if (!this.tokenService.getPayload()) {
      return;
    }
    return this.tokenService.getPayload().userName;
  }

  getEmail(): string {
    if (!this.tokenService.getPayload()) {
      return;
    }
    return this.tokenService.getPayload().sub;
  }

  private getAuthority(): string {
    if (!this.tokenService.getPayload()) {
      return;
    }
    return this.tokenService.getPayload().authorities[0]; // hoje, um usuário tem apenas uma permissão
  }

  getUser() {
    return this.userLoggedSubject.asObservable();
  }

  setToken(token: string) {
    this.tokenService.setToken(token);
    this.decodeAndNotify();
  }

  isLogged(): boolean {
    return this.tokenService.hasToken() && !this.tokenService.isTokenExpired();
  }

  logout() {
    this.tokenService.removeToken();
    this.userLoggedSubject.next(null);
    this.webSocket.unSubscribeAll();
  }

  removeToken() {
    this.tokenService.removeToken();
  }

  isAluno(): boolean {
    if (!this.getAuthority()) {
      return false;
    }

    return this.getAuthority().indexOf('ROLE_ALUNO') !== -1;
  }

  isLaboratorista(): boolean {
    if (!this.getAuthority()) {
      return false;
    }

    return this.getAuthority().indexOf('ROLE_LABORATORISTA') !== -1;
  }

  isAdmin(): boolean {
    if (!this.getAuthority()) {
      return false;
    }

    return this.getAuthority().indexOf('ROLE_ADMIN') !== -1;
  }

  isProfessor(): boolean {
    if (!this.getAuthority()) {
      return false;
    }

    return this.getAuthority().indexOf('ROLE_PROFESSOR') !== -1;
  }

  hasAnyRole(roles: string[]): boolean {
    let hasrole = false;
    for (const role of roles) {
      hasrole = this.hasRole(role);
      if (hasrole) {
        break;
      }
    }
    return hasrole;
  }

  hasRole(role: string): boolean {
    return this.getAuthority().indexOf(role) !== -1;
  }

  changePassword(senhaAtual: string, novaSenha: string) {
    const email = this.getEmail();
    const url = `${this.getUrl()}/change-password`;
    return this.http.post(url, { email, senhaAtual, novaSenha });
  }

  forgotPassword(email: string) {
    const url = `${this.getUrl()}/forgot-password`;
    return this.http.post(url, { email });
  }

  getUsuariosPendentes(): Observable<Usuario[]> {
    const url = `${this.getUrl()}/usuarios-pendentes`;
    return this.http.get<Usuario[]>(url);
  }

  updateStatus(idUsuario: number, ativo: boolean) {
    const url = `${this.getUrl()}/${idUsuario}/ativar`;
    return this.http.patch(url, ativo);
  }

  findByNroraOrEmail(nroRAOrEmail: string): Observable<Usuario[]> {
    const url = `${this.getUrl()}/search?chavePesquisa=${nroRAOrEmail}`;
    return this.http.get<Usuario[]>(url);
  }

  findByFiltros(usuarioFiltro: UsuarioFiltro): Observable<Page<UsuarioDto>> {
    const url = `${this.getUrl()}/filter?`;
    let params = new HttpParams();
    params = params.set('page', usuarioFiltro.page.toString());
    params = params.set('size', usuarioFiltro.size.toString());

    if (usuarioFiltro.email) {
      params = params.set('email', usuarioFiltro.email);
    }

    return this.http.get<Page<UsuarioDto>>(url, {params});
  }

  emailExiste(email: string): Observable<boolean> {
    const url = `${this.getUrl()}/email/${email}`;
    return this.http.get<boolean>(url);
  }
}
