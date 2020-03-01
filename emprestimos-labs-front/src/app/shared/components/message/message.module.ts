import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { MessageComponent } from './error/message-error.component';

@NgModule({
  declarations: [ MessageComponent ],
  imports: [ CommonModule, FormsModule ],
  exports: [ MessageComponent ]
})
export class MessageModule { }
