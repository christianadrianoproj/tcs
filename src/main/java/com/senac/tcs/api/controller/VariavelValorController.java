package com.senac.tcs.api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.senac.tcs.api.domain.VariavelValor;
import com.senac.tcs.api.repository.VariavelValorRepository;

/**
*
* @author Christian
*/

@RestController
@RequestMapping("/variavel/valor")
@CrossOrigin(origins = "*")
public class VariavelValorController {
	@Autowired
	private VariavelValorRepository repository;

	@GetMapping("/{id}")
	public ResponseEntity<?> find(@PathVariable("id") Integer id) {
		Optional<VariavelValor> variavelValor = repository.findById(id);
		if (variavelValor.isPresent()) {
			return ResponseEntity.ok(variavelValor.get());
		}
		return ResponseEntity.notFound().build();
	}
}
