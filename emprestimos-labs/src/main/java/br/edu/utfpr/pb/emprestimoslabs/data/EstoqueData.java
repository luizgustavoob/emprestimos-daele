package br.edu.utfpr.pb.emprestimoslabs.data;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.utfpr.pb.emprestimoslabs.entity.Equipamento;
import br.edu.utfpr.pb.emprestimoslabs.entity.Estoque;
import br.edu.utfpr.pb.emprestimoslabs.entity.dto.EquipamentoDto;

@Repository
public interface EstoqueData extends JpaRepository<Estoque, Long> {
	
	Optional<Estoque> findByDataAndEquipamento(LocalDate data, Equipamento equipamento);
	
	@Query(nativeQuery = true, value = "SELECT COALESCE(V1.SALDO, 0) AS SALDO "
									 + "  FROM V_ESTOQUE V1 "
							         + " WHERE V1.DATAESTOQUE <= :data "
								     + "   AND V1.IDEQUIPAMENTO = :idEquipamento "
								     + " ORDER BY V1.DATAESTOQUE DESC "
							         + " LIMIT 1")
	Integer findSaldoEstoqueNaData(@Param("idEquipamento") Long idEquipamento, @Param("data") LocalDate data);
	
	@Query(nativeQuery = true, value =
			" SELECT x.idequipamento, x.nome, x.patrimonio, x.qtdeminima, x.saldo" +
			"  FROM (SELECT e.idequipamento, e.nome, e.patrimonio, coalesce(e.qtdeminima, 0) as qtdeminima," +
			"      		   coalesce((SELECT coalesce(V1.SALDO, 0) as SALDO " +
			"			     	  	   FROM v_estoque V1 " +
			"		 				 WHERE V1.dataestoque <= current_date " +
			"						   AND v1.idequipamento = e.idequipamento " +
			"						 ORDER BY v1.dataestoque desc " +
			"						 LIMIT 1), 0) as saldo " +
			" 	      FROM EQUIPAMENTO e) x " +
			" WHERE x.qtdeminima > 0 " +
			"   AND x.qtdeminima + 5 >= x.saldo")
	List<EquipamentoDto> findEquipamentosComEstoqueEsgotando();
}
