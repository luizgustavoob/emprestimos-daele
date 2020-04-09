import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { SignUpComponent } from './signup.component';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MessageModule } from 'src/app/shared/components/message/message.module';
import { RadioButtonModule } from 'primeng/radiobutton';

@NgModule({
  declarations: [
    SignUpComponent
  ], 
  imports: [
    CommonModule,
    ReactiveFormsModule.withConfig({warnOnNgModelWithFormControl: 'never'}),
    FormsModule,
    HttpClientModule,
    NgbModule,
    MessageModule,
    RadioButtonModule
  ],
  exports: [
    SignUpComponent
  ]
})
export class SignUpModule { }