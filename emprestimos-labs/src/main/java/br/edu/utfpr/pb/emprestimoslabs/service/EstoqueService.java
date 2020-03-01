package br.edu.utfpr.pb.emprestimoslabs.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.edu.utfpr.pb.emprestimoslabs.data.EstoqueData;
import br.edu.utfpr.pb.emprestimoslabs.entity.Equipamento;
import br.edu.utfpr.pb.emprestimoslabs.entity.Estoque;
import br.edu.utfpr.pb.emprestimoslabs.entity.dto.EquipamentoDto;
import br.edu.utfpr.pb.emprestimoslabs.entity.dto.FichaEstoqueDto;
import br.edu.utfpr.pb.emprestimoslabs.security.util.UsuarioAutenticado;
import br.edu.utfpr.pb.emprestimoslabs.service.generic.CrudServiceImpl;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class EstoqueService extends CrudServiceImpl<Estoque, Long>{
	
	@Autowired
	private EstoqueData estoqueData;
	@PersistenceContext
	private EntityManager manager;

	@Override
	protected JpaRepository<Estoque, Long> getData() {
		return estoqueData;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Deprecated
	public Estoque update(Long id, Estoque estoque) {
		return null;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public void atualizarEstoqueEntrada(Equipamento equipamento, LocalDate data, Integer quantidade) {
		Estoque estoque = estoqueData.findByDataAndEquipamento(data, equipamento).orElse(new Estoque(data, equipamento));
		estoque.addEntradas(quantidade);
		estoqueData.save(estoque);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public void atualizarEstoqueSaida(Equipamento equipamento, LocalDate data, Integer quantidade) {
		Estoque estoque = estoqueData.findByDataAndEquipamento(data, equipamento).orElse(new Estoque(data, equipamento));
		estoque.addSaidas(quantidade);
		estoqueData.save(estoque);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public void atualizarEstoqueReservas(Equipamento equipamento, LocalDate data, Integer quantidade) {
		Estoque estoque = estoqueData.findByDataAndEquipamento(data, equipamento).orElse(new Estoque(data, equipamento));
		estoque.addReservas(quantidade);
		estoqueData.save(estoque);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<EquipamentoDto> findEquipamentosComEstoqueEsgotando() {
		return estoqueData.findEquipamentosComEstoqueEsgotando();
	}
	
	public byte[] relatorioFichaDeEstoque(LocalDate data, Integer[] idsEquipamentos) throws Exception {
		char todos;
		
		if (idsEquipamentos == null) {
			idsEquipamentos = new Integer[1];
			idsEquipamentos[0] = 0;
			todos = 'S';
		} else {
			todos = 'N';
		}

		List<Integer> ids = Arrays.asList(idsEquipamentos);
		
		TypedQuery<FichaEstoqueDto> query = manager.createNamedQuery("Estoque.relatorioFichaEstoque", FichaEstoqueDto.class);
		query.setParameter("data", Date.valueOf(data));
		query.setParameter("idsEquipamentos", ids);
		query.setParameter("todos", todos);

		List<FichaEstoqueDto> dados = query.getResultList();
		
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("DATA", Date.valueOf(data));
		parametros.put("USUARIO", UsuarioAutenticado.get().getNome());
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		//parametros.put("LOGO", new ImageIcon(this.getClass().getResource("/static/img/logo-utfpr-branca.png")).getImage());
		
		InputStream streamRelatorio = this.getClass().getResourceAsStream("/relatorios/ficha-de-estoque.jasper");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(streamRelatorio, parametros,
				new JRBeanCollectionDataSource(dados));
		
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
}
