package com.ntth.socialnetwork.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ntth.socialnetwork.entity.User;
import com.ntth.socialnetwork.entity.UserProfile;


@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
	
	@Query(value="SELECT * from userprofile where user_id =:#{#user_id}",nativeQuery = true)
	UserProfile getProfileWithUserID(@Param("user_id") Long userID);
	
	@Query(value = "SELECT * FROM userprofile up WHERE up.first_name LIKE %:keyword% OR up.last_name LIKE %:keyword% "
			+ "OR up.about like %:keyword% ", nativeQuery = true)
	List<UserProfile> findByKeyword(@Param("keyword") String keyword);
	
	//get all list friend
	@Query(value = "SELECT * FROM userprofile up JOIN friendship f ON up.user_id = f.user_id_1 "
			+ "WHERE f.user_id_2 = :userID and status_id = 2 "
			+ "UNION SELECT *  FROM userprofile up JOIN friendship f ON up.user_id = f.user_id_2 "
			+ "WHERE f.user_id_1 = :userID and status_id = 2",nativeQuery = true)
	List<UserProfile> getListFriend(@Param("userID") long userId);
	
	//get list people send add request to userID
	@Query(value = "SELECT * FROM userprofile up JOIN friendship f ON up.user_id = f.user_id_1 "
			+ "WHERE f.user_id_2 = :userID and f.status_id = 1", nativeQuery = true)
	List<UserProfile> getListRequestFriendToUser(@Param("userID")  long userId);
	
	//get list user request friend
	@Query(value = "SELECT * FROM userprofile up JOIN friendship f ON up.user_id = f.user_id_2 "
			+ "WHERE f.user_id_1 = :userID and f.status_id = 1", nativeQuery = true)
	List<UserProfile> getListUserRequestFriend(@Param("userID") long userId);
	
	//get list in friendship with user
	//get all list friend
	@Query(value = "SELECT * FROM userprofile up JOIN friendship f ON up.user_id = f.user_id_1 "
			+ "WHERE f.user_id_2 = :userID "
			+ "UNION SELECT *  FROM userprofile up JOIN friendship f ON up.user_id = f.user_id_2 "
			+ "WHERE f.user_id_1 = :userID ",nativeQuery = true)
	List<UserProfile> getListFriendShip(@Param("userID") long userId);
	
	@Query(value = "SELECT * from userprofile WHERE userprofile.user_id is not null and userprofile.user_id in"
			+ "(SELECT DISTINCT"
			+ "CASE"
			+ "when friendship.user_id_1 =:#{#user_id} then friendship.user_id_2"
			+ "WHEN friendship.user_id_2 =:#{#user_id} THEN friendship.user_id_1"
			+ "else null"
			+ "END"
			+ "from friendship)",nativeQuery = true)
	List<UserProfile> getFriendProfile(@Param("user_id") long userId);
	
	Optional<UserProfile> findByUser(User user);
	
	@Query(value = "SELECT * FROM userprofile WHERE user_id = :#{#user_id}",nativeQuery = true)
	UserProfile findByUserID(@Param("user_id") long userId);
	
	@Query(value = "SELECT userprofile_id, avatar, background, dob, first_name, last_name, "
			+ "gender, update_date, about, location_id, up.user_id "
			+ "FROM (joined_conver jc JOIN user u ON u.user_id = jc.user_id) "
			+ "JOIN userprofile up ON up.user_id = u.user_id WHERE conv_id = :#{#conv_id} AND jc.user_id <> :#{#user_id}", nativeQuery = true)
	List<UserProfile> getProfileOfOtherConvMembers(@Param("conv_id") long conv_id, @Param("user_id") long user_id);
	

	@Query(value = "SELECT userprofile_id, avatar, background, dob, first_name, last_name, gender, update_date, about, location_id, up.user_id \r\n"
			+ "FROM (joinedgroup jg JOIN user u ON jg.user_id = u.user_id) \r\n"
			+ "JOIN userprofile up ON up.user_id = u.user_id WHERE group_id = :#{#group_id} AND u.user_id <> :#{#user_id}", nativeQuery = true)
	List<UserProfile> getProfileOfGroupMembers(@Param("group_id") Long group_id, @Param("user_id") Long user_id);

	
	
	
	

	@Query(value = "SELECT * FROM userprofile up JOIN friendship f ON up.user_id = f.user_id_2 "
			+ "WHERE f.user_id_1 = :#{#user_id} AND status_id = 2 AND up.user_id NOT IN ( "
			+ "    SELECT user_id FROM joined_conver WHERE conv_id = :#{#conv_id} "
			+ ") "
			+ "UNION "
			+ "SELECT * FROM userprofile up JOIN friendship f ON up.user_id = f.user_id_1 "
			+ "WHERE f.user_id_2 = :#{#user_id} AND status_id = 2 AND up.user_id NOT IN ( "
			+ "    SELECT user_id FROM joined_conver WHERE conv_id = :#{#conv_id} "
			+ ")", nativeQuery = true)
	List<UserProfile> getProfileOfFriendNotJoinedConv(
			@Param("conv_id") long conv_id, 
			@Param("user_id") long user_id
	);

	

}