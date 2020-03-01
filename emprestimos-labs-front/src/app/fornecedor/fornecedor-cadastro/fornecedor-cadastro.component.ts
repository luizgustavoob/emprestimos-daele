import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Fornecedor } from '../model/fornecedor';
import { Cidade } from '../../shared/cidade';
import { FornecedorService } from '../service/fornecedor.service';
import { CidadeService } from '../../shared/services/cidade.service';
import { MyMessageService } from '../../shared/services/my-message.service';

@Component({
  selector: 'app-fornecedor-cadastro',
  templateUrl: './fornecedor-cadastro.component.html'
})
export class FornecedorCadastroComponent implements OnInit {

  formFornecedor: FormGroup;
  cidades: Cidade[];

  constructor(private activatedRoute: ActivatedRoute,
              private router: Router,
              private formBuilder: FormBuilder,
              private fornecedorService: FornecedorService,
              private cidadeService: CidadeService,
              private messageService: MyMessageService) { }

  ngOnInit() {
    this.configurarForm();
    this.activatedRoute.params.subscribe(
      () => this.atualizarFormByFornecedor(this.activatedRoute.snapshot.data.fornecedor)
    );
  }

  private configurarForm() {
    this.formFornecedor = this.formBuilder.group({
      idFornecedor: [],
      razaoSocial: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(100)]],
      cnpj: ['', Validators.required],
      nomeFantasia: ['', Validators.maxLength(50)],
      endereco: this.formBuilder.group({
        logradouro: ['', [Validators.required, Validators.maxLength(100)]],
        numero: ['', [Validators.required, Validators.maxLength(5)]],
        complemento: ['', Validators.maxLength(50)],
        bairro: ['', [Validators.required, Validators.maxLength(50)]],
        cidade: ['', Validators.required],
        cep: ['', [Validators.required, Validators.maxLength(8)]]
      })
    });
  }

  private atualizarFormByFornecedor(fornecedor) {
    this.formFornecedor.patchValue(fornecedor);
  }

  save() {
    const fornecedor = this.formFornecedor.getRawValue() as Fornecedor;

    this.fornecedorService.save(fornecedor.idFornecedor, fornecedor).subscribe(
      () => {
        this.formFornecedor.reset();
        this.messageService.showMessage('success', 'Fornecedor salvo com sucesso!');
        this.router.navigate(['/fornecedores']);
      }
    );
  }

  findCidades(event) {
    this.cidadeService.findByNomeOrUF(event.query).subscribe(
      (resp) => this.cidades = resp
    );
  }
}
