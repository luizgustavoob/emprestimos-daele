import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { MessageModule } from '../shared/components/message/message.module';
import { HomeModule } from './home/home.module';

import { MenuComponent } from './header/menu/menu.component';
import { LoginComponent } from './login/login.component';
import { HeaderComponent } from './header/header.component';
import { UserRoleDirective } from '../shared/directives/user-role.directive';

import { ConfirmationService } from 'primeng/api';
import { RadioButtonModule } from 'primeng/radiobutton';

@NgModule({
  declarations: [
    HeaderComponent,
    MenuComponent,
    LoginComponent,
    UserRoleDirective
  ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule.withConfig({warnOnNgModelWithFormControl: 'never'}),
    HttpClientModule,
    MessageModule,
    HomeModule,
    RadioButtonModule
  ],
  providers: [
    ConfirmationService
  ],
  exports: [
    LoginComponent,
    HeaderComponent,
    UserRoleDirective    
  ]  
})
export class CoreModule { }