package br.edu.utfpr.pb.emprestimoslabs.data;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.utfpr.pb.emprestimoslabs.entity.Saida;
import br.edu.utfpr.pb.emprestimoslabs.entity.Usuario;

@Repository
public interface SaidaData extends JpaRepository<Saida, Long> {

	Page<Saida> findByUsuarioEqualsOrderByDataDesc(Usuario usuario, Pageable pageable);
	
	@Query("SELECT s FROM Saida s WHERE s.tipoSaida = 'EMPRESTIMO' AND s.situacao = 'PENDENTE' ORDER BY s.idSaida")
	List<Saida> findEmprestimosPendentes();
}
