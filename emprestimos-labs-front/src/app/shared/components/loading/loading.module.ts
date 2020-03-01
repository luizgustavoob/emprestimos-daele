import { NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { CommonModule } from '@angular/common';

import { ProgressBarModule } from 'primeng/progressbar';

import { LoadingComponent } from './loading.component';
import { LoadingInterceptor } from './loading.interceptor';
import { LoadingService } from './loading.service';

@NgModule({
  declarations: [
    LoadingComponent
  ],
  exports: [
    LoadingComponent
  ],
  imports: [
    CommonModule,
    ProgressBarModule
  ],
  providers: [
    LoadingService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LoadingInterceptor,
      multi: true
    }
  ]
})
export class LoadingModule { }
