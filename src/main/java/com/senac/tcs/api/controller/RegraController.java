package com.senac.tcs.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.senac.tcs.api.domain.RegraItem;
import com.senac.tcs.api.domain.RegraItemResultado;
import com.senac.tcs.api.repository.RegraItemRepository;
import com.senac.tcs.api.repository.RegraItemResultadoRepository;
import com.senac.tcs.api.domain.Regra;
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
@RequestMapping("/regra")
@CrossOrigin(origins = "*")
public class RegraController {

	@Autowired
	private RegraRepository repository;

	@Autowired
	private RegraItemRepository repositoryRegraItem;

	@Autowired
	private RegraItemResultadoRepository repositoryRegraItemResultado;

	@GetMapping
	public List<Regra> findAll() {
		return repository.findAll();
	}

	@GetMapping("/perguntas")
	public List<Regra> getPerguntas() {
		List<Regra> lista = new ArrayList<Regra>();
		for (Regra regra : repository.findAll()) {
			for (RegraItem item : regra.getItens()) {
				if (!item.getPergunta().trim().equals("")) {
					if (lista.indexOf(regra) == -1) {
						lista.add(regra);
					}
				}
			}
		}
		return lista;
	}

	@PostMapping("/salvaRegra")
	public Regra salvaRegra(@RequestBody Regra v) {
		Regra var = repository.save(v);
		v.setIdRegra(var.getIdRegra());
		if (v.getItens() != null) {
			for (RegraItem i : v.getItens()) {
				i.setRegra(var);
				repositoryRegraItem.save(i);
			}
		}
		if (v.getResultados() != null) {
			for (RegraItemResultado i : v.getResultados()) {
				i.setRegra(var);
				repositoryRegraItemResultado.save(i);
			}
		}
		return repository.findById(var.getIdRegra()).get();
	}

	@PostMapping("/adicionaItem/{idregra}")
	public ResponseEntity<?> adicionaValor(@PathVariable("idregra") Integer idregra, @RequestBody RegraItem item) {
		Regra regra = repository.findById(idregra).get();
		item.setRegra(regra);
		repositoryRegraItem.save(item);
		Optional<Regra> r = repository.findById(idregra);
		return ResponseEntity.ok(r.get());
	}

	@PostMapping("/adicionaItemResultado/{idregra}")
	public ResponseEntity<?> adicionaValor(@PathVariable("idregra") Integer idregra,
			@RequestBody RegraItemResultado item) {
		Regra regra = repository.findById(idregra).get();
		item.setRegra(regra);
		repositoryRegraItemResultado.save(item);
		Optional<Regra> r = repository.findById(idregra);
		return ResponseEntity.ok(r.get());
	}

	@PostMapping("/deletaItem/{idregra}")
	public Regra deleteItem(@PathVariable("idregra") Integer idregra, @RequestBody RegraItem item) {
		repositoryRegraItem.deleteById(item.getIdRegraItem());
		return repository.findById(idregra).get();
	}

	@PostMapping("/deletaItemResultado/{idregra}")
	public Regra deleteResultado(@PathVariable("idregra") Integer idregra, @RequestBody RegraItemResultado item) {
		repositoryRegraItemResultado.deleteById(item.getIdRegraItemResultado());
		return repository.findById(idregra).get();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> find(@PathVariable("id") Integer id) {
		Optional<Regra> regra = repository.findById(id);
		if (regra.isPresent()) {
			return ResponseEntity.ok(regra.get());
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Integer id) {
		Regra regra = repository.findById(id).get();
		for (RegraItem i : regra.getItens()) {
			repositoryRegraItem.deleteById(i.getIdRegraItem());
		}
		for (RegraItemResultado i : regra.getResultados()) {
			repositoryRegraItemResultado.deleteById(i.getIdRegraItemResultado());
		}
		repository.deleteById(id);
	}
}
