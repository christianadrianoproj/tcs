package com.senac.tcs.api.controller;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.senac.tcs.api.domain.ExecucaoRegraResposta;
import com.senac.tcs.api.domain.ExecucaoRegra;
import com.senac.tcs.api.domain.Regra;
import com.senac.tcs.api.domain.RegraItem;
import com.senac.tcs.api.domain.TipoConectivo;
import com.senac.tcs.api.domain.TipoVariavel;
import com.senac.tcs.api.repository.ExecucaoRegraRespostaRepository;
import com.senac.tcs.api.repository.ExecucaoRegraRepository;
import com.senac.tcs.api.domain.Execucao;
import com.senac.tcs.api.repository.ExecucaoRepository;
import com.senac.tcs.api.repository.ImageRepository;
import com.senac.tcs.api.repository.RegraRepository;

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
	private ExecucaoRegraRespostaRepository repositoryExecucaoRegraResposta;

	@Autowired
	private ExecucaoRegraRepository repositoryExecucaoRegra;
	
	@Autowired
	private RegraRepository repositoryRegra;
	
	@Autowired
	private ImageRepository repositoryImage;
	
	private String logTomadaDecisao = "";
	
	@PostMapping("/iniciaExecucao/{idimage}")
	public ResponseEntity<?> iniciaExecucao(@PathVariable("idimage") Integer idimage) {
		Execucao exec = new Execucao();
		
		if (idimage > 0) {
			exec.setImage(repositoryImage.getOne(idimage));
		}
		exec.setDatahora(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
		exec.setRegras(new ArrayList<ExecucaoRegra>());
		exec = repository.save(exec);
		
		for (Regra regra : repositoryRegra.findAll()) {
			ExecucaoRegra execRegra = new ExecucaoRegra();
			execRegra.setExecucao(exec);
			execRegra.setRespostas(new ArrayList<ExecucaoRegraResposta>());
			execRegra.setRegra(regra);
			execRegra = repositoryExecucaoRegra.save(execRegra);
			
			for (RegraItem item : regra.getItens()) {
				if (!item.getPergunta().trim().equals("")) {
					ExecucaoRegraResposta resposta = new ExecucaoRegraResposta();
					resposta.setExecucaoRegra(execRegra);
					resposta.setRegraItem(item);
					resposta.setResposta(null);
					resposta = repositoryExecucaoRegraResposta.save(resposta);			
				}	
			}
		}
		Optional<Execucao> r = repository.findById(exec.getIdExecucao());
		return ResponseEntity.ok(r.get());
	}

	@PostMapping("/salvaExecucao")
	public Execucao salvaRegra(@RequestBody Execucao v) {
		return repository.save(v);
	}

	@PostMapping("/adicionaRegra/{idexecucao}")
	public ResponseEntity<?> adicionaValor(@PathVariable("idexecucao") Integer idexecucao,
			@RequestBody ExecucaoRegra regra) {
		Execucao exec = repository.findById(idexecucao).get();
		regra.setExecucao(exec);
		repositoryExecucaoRegra.save(regra);
		Optional<Execucao> r = repository.findById(idexecucao);
		return ResponseEntity.ok(r.get());
	}

	@PostMapping("/deletaRegra/{idexecucao}")
	public Execucao deleteItem(@PathVariable("idexecucao") Integer idexecucao, @RequestBody ExecucaoRegra regra) {
		repositoryExecucaoRegra.deleteById(regra.getIdExecucaoRegra());
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
		for (ExecucaoRegra i : exec.getRegras()) {
			repositoryExecucaoRegra.deleteById(i.getIdExecucaoRegra());
		}
		repository.deleteById(id);
	}

	private List<Regra> getRegrasRespondidas(Execucao exec) {
		List<Regra> lista = new ArrayList<Regra>();
		for (ExecucaoRegra i : exec.getRegras()) {
			if (lista.indexOf(i.getRegra()) == -1) {
				lista.add(i.getRegra());
			}
		}
		return lista;
	}

	private ExecucaoRegra getExecucaoRegra(Execucao exec, Regra regra) {
		ExecucaoRegra objRegra = null;
		for (ExecucaoRegra i : exec.getRegras()) {
			if (i.getRegra().getIdRegra() == regra.getIdRegra()) {
				objRegra = i;
				break;
			}
		}
		return objRegra;
	}

	private List<ExecucaoRegraResposta> getRespostasByRegra(Execucao exec, Regra regra) {
		List<ExecucaoRegraResposta> lista = new ArrayList<ExecucaoRegraResposta>();
		for (ExecucaoRegra i : exec.getRegras()) {
			if (i.getRegra().getIdRegra() == regra.getIdRegra()) {
				for (ExecucaoRegraResposta execucaoRegraResposta : i.getRespostas()) {
					if (lista.indexOf(execucaoRegraResposta) == -1) {
						lista.add(execucaoRegraResposta);
					}
				}
			}
		}
		return lista;
	}

	private void escreveLogsSE(RegraItem item, String valorResposta) {
		Integer conectivo = item.getConectivo();
		String Condicional = item.getCondicional();
		if (conectivo == TipoConectivo._IF_.getOpcao()) {
			logTomadaDecisao += "     SE " + item.getVariavel().getNome() + " " + Condicional + " "
					+ item.getVariavelValor().getValor() + " (Resposta: " + valorResposta + ") \n";

		} else if (conectivo == TipoConectivo._AND_.getOpcao()) {
			logTomadaDecisao += "     E " + item.getVariavel().getNome() + " " + Condicional + " "
					+ item.getVariavelValor().getValor() + " (Resposta: " + valorResposta + ") \n";

		} else if (conectivo == TipoConectivo._OR_.getOpcao()) {
			logTomadaDecisao += "     OU " + item.getVariavel().getNome() + " " + Condicional + " "
					+ item.getVariavelValor().getValor() + " (Resposta: " + valorResposta + ") \n";

		}
	}

	private void escreveLogsEntao(Regra regra, Boolean resultadoRegra) {
		logTomadaDecisao += "     ENTÃO " + resultadoRegra+"\n";
		logTomadaDecisao += " RESULTADO DA " + regra.getNome().toUpperCase() + ": "+ resultadoRegra+"\n";
		logTomadaDecisao += "\n";
	}

	@GetMapping("/tomadaDecisao/{idexecucao}")
	public Execucao tomadadeDeDecisao(@PathVariable("idexecucao") Integer idexecucao) {
		Execucao exec = repository.findById(idexecucao).get();
		logTomadaDecisao = "";
		if (exec.getConcluido() != null) {
			for (Regra regra : getRegrasRespondidas(exec)) {
				Boolean condicaoItemRegra = false;
				List<Boolean> validacaoItemRegra = new ArrayList<Boolean>();
				List<Integer> conectivoItemRegra = new ArrayList<Integer>();
				logTomadaDecisao += "------------------------------------------------------------\n";

				// executa validacao item a item da regra
				for (ExecucaoRegraResposta i : getRespostasByRegra(exec, regra)) {

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
					i.setAcertou(condicaoItemRegra);
					repositoryExecucaoRegraResposta.save(i);
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
				ExecucaoRegra execRegra = getExecucaoRegra(exec, regra);
				if (execRegra != null) {
					execRegra.setValidou(resultadoRegra);
					repositoryExecucaoRegra.save(execRegra);
				}
			}
		}
		exec = repository.getOne(exec.getIdExecucao());
		logTomadaDecisao += " PERCENTUAL DE ACERTO: " + (new DecimalFormat("###.##")).format(exec.getPercentualAcerto())+"% \n";
		logTomadaDecisao += "\n";
		System.out.println(logTomadaDecisao);
		return exec;
	}
}