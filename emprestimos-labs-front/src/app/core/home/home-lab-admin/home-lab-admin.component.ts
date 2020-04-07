import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MyWebSocket } from 'src/app/core/websocket/websocket.service';
import { QueueUtils } from 'src/app/core/websocket/queue-util';
import { UsuarioService } from 'src/app/usuarios/service/usuario.service';

@Component({
  selector: 'app-home-lab-admin',
  templateUrl: './home-lab-admin.component.html',
  styleUrls: ['./home-lab-admin.component.css']
})
export class HomeLaboratoristaAdminComponent implements OnInit {
  
  constructor(private router: Router, 
              private webSocket: MyWebSocket,
              private usuarioService: UsuarioService) { }  
  
  ngOnInit() {
    this.webSocket.subscribe(QueueUtils.getQueueUser(this.usuarioService.getEmail()), 
      (msg) => {
        if (msg.body === 'logout') {
          this.usuarioService.logout();
          this.router.navigate(['/login']);
        }
      });
  } 
}
