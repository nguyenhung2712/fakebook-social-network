package com.ntth.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ntth.socialnetwork.entity.Reply;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long>{

	@Query(value="SELECT * from comment_reply WHERE comment_reply.post_comment_id =:#{#comment_id} order by date_reply DESC",nativeQuery = true)
	List<Reply> SelectRepliesOnComment(@Param("comment_id") Long commentID);
}
