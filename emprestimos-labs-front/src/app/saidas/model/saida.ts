import { Usuario } from '../../usuarios/model/usuario';
import { SaidaItem } from './saida-item';

export class Saida {
  idSaida: number;
  data: Date = new Date();
  tipoSaida: TipoSaida = TipoSaida.emprestimo;
  finalidadeSaida: FinalidadeSaida = FinalidadeSaida.projeto;
  situacao: SituacaoSaida = SituacaoSaida.pendente;
  usuario: Usuario;
  observacao: string;
  itens: SaidaItem[] = [];
  enable: boolean = true;
  unauthorized: boolean = false;
}

export enum TipoSaida {
  emprestimo = 'EMPRESTIMO',
  baixa = 'BAIXA'
}

export enum FinalidadeSaida {
  projeto = 'PROJETO',
  aulaPratica = 'AULA_PRATICA'
}

export enum SituacaoSaida {
  pendente = 'PENDENTE',
  aprovada = 'APROVADA',
  encerrada = 'ENCERRADA',
  reprovada = 'REPROVADA'
}
