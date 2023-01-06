package com.ntth.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ntth.socialnetwork.entity.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

	@Query(value = "SELECT c_id, status, name FROM conversation c JOIN joined_conver jc ON "
			+ "jc.conv_id = c.c_id WHERE jc.user_id = :#{#userID}", nativeQuery = true)
	List<Conversation> getConversationByUserID(@Param("userID") long userID) ;
	
	@Query(value = "SELECT COUNT(user_id) FROM joined_conver WHERE conv_id = :#{#c_id}", nativeQuery = true)
	Long getMemberQttInConv(@Param("c_id") Long c_id);
	
	@Query(value = "SELECT c_id, name, status FROM conversation "
			+ "WHERE c_id IN (SELECT conv_id FROM joined_conver WHERE user_id = :#{#user_id}) "
			+ "AND c_id IN (SELECT conv_id FROM joined_conver WHERE user_id = :#{#other_user_id}) "
			+ "AND c_id IN (SELECT conv_id FROM joined_conver GROUP BY conv_id HAVING COUNT(conv_id) = 2)", nativeQuery = true)
	Conversation getConverBy2ID(@Param("user_id") Long user_id, @Param("other_user_id") Long other_user_id);

	
	@Query(value ="SELECT u.user_id FROM user u JOIN joined_conver jc ON jc.user_id = u.user_id "
			+ "WHERE jc.user_id <> :#{#user_id} AND conv_id = :#{#cv_id}",nativeQuery = true)
	List<Long> getOtherUserID(@Param("user_id") Long user_id, @Param("cv_id") Long cv_id);
}
