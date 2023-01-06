package com.ntth.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntth.socialnetwork.entity.Wards;

public interface WardsRepository extends JpaRepository<Wards, String>{
	
	@Query(value="SELECT * FROM wards WHERE `district_code` =:#{#district_code}",nativeQuery = true)
	List<Wards> getWards(@Param("district_code") String district_code);
	
	
	Wards findByCode(String code);
}
