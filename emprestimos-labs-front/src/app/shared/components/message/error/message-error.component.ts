import { Component, Input } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-message-error',
  templateUrl: './message-error.component.html'
})
export class MessageComponent  {

  @Input() error: string;
  @Input() text: string;
  @Input() control: FormControl;

  temErro(): boolean {
    return this.control.dirty && this.control.hasError(this.error);
  }
}
