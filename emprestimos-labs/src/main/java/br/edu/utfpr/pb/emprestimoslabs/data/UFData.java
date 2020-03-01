package br.edu.utfpr.pb.emprestimoslabs.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.utfpr.pb.emprestimoslabs.entity.UF;

@Repository
public interface UFData extends JpaRepository<UF, Long> {

}
