package com.senac.tcs.api.controller;

import java.util.List;
import java.util.Optional;

import com.senac.tcs.api.domain.VariavelValor;
import com.senac.tcs.api.repository.VariavelValorRepository;
import com.senac.tcs.api.domain.TipoVariavel;
import com.senac.tcs.api.domain.Variavel;
import com.senac.tcs.api.repository.VariavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/variavel")
@CrossOrigin(origins = "*")
public class VariavelController {
	@Autowired
	private VariavelRepository repository;

	@Autowired
	private VariavelValorRepository repositoryVariavelValor;

	@GetMapping
	public List<Variavel> findAll() {		
		return repository.findAll();
	}

	@PostMapping("/salvaVariavel")
	public Variavel salvaVariavel(@RequestBody Variavel v) {
		return repository.save(v);
	}

	@PostMapping("/adicionaValor/{idvariavel}")
	public ResponseEntity<?> adicionaValor(@PathVariable("idvariavel") Integer idvariavel, @RequestBody VariavelValor valor) {
		Variavel v = repository.findById(idvariavel).get();
		if (v.getTipoVariavel() == TipoVariavel.Numerica.getTipo()) {
			if (valor.getValor().indexOf(";") != -1) {
				String[] valores = valor.getValor().split(";");
				for (int i = valores.length - 1; i >= 0; i--) {
					VariavelValor vv = new VariavelValor();
					vv.setVariavel(v);
					vv.setValor(valores[i]);
					repositoryVariavelValor.save(vv);
				}
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(String.format("Variavel %s : Valor inaceitável de intervalo. Deixe os valores em branco ou coloqueo um intervalo do tipo \"min;max\".",v.getNome()));
			}

		} else {
			if (valor.getValor().trim().equals("")) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(String.format("Variavel %s : Informe o valor.",v.getNome()));		
			}
			else {
				valor.setVariavel(v);
				repositoryVariavelValor.save(valor);
			}
		} 
		Optional<Variavel> var = repository.findById(v.getIdVariavel());
		return ResponseEntity.ok(var.get());
	}

	@PostMapping("/deletaValor/{idvariavel}")
	public Variavel deleteItem(@PathVariable("idvariavel") Integer idvariavel, @RequestBody VariavelValor valor) {
		repositoryVariavelValor.deleteById(valor.getIdVariavelValor());
		return repository.findById(idvariavel).get();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> find(@PathVariable("id") Integer id) {
		Optional<Variavel> variavel = repository.findById(id);
		if (variavel.isPresent()) {
			return ResponseEntity.ok(variavel.get());
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Integer id) {
		Variavel variavel = repository.findById(id).get();
		for (VariavelValor i : variavel.getValores()) {
			repositoryVariavelValor.deleteById(i.getIdVariavelValor());
		}
		repository.deleteById(id);
	}

}
