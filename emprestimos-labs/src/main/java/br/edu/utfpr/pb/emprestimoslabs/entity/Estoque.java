package br.edu.utfpr.pb.emprestimoslabs.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.edu.utfpr.pb.emprestimoslabs.entity.dto.FichaEstoqueDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ESTOQUE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"idEstoque"})
@NamedNativeQuery(name = "Estoque.relatorioFichaEstoque", 
	resultSetMapping = "fichaEstoque", 
	query = " select :data as data, e.idequipamento, e.nome, e.patrimonio, "
		+ "       coalesce((select coalesce(saldo, 0) as saldo " 
		+ "			          from v_estoque "
		+ "	                 where idequipamento = e.idequipamento "
		+ "	                   and dataestoque <= :data " 
		+ "	                 order by dataestoque desc "
		+ "	                 limit 1), 0) as saldo " + " from equipamento e "
		+ " where (e.idequipamento in :idsEquipamentos and :todos = 'N') or (:todos = 'S')")
@SqlResultSetMapping(name = "fichaEstoque", classes = {
		@ConstructorResult(targetClass = FichaEstoqueDto.class, columns = {
				@ColumnResult(name = "data", type = LocalDate.class),
				@ColumnResult(name = "idequipamento", type = Long.class),
				@ColumnResult(name = "nome", type = String.class),
				@ColumnResult(name = "patrimonio", type = Long.class),
				@ColumnResult(name = "saldo", type = Integer.class) }) })
public class Estoque implements Serializable, EntidadeBD {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estoque_id")
	@SequenceGenerator(name = "estoque_id", sequenceName = "estoque_id", allocationSize = 1)
	@Column(name = "idestoque")
	private Long idEstoque;

	@ManyToOne
	@JoinColumn(name = "idequipamento", referencedColumnName = "idequipamento")
	private Equipamento equipamento;

	@Column(name = "dataestoque")
	private LocalDate data;

	private Integer entradas;

	private Integer saidas;

	private Integer emprestimos;
	
	@Column(name = "retornoemprestimos")
	private Integer retornoEmprestimos;

	public Estoque(LocalDate data, Equipamento item) {
		this.data = data;
		this.equipamento = item;
		this.entradas = 0;
		this.saidas = 0;
		this.saidas = 0;
		this.setRetornoEmprestimos(0);
	}

	@Override
	@JsonIgnore
	public Long getId() {
		return idEstoque;
	}

}
