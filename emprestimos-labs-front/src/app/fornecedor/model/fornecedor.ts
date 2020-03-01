import { Endereco } from '../../shared/endereco';

export class Fornecedor {
  idFornecedor: number;
  razaoSocial: string;
  nomeFantasia: string;
  cnpj: string;
  endereco: Endereco = new Endereco();
}
