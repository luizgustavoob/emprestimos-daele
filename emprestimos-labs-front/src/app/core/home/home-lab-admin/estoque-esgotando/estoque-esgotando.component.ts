import { Component, OnInit, OnDestroy } from '@angular/core';
import { Observable } from 'rxjs';
import { MyWebSocket } from 'src/app/core/websocket/websocket.service';
import { EquipamentoService } from 'src/app/equipamentos/service/equipamentos.service';
import { EquipamentoDto } from 'src/app/equipamentos/model/equipamento-dto';
import { QueueUtils } from 'src/app/core/websocket/queue-util';

@Component({
  selector: 'app-estoque-esgotando',
  templateUrl: './estoque-esgotando.component.html'
})
export class EstoqueEsgotandoComponent implements OnInit, OnDestroy {
  
  equipamentos$: Observable<EquipamentoDto[]>;
  
  constructor(private webSocket: MyWebSocket, private equipamentoService: EquipamentoService) {}
  
  ngOnInit() {
    this.equipamentos$ = this.getEquipamentosComEstoqueEsgotando();
    
    this.webSocket.subscribe(QueueUtils.ATUALIZAR_ESTOQUE, 
      (msg) => {        
        if (msg.body === 'update') {
          this.equipamentos$ = this.getEquipamentosComEstoqueEsgotando();
        }        
      });
  }

  private getEquipamentosComEstoqueEsgotando() {
    return this.equipamentoService.findEquipamentosComEstoqueEsgotando();
  }  

  ngOnDestroy() {
    this.webSocket.unsubscribe(QueueUtils.ATUALIZAR_ESTOQUE);
  }
}