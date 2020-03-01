package br.edu.utfpr.pb.emprestimoslabs.data;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.utfpr.pb.emprestimoslabs.entity.EntradaItem;
import br.edu.utfpr.pb.emprestimoslabs.entity.EntradaItemPK;

@Repository
public interface EntradaItemData extends JpaRepository<EntradaItem, EntradaItemPK> {

	@Query(nativeQuery = true, value = " SELECT EI.VALORUNITARIO "
								       + " FROM ENTRADA E"
									   + " LEFT JOIN ENTRADAITEM EI"
								         + " ON EI.IDENTRADA = E.IDENTRADA"
								      + " WHERE EI.IDEQUIPAMENTO = :idEquipamento"
								      + " ORDER BY E.DATAENTRADA DESC"
									  + " LIMIT 1 ")
	Optional<BigDecimal> findValorUnitarioUltimaCompra(@Param("idEquipamento") Long idEquipamento);
	
}
