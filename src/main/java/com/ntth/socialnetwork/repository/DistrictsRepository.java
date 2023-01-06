package com.ntth.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntth.socialnetwork.entity.Districts;

public interface DistrictsRepository extends JpaRepository<Districts, String> {
	
	
	@Query(value="SELECT * FROM districts WHERE province_code =:#{#province_code}",nativeQuery = true)
	List<Districts> getDistricts(@Param("province_code") String province_code);
	
	Districts findByCode(String code);
}
