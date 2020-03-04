import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class MyWebSocket {

  private stompClient;
  private subscriptions: QueueSubscription[] = [];

  async init(queueName = '', callback = (msg: any) => {}) {
    const ws = new SockJS(environment.api_websocket);
    this.stompClient = Stomp.over(ws);

    if (!this.stompClient.connected) {
      if (queueName) {
        await this.stompClient.connect({}, function() {
          const subscription = this.stompClient.subscribe(queueName, callback);
          this.subscriptions.push(new QueueSubscription(queueName, subscription));      
        }.bind(this));
      } else {
        await this.stompClient.connect({}, () => {});
      }
    }
  }

  subscribe(queueName: string, callback: (msg: any) => void) {
    const existe = this.subscriptions.filter(qs => qs.queueName === queueName).length > 0;
    if (existe) {
      return;
    }
    
    const subscription = this.stompClient.subscribe(queueName, callback);
    this.subscriptions.push(new QueueSubscription(queueName, subscription));
  }

  unsubscribe(queueName) {
    const queueSubscription = this.subscriptions.find(qs => qs.queueName === queueName);
    if (!queueSubscription) {
      return;
    }
    queueSubscription.subscription.unsubscribe();
    this.subscriptions = this.subscriptions.filter(qs => qs.queueName !== queueName);
  }

  unSubscribeAll() {
    if (this.subscriptions.length === 0) {
      return;
    }
    
    this.subscriptions.forEach(qs => qs.subscription.unsubscribe());
    this.subscriptions = [];
  }
 
  destroy() {
    this.unSubscribeAll();
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
  }
}

class QueueSubscription {

  constructor(public readonly queueName, public subscription) { }
}