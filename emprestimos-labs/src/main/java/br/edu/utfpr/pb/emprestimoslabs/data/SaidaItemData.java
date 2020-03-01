package br.edu.utfpr.pb.emprestimoslabs.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.utfpr.pb.emprestimoslabs.entity.SaidaItem;
import br.edu.utfpr.pb.emprestimoslabs.entity.SaidaItemPK;

@Repository
public interface SaidaItemData extends JpaRepository<SaidaItem, SaidaItemPK> {

}
