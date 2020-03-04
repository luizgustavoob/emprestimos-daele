ALTER TABLE estoque
RENAME COLUMN reservas TO emprestimos;


ALTER TABLE estoque
ADD retornoemprestimos bigint;


DROP VIEW V_ESTOQUE;


CREATE OR REPLACE VIEW V_ESTOQUE AS 
  SELECT E1.IDEQUIPAMENTO, 
         E1.DATAESTOQUE, 
         coalesce(E1.ENTRADAS, 0) as ENTRADAS, 
         coalesce(E1.SAIDAS, 0) as SAIDAS, 
         coalesce(E1.EMPRESTIMOS, 0) as EMPRESTIMOS,
         coalesce(E1.RETORNOEMPRESTIMOS,0) as RETORNOEMPRESTIMOS,
         
         SUM(COALESCE(E1.ENTRADAS,0)) 
         	OVER (PARTITION BY E1.IDEQUIPAMENTO ORDER BY E1.DATAESTOQUE) TOTENTRADAS,   
         	
         SUM(COALESCE(E1.SAIDAS,0))  
         	OVER (PARTITION BY E1.IDEQUIPAMENTO ORDER BY E1.DATAESTOQUE) TOTSAIDAS,  
         	
         SUM(COALESCE(E1.EMPRESTIMOS,0))  
         	OVER (PARTITION BY E1.IDEQUIPAMENTO ORDER BY E1.DATAESTOQUE) TOTEMPRESTIMOS,
         
     	SUM(COALESCE(E1.RETORNOEMPRESTIMOS,0))  
         	OVER (PARTITION BY E1.IDEQUIPAMENTO ORDER BY E1.DATAESTOQUE) TOTRETORNOEMPRESTIMOS,
         	
         SUM(COALESCE(E1.ENTRADAS,0)) 
         	OVER (PARTITION BY E1.IDEQUIPAMENTO ORDER BY E1.DATAESTOQUE) +
         SUM(COALESCE(E1.RETORNOEMPRESTIMOS,0)) 
         	OVER (PARTITION BY E1.IDEQUIPAMENTO ORDER BY E1.DATAESTOQUE) -
         SUM(COALESCE(E1.SAIDAS,0)) 
         	OVER (PARTITION BY E1.IDEQUIPAMENTO ORDER BY E1.DATAESTOQUE) -
     	SUM(COALESCE(E1.EMPRESTIMOS,0)) 
    	 	OVER (PARTITION BY E1.IDEQUIPAMENTO ORDER BY E1.DATAESTOQUE) AS SALDO
    FROM ESTOQUE E1        
   ORDER BY E1.IDEQUIPAMENTO, E1.DATAESTOQUE DESC;


create or replace function atualiza_saldo_entrada_item(in in_dataentrada date, in in_idequipamento integer, in in_quantidade bigint) returns void as
$$
declare
	x_idestoque integer := 0;
begin
	begin
		select e.idestoque
		  into x_idestoque
		  from estoque e
		 where e.idequipamento = in_idequipamento
		   and e.dataestoque = in_dataentrada;	
	end;

	if (coalesce(x_idestoque, 0) > 0) then
		update estoque
		   set entradas = coalesce(entradas, 0) + in_quantidade
		 where idestoque = x_idestoque;  
	else
		insert into estoque(dataestoque, idequipamento, entradas)
		     values (in_dataentrada, in_idequipamento, in_quantidade);
	end if;
end;	
$$ language plpgsql;


create or replace function atualiza_estoque_entrada_item() returns trigger as
$$
declare
	x_data date;
begin
	if (tg_op = 'INSERT') then
		begin
			select e.dataentrada
			  into x_data
			  from entrada e
			 where e.identrada = new.identrada;
		end;
	
		perform atualiza_saldo_entrada_item(x_data, new.idequipamento, new.quantidade);
	
		return new;
	elsif (tg_op = 'DELETE') then
		begin
			select e.dataentrada
			  into x_data
			  from entrada e
			 where e.identrada = old.identrada;
		end;
	
		perform atualiza_saldo_entrada_item(x_data, old.idequipamento, old.quantidade*(-1));
	
		return old;
	elsif (tg_op = 'UPDATE') then
		begin
			select e.dataentrada
			  into x_data
			  from entrada e
			 where e.identrada = old.identrada;
		end;
	
		perform atualiza_saldo_entrada_item(x_data, old.idequipamento, old.quantidade*(-1));
		perform atualiza_saldo_entrada_item(x_data, new.idequipamento, new.quantidade);
	
		return new;	
	end if;	
end;
$$ language plpgsql;


create trigger trg_atualiza_estoque_entrada_item after insert or delete or update of quantidade, idequipamento ON entradaitem for each row execute procedure atualiza_estoque_entrada_item();


create or replace function atualiza_saldo_entrada(in in_identrada integer, in in_dataentrada date, in in_estorno varchar(1)) returns void as
$$
declare 
	x entradaitem%rowtype;
begin
	for x in (select ei.* 
				from entradaitem ei 
		       where ei.identrada = in_identrada)
	loop	
		if (in_estorno = 'S') then
			perform atualiza_saldo_entrada_item(in_dataentrada, x.idequipamento, x.quantidade*(-1));
		else
			perform atualiza_saldo_entrada_item(in_dataentrada, x.idequipamento, x.quantidade);
		end if;
	end loop;	
end;	
$$ language plpgsql;


create or replace function atualiza_estoque_entrada() returns trigger as
$$ 
declare
	x_olddata date;
	x_newdata date;
begin
	x_olddata := old.dataentrada;
	x_newdata := new.dataentrada;
	
	if (x_olddata <> x_newdata) then
		perform atualiza_saldo_entrada(old.identrada, x_olddata, 'S');
		perform atualiza_saldo_entrada(new.identrada, x_newdata, 'N');		
	end if;

	return new;
end;
$$ language plpgsql;


create trigger trg_atualiza_estoque_entrada after update of dataentrada ON entrada for each row execute procedure atualiza_estoque_entrada();


create or replace function atualiza_saldo_saida_item(in in_datasaida date, in in_idequipamento integer, in in_situacao varchar(20), in in_tiposaida varchar(20), in in_quantidade bigint) returns void as
$$
declare
	x_idestoque integer := 0;
begin
	begin
		select e.idestoque
		  into x_idestoque
		  from estoque e
		 where e.idequipamento = in_idequipamento
		   and e.dataestoque = in_datasaida;	
	end;	

	if (coalesce(x_idestoque, 0) > 0) then
		if (in_situacao = 'APROVADA') then
			update estoque
			   set emprestimos = coalesce(emprestimos, 0) + in_quantidade
			 where idestoque = x_idestoque;
		elsif (in_situacao = 'ENCERRADA') then
			if (in_tiposaida = 'EMPRESTIMO') then
				update estoque
				   set retornoemprestimos = coalesce(retornoemprestimos, 0) + in_quantidade
				 where idestoque = x_idestoque;
			elsif (in_tiposaida = 'BAIXA') then
				update estoque
				   set saidas = coalesce(saidas, 0) + in_quantidade
				 where idestoque = x_idestoque;			
			end if;						
		end if;			  
	else
		if (in_situacao = 'APROVADA') then
			insert into estoque(dataestoque, idequipamento, emprestimos)
			     values (in_datasaida, in_idequipamento, in_quantidade);
		elsif (in_situacao = 'ENCERRADA') then
			if (in_tiposaida = 'EMPRESTIMO') then
				insert into estoque(dataestoque, idequipamento, retornoemprestimos)
				     values (in_datasaida, in_idequipamento, in_quantidade);
			elsif (in_tiposaida = 'BAIXA') then
				insert into estoque(dataestoque, idequipamento, saidas)
				     values (in_datasaida, in_idequipamento, in_quantidade);			
			end if;
		end if;		
	end if;
end;	
$$ language plpgsql;


create or replace function atualiza_estoque_saida_item() returns trigger as
$$
declare
	x_data date;
	x_situacao varchar(20);
	x_tiposaida varchar(20);
begin
	if (tg_op = 'INSERT') then
		begin
			select s.datasaida, s.situacao, s.tiposaida
			  into x_data, x_situacao, x_tiposaida
			  from saida s
			 where s.idsaida = new.idsaida;			  
		end;		
		
		if (x_tiposaida = 'EMPRESTIMO') then
			if (x_situacao = 'APROVADA') then
				perform atualiza_saldo_saida_item(x_data, new.idequipamento, x_situacao, x_tiposaida, new.quantidade);			
			elsif (x_situacao = 'ENCERRADA') then
				perform atualiza_saldo_saida_item(new.datadevolucao, new.idequipamento, x_situacao, x_tiposaida, new.quantidadedevolvida);			
			end if;		
		elsif (x_tiposaida = 'BAIXA') then
			perform atualiza_saldo_saida_item(x_data, new.idequipamento, x_situacao, x_tiposaida, new.quantidade);
		end if;
	
		return new;
	elsif (tg_op = 'DELETE') then
		begin
			select s.datasaida, s.situacao, s.tiposaida
			  into x_data, x_situacao, x_tiposaida
			  from saida s
			 where s.idsaida = old.idsaida;			  
		end;
	
		if (x_tiposaida = 'EMPRESTIMO') then
			if (x_situacao = 'APROVADA') then
				perform atualiza_saldo_saida_item(x_data, old.idequipamento, x_situacao, x_tiposaida, old.quantidade*(-1));
			elsif (x_situacao = 'ENCERRADA') then
				perform atualiza_saldo_saida_item(x_data, old.idequipamento, 'APROVADA', x_tiposaida, old.quantidade*(-1));
				perform atualiza_saldo_saida_item(old.datadevolucao, old.idequipamento, x_situacao, x_tiposaida, old.quantidadedevolvida*(-1));
			end if;
		elsif (x_tiposaida = 'BAIXA') then
			perform atualiza_saldo_saida_item(x_data, old.idequipamento, x_situacao, x_tiposaida, old.quantidade*(-1));
		end if;
	
		return old;
	elsif (tg_op = 'UPDATE') then
		begin
			select s.datasaida, s.situacao, s.tiposaida
			  into x_data, x_situacao, x_tiposaida
			  from saida s
			 where s.idsaida = old.idsaida;			  
		end;
	
		if ((x_situacao = 'APROVADA' and x_tiposaida = 'EMPRESTIMO') or x_tiposaida = 'BAIXA') then			
			perform atualiza_saldo_saida_item(x_data, old.idequipamento, x_situacao, x_tiposaida, old.quantidade*(-1));
			perform atualiza_saldo_saida_item(x_data, new.idequipamento, x_situacao, x_tiposaida, new.quantidade);
		elsif (x_situacao = 'ENCERRADA' and x_tiposaida = 'EMPRESTIMO') then
			perform atualiza_saldo_saida_item(new.datadevolucao, new.idequipamento, x_situacao, x_tiposaida, new.quantidadedevolvida);
		end if;
	
		return new;	
	end if;	
end;
$$ language plpgsql;


create trigger trg_atualiza_estoque_saida_item after insert or delete or update of quantidade, quantidadedevolvida, idequipamento ON saidaitem for each row execute procedure atualiza_estoque_saida_item();


create type saidaitem_atualiza_saldo as (
	idequipamento integer,
	quantidade bigint,
	situacao varchar(20)
);


create or replace function atualiza_saldo_saida(in in_idsaida integer, in in_datasaida date, in in_situacao varchar(20), in in_tiposaida varchar(20), in in_estorno varchar(1)) returns void as
$$
declare
	x saidaitem_atualiza_saldo%rowtype;
begin
	for x in (select si.idequipamento, si.quantidade, s.situacao
				from saidaitem si
				left join saida s
				  on s.idsaida = si.idsaida
		       where si.idsaida = in_idsaida)
	loop		
		if (in_estorno = 'S') then
			if ( (in_situacao = 'APROVADA' and in_situacao = x.situacao) or (in_tiposaida = 'BAIXA') ) then -- atualizando um emprestimo aprovado ou atualizando uma baixa
				perform atualiza_saldo_saida_item(in_datasaida, x.idequipamento, in_situacao, in_tiposaida, x.quantidade*(-1));
			end if;
		else			
			if (in_situacao = 'APROVADA' or (in_situacao = 'ENCERRADA' and in_tiposaida = 'BAIXA')) then
				perform atualiza_saldo_saida_item(in_datasaida, x.idequipamento, in_situacao, in_tiposaida, x.quantidade);			
			end if;
		end if;		
	end loop;	
end;	
$$ language plpgsql;


create or replace function atualiza_estoque_saida() returns trigger as
$$ 
declare
	x_olddata date;
	x_newdata date;
	x_oldsituacao varchar(20);
	x_newsituacao varchar(20);
begin
	x_olddata := old.datasaida;
	x_newdata := new.datasaida;
	x_oldsituacao := old.situacao;
	x_newsituacao := new.situacao;
	
	if (x_olddata <> x_newdata or x_oldsituacao <> x_newsituacao) then
		perform atualiza_saldo_saida(old.idsaida, x_olddata, x_oldsituacao, old.tiposaida, 'S');
		perform atualiza_saldo_saida(new.idsaida, x_newdata, x_newsituacao, new.tiposaida, 'N');
	end if;

	return new;
end;
$$ language plpgsql;


create trigger trg_atualiza_estoque_saida after update of datasaida, situacao ON saida for each row execute procedure atualiza_estoque_saida();