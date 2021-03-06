package com.senac.tcs.api.controller;

import java.util.Optional;

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

import com.senac.tcs.api.domain.Foto;
import com.senac.tcs.api.repository.FotoRepository;

/**
*
* @author Christian
*/

@RestController
@RequestMapping("/foto")
@CrossOrigin(origins = "*")
public class FotoController {
	@Autowired
	private FotoRepository repository;


	@PostMapping("/salvaFoto")
	public Foto salvaVariavel(@RequestBody Foto v) {
		return repository.save(v);
	}


	@GetMapping("/{id}")
	public ResponseEntity<?> find(@PathVariable("id") Integer id) {
		Optional<Foto> image = repository.findById(id);
		if (image.isPresent()) {
			return ResponseEntity.ok(image.get());
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/deletaFoto/{id}")
	public void delete(@PathVariable("id") Integer id) {
		repository.deleteById(id);
	}

}

