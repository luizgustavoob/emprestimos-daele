export class UsuarioFiltro {
  email: string;
  page: number;
  size: number;

  constructor() {
    this.page = 0;
    this.size = 10;
  }
}
