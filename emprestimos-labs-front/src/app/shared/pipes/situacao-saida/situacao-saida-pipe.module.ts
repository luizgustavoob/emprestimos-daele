import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SituacaoSaidaPipe } from './situacao-saida.pipe';

@NgModule({
  declarations: [ SituacaoSaidaPipe ],
  imports: [ CommonModule ],
  exports: [ SituacaoSaidaPipe ]
})
export class SituacaoSaidaPipeModule { }
