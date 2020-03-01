import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';

import { MessageModule } from '../shared/components/message/message.module';

import { HeaderComponent } from './header.component';
import { MenuComponent } from './menu/menu.component';
import { UserRoleDirective } from '../shared/directives/user-role.directive';

@NgModule({
  declarations: [
    HeaderComponent,
    MenuComponent,
    UserRoleDirective
  ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule.withConfig({warnOnNgModelWithFormControl: 'never'}),
    HttpClientModule,
    MessageModule
  ],
  exports: [
    HeaderComponent,
    UserRoleDirective
  ]
})
export class HeaderModule { }
