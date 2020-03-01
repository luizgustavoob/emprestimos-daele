import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { EntradaItem } from '../../model/entrada-item';
import { EquipamentoService } from 'src/app/equipamentos/service/equipamentos.service';
import { Equipamento } from 'src/app/equipamentos/model/equipamento';

@Component({
  selector: 'app-entrada-item',
  templateUrl: './entrada-item.component.html'
})
export class EntradaItemComponent implements OnInit {
  
  formItem: FormGroup;
  equipamentos: Equipamento[];
  @Input() showDialog: boolean;
  @Input() entradaItem: EntradaItem;
  @Output() onSave = new EventEmitter();
  @Output() onHide = new EventEmitter();
  
  constructor(private formBuilder: FormBuilder, private equipamentoService: EquipamentoService) { }
  
  ngOnInit() {
    this.formItem = this.formBuilder.group({
      idEntradaItem: '',
      equipamento: ['', Validators.required],
      quantidade: ['', Validators.required],
      valorUnitario: ['', Validators.required]
    });
  }

  disabledSaveItem(): boolean {
    const item = this.formItem.getRawValue() as EntradaItem;    
    return typeof(item.equipamento) === 'string' 
      || !item.equipamento 
      || !item.valorUnitario 
      || !item.quantidade;  
  }

  loadEquipamentos(event) {
    this.equipamentoService.findByNomeOrPatrimonioOrSiorg(event.query)
      .subscribe( (res) => this.equipamentos = res );
  }

  onShowEvent() {
    this.formItem.reset();
    this.formItem.patchValue(this.entradaItem.idEntradaItem ? this.entradaItem : {});
  }

  onHideEvent() {
    this.onHide.emit({showDialog: false});
  }

  saveItem() {
    this.entradaItem = this.formItem.getRawValue() as EntradaItem;
    this.showDialog = false;
    this.onSave.emit({entradaItem: this.entradaItem});    
  }

}