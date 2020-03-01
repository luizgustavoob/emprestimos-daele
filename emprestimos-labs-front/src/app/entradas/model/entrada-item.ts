import { Equipamento } from '../../equipamentos/model/equipamento';

export class EntradaItem {
  idEntrada: number;
  idEntradaItem: number;
  equipamento: Equipamento;
  quantidade: number;
  valorUnitario: number;
  valorTotal: number;

  constructor() {
    this.equipamento = new Equipamento();
    this.idEntradaItem = 0;
    this.valorTotal = 0;
  }
}
