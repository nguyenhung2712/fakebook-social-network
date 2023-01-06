package com.ntth.socialnetwork.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ntth.socialnetwork.entity.Group;
import com.ntth.socialnetwork.entity.GroupJoinDetails;
import com.ntth.socialnetwork.entity.User;

@Repository
public interface GroupJoinDetailsRepository extends JpaRepository<GroupJoinDetails, Long> {
	@Query(value = "SELECT COUNT(user_id) FROM joinedgroup WHERE group_id = :#{#group_id}"
			, nativeQuery = true)
	public Long getTotalMemberOfGroup(@Param("group_id") Long group_id);
	
	@Query(value = "SELECT"
			+ "	CASE"
			+ "	    WHEN EXISTS ( SELECT * FROM joinedgroup where user_id = :#{#user_id} AND group_id = :#{#group_id} )THEN 1"
			+ "	    ELSE 0"
			+ "	END;"
			, nativeQuery = true)
	public Long checkUserJoinedGroup(@Param("group_id") Long group_id, @Param("user_id") Long user_id);
	
	@Transactional
	public void deleteByGroupAndUser(Group group, User user);
}