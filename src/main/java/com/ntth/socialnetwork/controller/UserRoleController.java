package com.ntth.socialnetwork.controller;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntth.socialnetwork.entity.Role;
import com.ntth.socialnetwork.entity.UserRole;
import com.ntth.socialnetwork.repository.RoleRepository;
import com.ntth.socialnetwork.repository.UserRepository;
import com.ntth.socialnetwork.repository.UserRoleRepository;

@RestController
@CrossOrigin("http://localhost:3000/")
@RequestMapping("/api/user-role")
public class UserRoleController {
	@Autowired
	UserRoleRepository userRoleRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;
	

	@GetMapping("/get-all-roles")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	Collection<Role> getAllRole() {
		return this.roleRepo.findAll();
	}
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	Collection<UserRole> getAllUserRole() {
		return this.userRoleRepo.findAll();
	}
	@GetMapping("/{user_id}/{role_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<?> getUserRoleById(@PathVariable("user_id") Long user_id, @PathVariable("role_id") Long role_id, UserRole userRole) {
		Optional<UserRole> result = this.userRoleRepo.findUserRoleByID(user_id, role_id);
		return ResponseEntity.ok().body(result); 
	}
	
	
	@PostMapping("/add/{user_id}/{role_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	void createUserRole(@PathVariable("user_id") Long user_id, @PathVariable("role_id") Long role_id) throws URISyntaxException {
		try {
			userRoleRepo.createUserRole(user_id, role_id);
		}
		catch (Exception e) {
			System.out.print(e);
		}
	}

	@PutMapping("/update/{user_id}/{role_id}/{new_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public void updateUserRole(@PathVariable("user_id") Long user_id, @PathVariable("role_id") Long role_id, @PathVariable("new_id") Long newrole_id){	
		try {
			userRoleRepo.updateUserRole(user_id,role_id,newrole_id);		
		}
		catch(Exception e) {
			System.out.print(e);
		}
	
}
	
	// Cần thêm @Modifying và @Transactional bên Repository mới Delete được
	// Truyền vào user_id và role_id
	@DeleteMapping("/remove/{user_id}/{role_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public void deleteUserRole(@PathVariable("user_id") Long user_id, @PathVariable("role_id") Long role_id) {
		try 
		{
			userRoleRepo.deleteUserRole(user_id, role_id);
		} 
		catch (Exception e) {
			System.out.print(e);

		}
		}
}