package br.edu.utfpr.pb.emprestimoslabs.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.utfpr.pb.emprestimoslabs.entity.Usuario;

@Repository
public interface UsuarioData extends JpaRepository<Usuario, Long>{
	
	Optional<Usuario> findByEmail(String email);
	
	@Query("SELECT u FROM Usuario u WHERE u.ativo = false AND (u.permissao = 'ALUNO' or u.permissao = 'PROFESSOR') AND u.dataInativacao IS NULL")
	List<Usuario> findUsuariosPendentes();
	
	@Query(nativeQuery = true,
			value = "SELECT * FROM USUARIO WHERE ( CAST(NRORA AS VARCHAR(10)) LIKE %:chavePesquisa% OR EMAIL LIKE %:chavePesquisa2% ) ")
	List<Usuario> findUsuariosByNroRAOrEmail(@Param("chavePesquisa") String nrora, @Param("chavePesquisa2") String email);
}
