import { SituacaoSaida } from '../saidas/model/saida';

export function getBadgeClass(situacao: SituacaoSaida) {
  switch (situacao) {
    case SituacaoSaida.pendente:
      return 'badge badge-secondary';
    case SituacaoSaida.aprovada:
      return 'badge badge-success';
    case SituacaoSaida.encerrada:
      return 'badge badge-info';
    case SituacaoSaida.reprovada:
      return 'badge badge-danger';
  }
}
