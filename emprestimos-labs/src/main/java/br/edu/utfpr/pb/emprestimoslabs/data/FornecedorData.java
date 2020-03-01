package br.edu.utfpr.pb.emprestimoslabs.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.utfpr.pb.emprestimoslabs.entity.Fornecedor;

@Repository
public interface FornecedorData extends JpaRepository<Fornecedor, Long> {

	@Query("SELECT f FROM Fornecedor f "
			+ " WHERE LOWER(f.razaoSocial) LIKE LOWER(CONCAT('%', :chavePesquisa, '%')) "
			+ " OR LOWER (f.nomeFantasia) LIKE LOWER(CONCAT('%', :chavePesquisa, '%')) "
			+ " OR f.cnpj like CONCAT('%', :chavePesquisa, '%') "
			+ " ORDER BY f.razaoSocial")
	List<Fornecedor> findRazaoSocialOrCNPJ(@Param("chavePesquisa") String chavePesquisa);
}
