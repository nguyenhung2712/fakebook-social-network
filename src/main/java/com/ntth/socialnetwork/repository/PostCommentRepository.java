package com.ntth.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ntth.socialnetwork.entity.PostComment;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
	
	@Query(value="SELECT * from post_comment WHERE post_comment.post_id =:#{#post_id} order by date_comment DESC",nativeQuery = true)
	List<PostComment> SelectCommentsOnPost(@Param("post_id") Long postID);
}
