export class Equipamento {
  idEquipamento: number;
  nome: string;
  descricao: string;
  patrimonio: number;
  grupo: GrupoItem = GrupoItem.consumo;
  valorUltimaCompra: number;
  quantidadeMinima: number;
  localizacao: string;
  devolucaoObrigatoria: boolean;
  siorg: number;

  constructor() {
    this.devolucaoObrigatoria = false;
  }
}

export enum GrupoItem {
  consumo = 'CONSUMO',
  permanente = 'PERMANENTE'
}
