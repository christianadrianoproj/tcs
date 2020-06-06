package com.senac.tcs.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.senac.tcs.api.domain.Interface;
import com.senac.tcs.api.domain.Variavel;

/**
*
* @author Christian
*/
public interface InterfaceRepository extends JpaRepository<Interface, Integer>{
	public Interface findByVariavel(Variavel variavel);
}
