-- Cidade
create sequence uf_id start 1 increment 1;
create table if not exists UF (
	iduf integer not null default nextval('uf_id'),
	uf char(2) not null unique,
	nome varchar(50) not null,
	codigouf integer not null unique,
	constraint pk_uf primary key (iduf)
);


-- UF
create sequence cidade_id start 1 increment 1;
create table if not exists cidade (
	idcidade integer not null default nextval('cidade_id'),
	nome varchar(50) not null,
	ibge integer unique,
	iduf integer not null,
	constraint pk_cidade primary key (idcidade),
	constraint fk_cidade_uf foreign key (iduf) references uf (iduf)
);


-- Usuário
create sequence usuario_id start 1 increment 1;
create table if not exists usuario (
	idusuario integer not null default nextval('usuario_id'),
	email varchar(50) not null unique,
	senha varchar(256) not null,
	nome varchar(100) not null,
	ativo boolean not null,
	permissao varchar(20) not null,
	constraint pk_usuario primary key (idusuario)
);


-- Fornecedor
create sequence fornecedor_id start 1 increment 1;
create table if not exists fornecedor (
	idfornecedor integer not null default nextval('fornecedor_id'),
	razaosocial varchar(100) not null,
	fantasia varchar(50),
	cnpj varchar(14) not null unique,
	logradouro varchar(100) not null,
	numero varchar(5) not null,
	complemento varchar(50),
	bairro varchar(50) not null,
	cep varchar(8) not null,
	idcidade integer not null,
	constraint pk_fornecedor primary key (idfornecedor),
	constraint fk_fornecedor_cidade foreign key (idcidade) references cidade (idcidade)
);


-- Equipamento
create sequence equipamento_id start 1 increment 1;
create table if not exists equipamento (
	idequipamento integer not null default nextval('equipamento_id'),
	nome varchar(20) not null,
	descricao varchar(100),
	patrimonio integer,
	grupo varchar(10) not null,
	imagem varchar(100),
	qtdeminima integer not null,
	localizacao varchar(50),
	obrigadevolucao boolean not null,
	siorg integer,
	constraint pk_equipamento primary key (idequipamento)
);


-- Estoque
create sequence estoque_id start 1 increment 1;
create table if not exists estoque (
	idestoque integer not null default nextval('estoque_id'),
	idequipamento integer not null,
	dataestoque date not null,
	entradas bigint,
	saidas bigint,
	reservas bigint,
	constraint pk_estoque primary key (idestoque),
	constraint fk_estoque_equipamento foreign key (idequipamento) references equipamento (idequipamento)
);


-- Entrada
create sequence entrada_id start 1 increment 1;
create table if not exists entrada (
	identrada integer not null default nextval('entrada_id'),
	dataentrada date not null,
	idfornecedor integer not null,
	idusuario integer not null,
	constraint pk_entrada primary key (identrada)
);
create table if not exists entradaitem (
	identrada integer not null,
	identradaitem integer not null,
	idequipamento integer not null,
	quantidade integer not null,
	valorunitario decimal(10, 2) not null,
	constraint pk_entradaitem primary key (identrada, identradaitem),
	constraint fk_entradaitem_entrada foreign key (identrada) references entrada (identrada),
	constraint fk_entradaitem_equipamento foreign key (idequipamento) references equipamento (idequipamento)
);


-- Saída
create sequence saida_id start 1 increment 1;
create table if not exists saida (
	idsaida integer not null default nextval('saida_id'),
	datasaida date not null,
	tiposaida varchar(10) not null,
	finalidade varchar(15) not null,
	situacao varchar(10) not null,
	idusuario integer not null,
	constraint pk_saida primary key (idsaida),
	constraint fk_saida_usuario foreign key (idusuario) references usuario (idusuario)
);
create table if not exists saidaitem (
	idsaida integer not null,
	idequipamento integer not null,
	quantidade integer not null,
	quantidadedevolvida integer,
	datadevolucao date,
	constraint pk_saidaitem primary key (idsaida, idequipamento),
	constraint fk_saidaitem_saida foreign key (idsaida) references saida (idsaida),
	constraint fk_saidaitem_equipamento foreign key (idequipamento) references equipamento (idequipamento)
);