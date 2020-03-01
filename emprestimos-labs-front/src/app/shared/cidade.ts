import { UF } from './uf';

export class Cidade {
  idCidade: number;
  nome: string;
  codigoIbge: number;
  uf: UF;

  constructor() {
    this.uf = new UF();
  }
}
