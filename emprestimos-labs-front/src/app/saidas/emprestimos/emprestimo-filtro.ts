import { Usuario } from '../../usuarios/model/usuario';
import { SituacaoSaida } from '../model/saida';

export class EmprestimoFiltro {
  data: Date = new Date();
  usuario: Usuario;
  situacao: SituacaoSaida[] = [];
  page: number = 0;
  size: number = 10;
}
