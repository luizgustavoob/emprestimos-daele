import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { MessageModule } from '../shared/components/message/message.module';
import { HomeModule } from './home/home.module';
import { SignUpModule } from './sign-up/signup.module';

import { MenuComponent } from './menu/menu.component';
import { LoginComponent } from './login/login.component';
import { HeaderComponent } from './header/header.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { ForgotPasswordComponent } from './forgot-password/forgotpassword.component';
import { UserRoleDirective } from '../shared/directives/user-role.directive';

import { ConfirmationService } from 'primeng/api';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { SignUpComponent } from './sign-up/signup.component';

@NgModule({
  declarations: [
    HeaderComponent,
    MenuComponent,
    LoginComponent,
    ChangePasswordComponent,
    ForgotPasswordComponent,
    UserRoleDirective
  ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule.withConfig({warnOnNgModelWithFormControl: 'never'}),
    HttpClientModule,
    MessageModule,
    HomeModule,
    NgbModule,
    SignUpModule 
  ],
  providers: [
    ConfirmationService
  ],
  entryComponents: [    
    ChangePasswordComponent,
    ForgotPasswordComponent,
    SignUpComponent  
  ],
  exports: [
    LoginComponent,
    HeaderComponent,
    UserRoleDirective
  ]  
})
export class CoreModule { }