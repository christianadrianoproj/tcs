package com.senac.tcs.api.repository;


import com.senac.tcs.api.domain.RegraItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

/**
*
* @author Christian
*/
public interface RegraItemRepository extends JpaRepository<RegraItem, Integer>{
	List<RegraItem> findAllOrderBy(Sort sort);
}
