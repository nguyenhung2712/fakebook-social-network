package com.ntth.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ntth.socialnetwork.entity.LikePost;

@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long> {
	@Query(value = "SELECT COUNT(user_id) FROM likepost WHERE post_id = :#{#post_id}"
			, nativeQuery = true)
	public Long getTotalLike(@Param("post_id") Long post_id);

	// Thêm vào là Delete được
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM likepost WHERE post_id = :#{#post_id} AND user_id = :#{#user_id}"
			, nativeQuery = true)
	public void dislikePost(@Param("post_id") Long post_id, @Param("user_id") Long user_id);
	

	@Query(value = "SELECT * FROM likepost WHERE user_id = :#{#user_id} and post_id = :#{#post_id}"
			, nativeQuery = true)
	public LikePost getPostsUserLiked(@Param("user_id") Long user_id,@Param("post_id") Long post_id);
	
//	@Query(value = "SELECT j.group_id, created_date, group_about, group_name FROM joinedgroup j JOIN `group` g "
//			+ "WHERE user_id = :#{#user_id} AND g.group_id = j.group_id"
//			, nativeQuery = true)
//	public List<Post> getGroupsUserJoined(@Param("user_id") Long user_id);
}