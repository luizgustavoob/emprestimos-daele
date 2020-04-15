import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { MessageComponent } from './error/message-error.component';
import { MessageService } from 'primeng/api';

@NgModule({
  declarations: [ MessageComponent ],
  imports: [ CommonModule, FormsModule ],
  exports: [ MessageComponent ],
  providers: [ MessageService ]
})
export class MessageModule { }
