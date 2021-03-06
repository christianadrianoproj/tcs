package com.senac.tcs.api.controller;

import java.util.ArrayList;
import java.util.List;
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

import com.senac.tcs.api.domain.Interface;
import com.senac.tcs.api.domain.Regra;
import com.senac.tcs.api.domain.RegraItem;
import com.senac.tcs.api.domain.Variavel;
import com.senac.tcs.api.repository.InterfaceRepository;
import com.senac.tcs.api.repository.RegraRepository;
import com.senac.tcs.api.repository.VariavelRepository;

/**
*
* @author Christian
*/

@RestController
@RequestMapping("/interface")
@CrossOrigin(origins = "*")
public class InterfaceController {
	@Autowired
	private InterfaceRepository repository;
	
	@Autowired
	private VariavelRepository repositoryVariavel;
	
	@Autowired
	private RegraRepository repositoryRegra;

	@GetMapping
	public List<Interface> findAll() {
		return repository.findAll();
	}
	
	@GetMapping("/interfacesComRegras")
	public List<Interface> findInterfacesByRegra() {
		List<Interface> listaRetorno = new ArrayList<Interface>();
		
		List<Interface> lista = repository.findAll();
		for (Interface i : lista) {
			for (Regra r : repositoryRegra.findAll()) {
				for (RegraItem item : r.getItens()) {					
			      if (i.getVariavel().getIdVariavel() == item.getVariavel().getIdVariavel()) {
			    	  if (listaRetorno.indexOf(i) == -1) {
			    		  listaRetorno.add(i);
			    	  }
			      }
				}
			}
		}
		return listaRetorno;
	}

	@PostMapping("/salvaInterface")
	public Interface salvaInterface(@RequestBody Interface v) {
		Interface var = repository.save(v);	
		return repository.findById(var.getIdInterface()).get();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> find(@PathVariable("id") Integer id) {
		Optional<Interface> inter = repository.findById(id);
		if (inter.isPresent()) {
			return ResponseEntity.ok(inter.get());
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Integer id) {
		repository.deleteById(id);
	}
	
	@GetMapping("/variavel/{idVariavel}")
	public Interface getVariavel(@PathVariable("idVariavel") Integer idVariavel) {
		Variavel variavel = repositoryVariavel.findById(idVariavel).get();
		return repository.findByVariavel(variavel);
	}
}

