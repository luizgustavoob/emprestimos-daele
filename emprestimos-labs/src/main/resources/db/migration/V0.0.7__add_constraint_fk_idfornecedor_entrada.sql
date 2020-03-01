alter table entrada
add constraint fk_entrada_fornecedor foreign key (idfornecedor) references fornecedor (idfornecedor);