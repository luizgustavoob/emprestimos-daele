import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class MyWebSocket {

  private stompClient;
  private subscriptions: any[] = [];

  subscribe(queueName: string, callback: (msg: any) => void) {
    const existe = this.subscriptions.filter(qs => qs.queueName === queueName).length > 0;
    if (existe) {
      return;
    }

    if (!this.stompClient || !this.stompClient.connected) {
      this.stompClient = Stomp.over(new SockJS(environment.api_websocket));
      this.stompClient.connect({}, () => {
        this.applySubscribe(queueName, callback);
      });
    } else {
      this.applySubscribe(queueName, callback);
    }
  }

  private applySubscribe(queueName: string, callback: (msg: any) => void) {
    const subscription = this.stompClient.subscribe(queueName, callback);
    this.subscriptions.push({queueName, subscription});
  }

  unsubscribe(queueName) {
    const queueSubscription = this.subscriptions.find(qs => qs.queueName === queueName);
    if (!queueSubscription) {
      return;
    }

    queueSubscription.subscription.unsubscribe();
    this.subscriptions = this.subscriptions.filter(qs => qs.queueName !== queueName);
  }

  unsubscribeAll() {    
    if (this.subscriptions.length === 0) {
      return;
    }
    
    this.subscriptions.forEach(qs => qs.subscription.unsubscribe());
    this.subscriptions = [];
  }
 
  destroy() {
    this.unsubscribeAll();
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
  }
}