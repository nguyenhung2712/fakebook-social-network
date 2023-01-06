package com.ntth.socialnetwork.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntth.socialnetwork.repository.StatiticsRepository;


@RestController
@RequestMapping("/api/statistics")
@CrossOrigin
public class StatiticsController {
	
	@Autowired
	StatiticsRepository statiticsRepo;
	
	/*
	@GetMapping("/totals-per-month")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<?>> TotalsPerMonth() {	
		return new ResponseEntity<List<?>>(postRepository.TotalsPerMonth(), HttpStatus.OK);
	}
	*/
	
	@GetMapping("/post-per-month/{year}")
	@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> TotalsPostPerMonth(@PathVariable("year") Long year) {	
		Optional<?> result = this.statiticsRepo.TotalsPostPerMonth(year);
		return result.map(res -> ResponseEntity.ok().body(res)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@GetMapping("/comment-per-month/{year}")
	@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> TotalsCommentsPerMonth(@PathVariable("year") Long year) {	
		Optional<?> result = this.statiticsRepo.TotalsCommentsPerMonth(year);
		return result.map(res -> ResponseEntity.ok().body(res)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@GetMapping("/reply-per-month/{year}")
	@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> TotalsReplyPerMonth(@PathVariable("year") Long year) {	
		Optional<?> result = this.statiticsRepo.TotalsReplyPerMonth(year);
		return result.map(res -> ResponseEntity.ok().body(res)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@GetMapping("/user-per-month/{year}")
	@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> TotalsUserPerMonth(@PathVariable("year") Long year) {	
		Optional<?> result = this.statiticsRepo.TotalsUserPerMonth(year);
		return result.map(res -> ResponseEntity.ok().body(res)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
}
