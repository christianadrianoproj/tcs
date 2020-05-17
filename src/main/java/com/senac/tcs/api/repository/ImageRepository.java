package com.senac.tcs.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senac.tcs.api.domain.Image;

/**
*
* @author Christian
*/
public interface ImageRepository extends JpaRepository<Image, Integer> {

}

