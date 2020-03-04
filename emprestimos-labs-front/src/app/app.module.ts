import { NgModule, ErrorHandler } from '@angular/core';
import { CommonModule, registerLocaleData } from '@angular/common';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import localePt from '@angular/common/locales/pt';

import { AppComponent } from './app.component';

import { AppRoutingModule } from './app.routing.module';
import { CoreModule } from './core/core.module';
import { LoadingModule } from './shared/components/loading/loading.module';
import { ToastModule } from 'primeng/toast';

import { AuthInterceptorService } from './core/auth/auth.interceptor';
import { ErrorHandlerService } from './shared/services/error-handler.service';

import { MessageService } from 'primeng/components/common/messageservice';

registerLocaleData(localePt);

@NgModule({
  declarations: [ AppComponent ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    CommonModule,
    ToastModule,
    CoreModule,
    LoadingModule,
    AppRoutingModule
  ],
  providers: [
    MessageService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
    },
    {
      provide: ErrorHandler,
      useClass: ErrorHandlerService
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
