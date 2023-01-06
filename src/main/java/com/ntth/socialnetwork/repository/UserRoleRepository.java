package com.ntth.socialnetwork.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ntth.socialnetwork.entity.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

	// Thêm vào là Delete được
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM user_roles WHERE user_id = :#{#user_id} AND role_id = :#{#role_id}"
			, nativeQuery = true)
	public void deleteUserRole(@Param("user_id") Long user_id, @Param("role_id") Long role_id);
	
//	@Modifying
//	@Transactional
	@Query(value = "SELECT * FROM user_roles WHERE user_id = :#{#user_id} AND role_id = :#{#role_id}"
			, nativeQuery = true)
	public Optional<UserRole> findUserRoleByID(@Param("user_id") Long user_id, @Param("role_id") Long role_id);
	
	// Thêm vào mới Insert được
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO user_roles (user_id, role_id) VALUES (:user_id, :role_id)"
	, nativeQuery = true)
	public void createUserRole(@Param("user_id") Long user_id, @Param("role_id") Long role_id);
	
	// The @Modifying annotation is used to enhance the @Query annotation so that we can execute not only SELECT queries, but also INSERT, UPDATE, DELETE, and even DDL queries.
	@Modifying
	@Transactional
	@Query(value = "UPDATE user_roles SET role_id = :#{#new_role_id} WHERE user_id = :#{#user_id} AND role_id = :#{#role_id}"
			, nativeQuery = true)
	public void updateUserRole(@Param("user_id") Long user_id, @Param("role_id") Long role_id, @Param("new_role_id") Long new_role_id);
}