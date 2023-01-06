package com.ntth.socialnetwork.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ntth.socialnetwork.entity.ConversationReply;

@Repository
public interface ConversationReplyRepository extends JpaRepository<ConversationReply,Long> {

	
	@Query(value = "SELECT * FROM conversation_reply WHERE c_id_fk=:#{#c_id} "
			+ "ORDER BY cr_id ASC", nativeQuery = true)
	List<ConversationReply> getConversationReplies(@Param("c_id") long c_id) ;

	
	@Query(value = "SELECT * FROM conversation_reply WHERE c_id_fk=:#{#c_id} "
			+ "ORDER BY cr_id DESC LIMIT 1", nativeQuery = true)
	ConversationReply getLastConversationReply(@Param("c_id") long c_id) ;
	
	
	@Query(value = "SELECT cr_id from conversation_reply "
			+ "ORDER by conversation_reply.cr_id desc limit 1", nativeQuery = true)
	Long getLastID() ;
	
	
	/*@Query(value = "SELECT Count(status) from conversation_reply "
			+ "WHERE status = 0 and c_id_fk = :#{#c_id} and  user_id_fk = :#{#user_id_fk}", nativeQuery = true)
	Long getCountNewMessage(@Param("c_id") long c_id, @Param("user_id_fk") long user_id_fk) ;*/
	
	@Query(value = "SELECT Count(status) from conversation_reply "
			+ "WHERE status = 0 and c_id_fk = :#{#c_id} and  user_id_fk <> :#{#user_id_fk}", nativeQuery = true)
	Long getCountNewMessage(@Param("c_id") long c_id, @Param("user_id_fk") long user_id_fk) ;
	
	////
	@Query(value = "SELECT Count(status) from conversation_reply "
			+ "WHERE status = 0 and user_id_fk <> :#{#user_id_fk}", nativeQuery = true)
	Long getTotalNewMessage(@Param("user_id_fk") long user_id_fk) ;
	////
	/*@Modifying
	@Transactional
	@Query(value = "UPDATE conversation_reply SET status=1 "
			+ "WHERE c_id_fk = :#{#c_id} and  user_id_fk = :#{#user_id_fk}", nativeQuery = true)
	void updateStatusReceiver(@Param("c_id") long c_id, @Param("user_id_fk") long user_id_fk);*/
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE conversation_reply SET status=1 "
			+ "WHERE c_id_fk = :#{#c_id} and  user_id_fk <> :#{#user_id_fk}", nativeQuery = true)
	void updateStatusReceiver(@Param("c_id") long c_id, @Param("user_id_fk") long user_id_fk);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE conversation_reply SET detele_status=1 WHERE cr_id = :#{#cr_id} ", nativeQuery = true)
	void deleteMessage(@Param("cr_id") long conversationReplyID);
	
	@Query(value = "SELECT DISTINCT(user_id_fk) FROM conversation_reply "
			+ "WHERE c_id_fk = :#{#c_id} AND user_id_fk <> :#{#user_id_fk}", nativeQuery = true)
	List<Long> getOthersUserID(@Param("c_id") long c_id, @Param("user_id_fk") long user_id_fk);
}

	