import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FinalidadeSaidaPipe } from './finalidade-saida.pipe';

@NgModule({
  declarations: [ FinalidadeSaidaPipe ],
  imports: [ CommonModule ],
  exports: [ FinalidadeSaidaPipe ]
})
export class FinalidadeSaidaPipeModule { }
