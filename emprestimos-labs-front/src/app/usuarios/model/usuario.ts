import { Permissao } from './permissao';

export class Usuario {

  idUsuario: number;
  nome: string;
  email: string;
  nrora: number;
  senha: string;
  permissao: Permissao = Permissao.aluno;
  ativo: boolean = false;
}
