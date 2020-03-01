import { SituacaoSaida } from './saida';

export class SituacaoSaidaDescricao {
    
    constructor(public readonly situacao: SituacaoSaida,
                public readonly descricao: string) { }
}
