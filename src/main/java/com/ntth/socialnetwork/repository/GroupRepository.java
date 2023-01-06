package com.ntth.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ntth.socialnetwork.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
	
	Boolean existsByGroupName(String groupName);
	
	@Query(value = "SELECT g.group_id, created_date, group_about, group_name, user_admin FROM joinedgroup j JOIN `group` g"
			+ " ON g.group_id = j.group_id WHERE user_id = :#{#user_id}"
			, nativeQuery = true)
	public List<Group> getGroupsUserJoined(@Param("user_id") Long user_id);
	@Query(value = "SELECT EXISTS (SELECT * FROM `group` WHERE group_id = :#{#group_id} "
			+ "AND user_admin = :#{#user_admin})"
			, nativeQuery = true)
	public Long checkUserIsAdminGroup(@Param("group_id") Long group_id, @Param("user_admin") Long user_admin);
}
