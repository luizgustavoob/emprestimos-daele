import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { filter, map, switchMap } from 'rxjs/operators';
import { UsuarioService } from './usuarios/service/usuario.service';
import { MyWebSocket } from './core/websocket/websocket.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {  

  constructor(private router: Router,
              private activatedRoute: ActivatedRoute,
              private titleService: Title,
              private usuarioService: UsuarioService,
              private webSocket: MyWebSocket) { }

  ngOnInit() {    
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .pipe(map( () => this.activatedRoute))
      .pipe(map( (route) => {
          while (route.firstChild) {
            route = route.firstChild;
          }
          return route;
        }))
      .pipe(switchMap((event) => event.data))
      .subscribe( (event) => this.titleService.setTitle(event.title) );
  }

  logado(): boolean {
    return this.usuarioService.isLogged();
  }

  ngOnDestroy() {
    this.webSocket.destroy();
  }
}
