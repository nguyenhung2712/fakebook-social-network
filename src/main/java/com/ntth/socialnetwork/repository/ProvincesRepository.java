package com.ntth.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntth.socialnetwork.entity.Provinces;


public interface ProvincesRepository extends JpaRepository<Provinces, String>  {

	Provinces findByCode(String code);
}
