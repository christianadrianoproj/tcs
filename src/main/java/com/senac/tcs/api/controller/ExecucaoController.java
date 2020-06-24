package com.senac.tcs.api.controller;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
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
import com.senac.tcs.api.repository.FotoRepository;
import com.senac.tcs.api.repository.RegraRepository;
import com.senac.tcs.api.repository.VariavelValorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
	private FotoRepository repositoryImage;

	@Autowired
	VariavelValorRepository repositoryVariavelValor;

	private String logTomadaDecisao = "";

	@PostMapping("/iniciaExecucao/{idimage}")
	public Execucao iniciaExecucao(@PathVariable("idimage") Integer idimage) {
		Execucao exec = new Execucao();

		if (idimage > 0) {
			exec.setImage(repositoryImage.getOne(idimage));
		}
		exec.setDatahora(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
		exec.setRegras(new ArrayList<ExecucaoRegra>());
		Execucao e = repository.save(exec);
		exec.setIdExecucao(e.getIdExecucao());
		System.out.println("Execucao Criado: " + exec.getIdExecucao());
		for (Regra regra : repositoryRegra.findAll(Sort.by("ordem"))) {
			ExecucaoRegra execRegra = new ExecucaoRegra();
			execRegra.setExecucao(exec);
			execRegra.setRespostas(new ArrayList<ExecucaoRegraResposta>());
			execRegra.setRegra(regra);
			ExecucaoRegra er = repositoryExecucaoRegra.save(execRegra);
			execRegra.setIdExecucaoRegra(er.getIdExecucaoRegra());
			System.out.println("ExecucaoRegra Criado: " + execRegra.getIdExecucaoRegra());
			for (RegraItem item : regra.getItens()) {
				ExecucaoRegraResposta resposta = new ExecucaoRegraResposta();
				resposta.setExecucaoRegra(execRegra);
				resposta.setRegraItem(item);
				resposta.setResposta(null);
				ExecucaoRegraResposta resp = repositoryExecucaoRegraResposta.save(resposta);
				resposta.setIdExecucaoRegraResposta(resp.getIdExecucaoRegraResposta());
				System.out.println("ExecucaoRegraResposta Criado: " + resposta.getIdExecucaoRegraResposta());
				execRegra.getRespostas().add(resposta);
			}
			exec.getRegras().add(execRegra);
		}
		// Optional<Execucao> r = repository.findById(exec.getIdExecucao());
		// return ResponseEntity.ok(r.get());
		return exec;
	}

	@PostMapping("/adicionaRespostas/{idexecucao}")
	public ResponseEntity<?> adicionaRespostas(@PathVariable("idexecucao") Integer idexecucao,
			@RequestBody List<String> arrayRespostas) {
		Execucao exec = repository.getOne(idexecucao);

		for (int i = 0; i < arrayRespostas.size(); i++) {
			String[] valores = arrayRespostas.get(i).split(";");
			for (ExecucaoRegra regra : exec.getRegras()) {
				for (ExecucaoRegraResposta resp : regra.getRespostas()) {
					if (resp.getIdExecucaoRegraResposta() == Integer.parseInt(valores[0])) {
						resp.setExecucaoRegra(regra);
						if (Integer.parseInt(valores[1]) == -1) {
							resp.setResposta(repositoryVariavelValor.getOne(1)); // Sem resposta
							repositoryExecucaoRegraResposta.save(resp);
						} else {
							resp.setResposta(repositoryVariavelValor.getOne(Integer.parseInt(valores[1])));
							repositoryExecucaoRegraResposta.save(resp);
						}
					}
				}
			}
		}
		
		exec.setConcluido(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
		repository.save(exec);

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
		lista.sort(Comparator.comparingInt(Regra::getOrdem));
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
		String str = "";
		if (resultadoRegra) {
			str += "Verdadeira\n";
		} else {
			str += "Falsa\n";
		}

		logTomadaDecisao += "     ENTÃO " + str;
		logTomadaDecisao += " RESULTADO DA " + regra.getNome().toUpperCase() + ": " + str;
		logTomadaDecisao += "\n";
	}
	
	/**
	 * A partir das respostas obtida sobre os sintomas/características selecionadas/identificas a máquina de inferência valida a probabilidade do Míldio
	 */	
	@GetMapping("/tomadaDecisao/{idexecucao}")
	public Execucao tomadaDeDecisao(@PathVariable("idexecucao") Integer idexecucao) {
		Execucao exec = repository.findById(idexecucao).get();
		logTomadaDecisao = "";
		if (exec.getConcluido() != null) {
			for (Regra regra : getRegrasRespondidas(exec)) {
				Boolean condicaoItemRegra = false;
				List<Boolean> validacaoItemRegra = new ArrayList<Boolean>();
				List<Integer> conectivoItemRegra = new ArrayList<Integer>();
				logTomadaDecisao += "------------------------------------------------------------\n";
				/* executa validação item a item (condição) da regra */
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
							if (i.getResposta().getIdVariavelValor() == i.getRegraItem().getVariavelValor()
									.getIdVariavelValor()) {
								condicaoItemRegra = true;
							} else {
								condicaoItemRegra = false;
							}
						} else if (Condicional.equals("<>")) {
							if (i.getResposta().getIdVariavelValor() != i.getRegraItem().getVariavelValor()
									.getIdVariavelValor()) {
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
				/* Aplica a validação da regra: Junção das validações dos itens (condições) da regra */
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
			exec = repository.getOne(exec.getIdExecucao());
			logTomadaDecisao += " Míldio: " + (new DecimalFormat("###.##")).format(exec.getPercentualAcerto()) + "% \n";
			logTomadaDecisao += "\n";
			System.out.println(logTomadaDecisao);
		} else {
			exec = null;
		}
		return exec;
	}
}