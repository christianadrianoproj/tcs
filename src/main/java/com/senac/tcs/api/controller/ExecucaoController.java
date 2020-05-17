package com.senac.tcs.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.senac.tcs.api.domain.ExecucaoResposta;
import com.senac.tcs.api.domain.Regra;
import com.senac.tcs.api.domain.RegraItem;
import com.senac.tcs.api.domain.TipoConectivo;
import com.senac.tcs.api.domain.TipoVariavel;
import com.senac.tcs.api.repository.ExecucaoRespostaRepository;
import com.senac.tcs.api.domain.Execucao;
import com.senac.tcs.api.repository.ExecucaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Christian
 */

@RestController
@RequestMapping("/execucao")
@CrossOrigin(origins = "*")
public class ExecucaoController {

	@Autowired
	private ExecucaoRepository repository;

	@Autowired
	private ExecucaoRespostaRepository repositoryExecucaoResposta;

	@GetMapping
	public List<Execucao> findAll() {
		return repository.findAll();
	}

	@PostMapping("/salvaExecucao")
	public Execucao salvaRegra(@RequestBody Execucao v) {
		return repository.save(v);
	}

	@PostMapping("/adicionaResposta/{idexecucao}")
	public ResponseEntity<?> adicionaValor(@PathVariable("idexecucao") Integer idexecucao,
			@RequestBody ExecucaoResposta resposta) {
		Execucao exec = repository.findById(idexecucao).get();
		resposta.setExecucao(exec);
		repositoryExecucaoResposta.save(resposta);
		Optional<Execucao> r = repository.findById(idexecucao);
		return ResponseEntity.ok(r.get());
	}

	@PostMapping("/deletaResposta/{idexecucao}")
	public Execucao deleteItem(@PathVariable("idexecucao") Integer idexecucao, @RequestBody ExecucaoResposta resposta) {
		repositoryExecucaoResposta.deleteById(resposta.getIdExecucaoResposta());
		return repository.findById(idexecucao).get();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> find(@PathVariable("id") Integer id) {
		Optional<Execucao> exec = repository.findById(id);
		if (exec.isPresent()) {
			return ResponseEntity.ok(exec.get());
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Integer id) {
		Execucao exec = repository.findById(id).get();
		for (ExecucaoResposta i : exec.getRespostas()) {
			repositoryExecucaoResposta.deleteById(i.getIdExecucaoResposta());
		}
		repository.deleteById(id);
	}

	private List<Regra> getRegrasRespondidas(Execucao exec) {
		List<Regra> lista = new ArrayList<Regra>();
		for (ExecucaoResposta i : exec.getRespostas()) {
			if (lista.indexOf(i.getRegraItem().getRegra()) == -1) {
				lista.add(i.getRegraItem().getRegra());
			}
		}
		return lista;
	}

	private List<ExecucaoResposta> getRespostasByRegra(Execucao exec, Regra regra) {
		List<ExecucaoResposta> lista = new ArrayList<ExecucaoResposta>();
		for (ExecucaoResposta i : exec.getRespostas()) {
			if (i.getRegraItem().getRegra().getIdRegra() == regra.getIdRegra()) {
				if (lista.indexOf(i) == -1) {
					lista.add(i);
				}
			}
		}
		return lista;
	}

	private void escreveLogsSE(RegraItem item, String valorResposta) {
		Integer conectivo = item.getConectivo();
		String Condicional = item.getCondicional();
		if (conectivo == TipoConectivo._IF_.getOpcao()) {
			System.out.println("     SE " + item.getVariavel().getNome() + " " + Condicional + " "
					+ item.getVariavelValor().getValor() + " (Resposta: " + valorResposta + ")");

		} else if (conectivo == TipoConectivo._AND_.getOpcao()) {
			System.out.println("     E " + item.getVariavel().getNome() + " " + Condicional + " "
					+ item.getVariavelValor().getValor() + " (Resposta: " + valorResposta + ")");

		} else if (conectivo == TipoConectivo._OR_.getOpcao()) {
			System.out.println("     OU " + item.getVariavel().getNome() + " " + Condicional + " "
					+ item.getVariavelValor().getValor() + " (Resposta: " + valorResposta + ")");

		}
	}

	private void escreveLogsEntao(Regra regra, Boolean resultadoRegra) {
		System.out.println("     ENTÃO " + resultadoRegra);
		System.out.println(" RESULTADO DA " + regra.getNome().toUpperCase() + ": " + resultadoRegra);
		System.out.println(" ");
	}

	@GetMapping("/tomadaDecisao/{idexecucao}")
	public Execucao tomadadeDeDecisao(@PathVariable("idexecucao") Integer idexecucao) {
		Execucao exec = repository.findById(idexecucao).get();
		if (exec.getConcluido() != null) {
			for (Regra regra : getRegrasRespondidas(exec)) {
				Boolean condicaoItemRegra = false;
				List<Boolean> validacaoItemRegra = new ArrayList<Boolean>();
				List<Integer> conectivoItemRegra = new ArrayList<Integer>();
				System.out.println("------------------------------------------------------------");

				// executa validacao item a item da regra
				for (ExecucaoResposta i : getRespostasByRegra(exec, regra)) {

					String Condicional = i.getRegraItem().getCondicional();
					String valorResposta = i.getResposta().getValor();
					String valorRegra = i.getRegraItem().getVariavelValor().getValor();
					Integer tipoVariavel = i.getRegraItem().getVariavel().getTipoVariavel();
					Integer conectivo = i.getRegraItem().getConectivo();

					escreveLogsSE(i.getRegraItem(), valorResposta);

					if (tipoVariavel == TipoVariavel.Numerica.getTipo()) {
						if (Condicional.equals("=")) {
							if (Float.parseFloat(valorRegra) == Float.parseFloat(valorResposta)) {
								condicaoItemRegra = true;

							} else {
								condicaoItemRegra = false;
							}

						} else if (Condicional.equals("<>")) {
							if (Float.parseFloat(valorRegra) != Float.parseFloat(valorResposta)) {
								condicaoItemRegra = true;
							} else {
								condicaoItemRegra = false;
							}
						} else if (Condicional.equals(">")) {
							if (Float.parseFloat(valorRegra) > Float.parseFloat(valorResposta)) {
							} else {
								condicaoItemRegra = false;
							}
						} else if (Condicional.equals(">=")) {
							if (Float.parseFloat(valorRegra) >= Float.parseFloat(valorResposta)) {
								condicaoItemRegra = true;
							} else {
								condicaoItemRegra = false;
							}

						} else if (Condicional.equals("<")) {
							if (Float.parseFloat(valorRegra) < Float.parseFloat(valorResposta)) {
								condicaoItemRegra = true;
							} else {
								condicaoItemRegra = false;
							}

						} else if (Condicional.equals("<=")) {
							if (Float.parseFloat(valorRegra) <= Float.parseFloat(valorResposta)) {
								condicaoItemRegra = true;
							} else {
								condicaoItemRegra = false;
							}
						}

					} else {
						if (Condicional.equals("=")) {
							if (valorRegra.equalsIgnoreCase(valorResposta)) {
								condicaoItemRegra = true;
							} else {
								condicaoItemRegra = false;
							}
						} else if (Condicional.equals("<>")) {
							if (!valorRegra.equalsIgnoreCase(valorResposta)) {
								condicaoItemRegra = true;
							} else {
								condicaoItemRegra = false;
							}

						}
					}
					validacaoItemRegra.add(condicaoItemRegra);
					conectivoItemRegra.add(conectivo);
				}

				// junta as validacoes dos itens na regra
				Boolean resultadoRegra = true;
				for (int i = 0; i < validacaoItemRegra.size(); i++) {
					Integer conectivo = conectivoItemRegra.get(i);
					if (conectivo == TipoConectivo._IF_.getOpcao()) {
						resultadoRegra = (resultadoRegra) && (validacaoItemRegra.get(i));
					} else if (conectivo == TipoConectivo._AND_.getOpcao()) {
						resultadoRegra = (resultadoRegra) && (validacaoItemRegra.get(i));

					} else if (conectivo == TipoConectivo._OR_.getOpcao()) {
						if ((i > 0) && (conectivoItemRegra.get(i - 1) == TipoConectivo._AND_.getOpcao())) {
							resultadoRegra = (resultadoRegra) && (validacaoItemRegra.get(i));
						} else {
							resultadoRegra = validacaoItemRegra.get(i);
						}
					}
				}
				escreveLogsEntao(regra, resultadoRegra);
			}
		}
		return exec;
	}
}