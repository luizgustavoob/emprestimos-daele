import { Equipamento } from '../../equipamentos/model/equipamento';

export class SaidaItem {
  equipamento: Equipamento;
  quantidade: number;
  quantidadeDevolvida: number;
  dataDevolucao: Date;

  constructor() {
    this.equipamento = new Equipamento();
  }
}
