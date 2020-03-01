import { Component, OnInit } from '@angular/core';
import { MyWebSocket } from 'src/app/websocket/websocket.service';
import { Queue } from 'src/app/websocket/queue';
import { UsuarioService } from 'src/app/usuarios/service/usuario.service';

@Component({
  selector: 'app-home-lab-admin',
  templateUrl: './home-lab-admin.component.html',
  styleUrls: ['./home-lab-admin.component.css']
})
export class HomeLaboratoristaAdminComponent implements OnInit {
  
  constructor(private webSocket: MyWebSocket, private usuarioService: UsuarioService) {}
  
  ngOnInit() {
    this.webSocket.subscribe(Queue.getQueueUser(this.usuarioService.getEmail()), function(msg) {
      if (msg.body === 'logout') {
        this.usuarioService.logout();
        this.router.navigate(['/login']);
      }
    });
  } 
}
