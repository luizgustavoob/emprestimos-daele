export class Queue {

  static readonly NOVOS_USUARIOS = '/queue/novos-usuarios';
  static readonly NOVOS_EMPRESTIMOS = '/queue/novos-emprestimos';
  static readonly ATUALIZAR_ESTOQUE = '/queue/atualizar-estoque';

  public static getQueueUser(user: string): string {
    return '/queue/' + user;
  }
}
