import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { UsuarioService } from '../service/usuario.service';
import { Usuario } from '../model/usuario';
import { UsuarioFiltro } from '../model/usuario-filtro';

@Component({
  selector: 'app-usuarios-filtro',
  templateUrl: './usuarios-filtro.component.html'
})
export class UsuariosFiltroComponent implements OnInit {
  
  formFiltro: FormGroup;
  usuariosList: Usuario[] = [];
  @Output() onPesquisarUsuarios = new EventEmitter();
  @Output() onCadastrarUsuario = new EventEmitter();

  constructor(private formBuilder: FormBuilder, private usuarioService: UsuarioService) { }
  
  ngOnInit() {
    this.formFiltro = this.formBuilder.group({usuario: ''});
  }

  loadUsuarios(event) {
    this.usuarioService.findByNroraOrEmail(event.query).subscribe(res => this.usuariosList = res);
  }

  devePesquisarUsuarios() {     
    const usuarioFiltro = new UsuarioFiltro();
    usuarioFiltro.email = this.formFiltro.get('usuario').value.email;
    this.onPesquisarUsuarios.emit({usuarioFiltro});
  }

  deveCadastrarUsuario() {
    this.onCadastrarUsuario.emit({novoUsuario: true});
  }
}