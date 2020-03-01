import { Fornecedor } from '../../fornecedor/model/fornecedor';
import { EntradaItem } from './entrada-item';

export class Entrada {
  idEntrada: number;
  data: Date = new Date();
  fornecedor: Fornecedor;
  usuario: string;
  itens: EntradaItem[] = [];
  valorTotal: number;

  constructor() {
    this.valorTotal = 0;
  }
}
