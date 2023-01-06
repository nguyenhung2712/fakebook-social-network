package com.ntth.socialnetwork.controller;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntth.socialnetwork.entity.Districts;
import com.ntth.socialnetwork.entity.Location;
import com.ntth.socialnetwork.entity.Provinces;
import com.ntth.socialnetwork.entity.Wards;
import com.ntth.socialnetwork.repository.DistrictsRepository;
import com.ntth.socialnetwork.repository.LocationRepository;
import com.ntth.socialnetwork.repository.ProvincesRepository;
import com.ntth.socialnetwork.repository.WardsRepository;

@RestController
@CrossOrigin
@RequestMapping("/api/location")
public class LocationController {

	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private ProvincesRepository provincesRepository;
	
	@Autowired
	private DistrictsRepository districtsRepository;
	
	@Autowired
	private WardsRepository wardsRepository;
	
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<Location> createComment(@RequestBody Location location) throws URISyntaxException {
		Location result = locationRepository.save(location);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<Location>> getAll() {
		try {
			List<Location> allList = locationRepository.findAll();
			return new ResponseEntity<List<Location>>(allList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/all-provinces")
	public ResponseEntity<List<Provinces>> getAllProvinces() {
		try {
			List<Provinces> allList = provincesRepository.findAll();
			return new ResponseEntity<List<Provinces>>(allList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/get-districts/{province_code}")
	public ResponseEntity<List<Districts>> getDistricts(@PathVariable("province_code") String province_code) {
		try {
			List<Districts> allList = districtsRepository.getDistricts(province_code);
			return new ResponseEntity<List<Districts>>(allList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
	@GetMapping("/get-wards/{district_code}")
	public ResponseEntity<List<Wards>> getWards(@PathVariable("district_code") String district_code) {
		try {
			List<Wards> allList = wardsRepository.getWards(district_code);
			return new ResponseEntity<List<Wards>>(allList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}