import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Table } from 'primeng/table';
import { EntradaService } from '../service/entrada.service';
import { Entrada } from '../model/entrada';
import { EntradaItem } from '../model/entrada-item';
import { FornecedorService } from '../../fornecedor/service/fornecedor.service';
import { Fornecedor } from '../../fornecedor/model/fornecedor';
import { MyMessageService } from '../../shared/services/my-message.service';
import { CalendarBase } from './../../shared/calendar.base';

@Component({
  selector: 'app-entradas-cadastro',
  templateUrl: './entradas-cadastro.component.html'
})
export class EntradasCadastroComponent extends CalendarBase implements OnInit {

  @ViewChild('tableItens') tableItens: Table;
  formEntrada: FormGroup;
  itens: EntradaItem[] = [];
  fornecedores: Fornecedor[];
  showDialog: boolean = false;
  entradaItem: EntradaItem = new EntradaItem();

  constructor(private formBuilder: FormBuilder,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private entradaService: EntradaService,
              private fornecedorService: FornecedorService,
              private messageService: MyMessageService) {
    super();
  }

  ngOnInit() {
    this.formEntrada = this.formBuilder.group({
      idEntrada: [],
      data: ['', Validators.required],
      fornecedor: ['', Validators.required]
    });

    this.activatedRoute.params.subscribe(
      () => this.carregarEntrada(this.activatedRoute.snapshot.data.entrada)
    );
  }

  addItem() {
    this.entradaItem = Object.assign({});
    this.showDialog = true;
  }
 
  private carregarEntrada(entrada: Entrada) {
    this.formEntrada.patchValue(entrada);
    this.itens = entrada.itens;  
  }

  disabledSave(): boolean {
    return (this.formEntrada.invalid || this.itens.length === 0);
  }

  editItem(idEntradaItem: number) {
    this.entradaItem = this.itens.find(itemEntrada => itemEntrada.idEntradaItem === idEntradaItem);
    this.showDialog = true;
  }

  loadFornecedores(event) {
    this.fornecedorService.findByRazaoSocialOuCNPJ(event.query)
      .subscribe( (res) => this.fornecedores = res );
  }

  onHideItem(event) {
    this.showDialog = event.showDialog;
  }

  removeItem(idEntradaItem: number) {
    this.itens = this.itens.filter(item => item.idEntradaItem !== idEntradaItem);
    this.tableItens.reset();
  }

  saveItem(event) {
    this.showDialog = false;
    const item = event.entradaItem;

    if (!item.idEntradaItem) {
      let maiorIdEntradaItem = this.itens.reduce((acc, currentValue) => {
        return currentValue.idEntradaItem > acc ? currentValue.idEntradaItem : acc;
      }, 0);
      item.idEntradaItem = ++maiorIdEntradaItem;
      this.itens.push(item);
    } else {
      const itemUpd = this.itens.find(itemEntrada => itemEntrada.idEntradaItem === item.idEntradaItem);
      itemUpd.equipamento = Object.assign(itemUpd.equipamento, item.equipamento);
      itemUpd.quantidade = item.quantidade;
      itemUpd.valorUnitario = item.valorUnitario;
    }

    this.tableItens.reset();
    this.entradaItem = Object.assign({});
  }

  save() {
    const entrada = this.formEntrada.getRawValue() as Entrada;
    entrada.itens = this.itens;

    this.entradaService.save(entrada.idEntrada, entrada).subscribe(
      () => {
        this.formEntrada.reset();
        this.messageService.showMessage('success', 'Lançamento de entrada salvo com sucesso!');
        this.router.navigate(['/entradas']);
      }
    );
  } 
}
