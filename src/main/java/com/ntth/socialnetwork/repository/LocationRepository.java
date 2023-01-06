package com.ntth.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntth.socialnetwork.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

}