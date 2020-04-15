import { Injectable, NgZone } from '@angular/core';
import { MessageService } from 'primeng/api';
@Injectable({
  providedIn: 'root'
})
export class MyMessageService {

  constructor(private zone: NgZone,
              private messageService: MessageService) { }

  showMessage(severity: string, detail: string) {
    this.zone.run(() => {
      this.messageService.clear();
      this.messageService.add({severity, summary: 'Empréstimos DAELE', detail, life: 2000});
    });
  }
}
