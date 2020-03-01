import { Injectable, Injector, ErrorHandler } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

import { MyMessageService } from './my-message.service';
import { LoadingService } from '../components/loading/loading.service';

@Injectable()
export class ErrorHandlerService implements ErrorHandler {

  constructor(private injector: Injector) { }

  handleError(errorResponse: any): void {
    console.log('Erro', errorResponse);

    const loadingService = this.injector.get(LoadingService);
    const messageService = this.injector.get(MyMessageService);

    loadingService.stop();

    if (typeof errorResponse === 'string') {
      messageService.showMessage('error', errorResponse);
    } else if (errorResponse instanceof HttpErrorResponse) {
      let msgError: string;
      try {
        msgError = errorResponse.error[0].mensagemUsuario;
      } catch (e) {
        msgError = errorResponse.error.message;
      }
      messageService.showMessage('error', msgError);
    }
  }
}
