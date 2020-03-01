alter table usuario add dtinclusao date;
alter table usuario add dtinativo date;
alter table usuario add nrora integer unique;

alter table saida add observacao varchar(200);