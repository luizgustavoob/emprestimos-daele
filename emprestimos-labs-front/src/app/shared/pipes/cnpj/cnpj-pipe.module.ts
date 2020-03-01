import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CNPJPipe } from './cnpj.pipe';

@NgModule({
  declarations: [ CNPJPipe ],
  imports: [ CommonModule ],
  exports: [ CNPJPipe ]
})
export class CNPJPipeModule { }
