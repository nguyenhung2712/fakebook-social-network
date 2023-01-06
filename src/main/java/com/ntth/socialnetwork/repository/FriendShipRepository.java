package com.ntth.socialnetwork.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ntth.socialnetwork.entity.FriendShip;


@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {
	
	@Query(value = "SELECT * FROM friendship f WHERE (f.user_id_1 = :userId1 AND f.user_id_2 = :userId2) "
			+ "or (f.user_id_1 = :userId2 AND f.user_id_2 = :userId1)", nativeQuery = true)
	Optional<FriendShip> getByIdTwoUser(@Param("userId1") long userId1, @Param("userId2") long userId2) ;
	
	//delete friendship
	@Query(value = "DELETE FROM friendship WHERE (user_id_1 = :userId1 AND user_id_2 = :userId2) "
			+ "or (user_id_1 = :userId2 AND user_id_2 = :userId1)", nativeQuery = true)
	void deleteFriendShip(@Param("userId1") long userId1, @Param("userId2") long userId2);
}
	
	
