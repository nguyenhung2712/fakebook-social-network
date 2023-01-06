package com.ntth.socialnetwork.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ntth.socialnetwork.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	@Query(value="SELECT * from post where user_id =:#{#user_id}",nativeQuery = true)
	List<Post> getPostsWithUserID(@Param("user_id") Long userID);
	
	@Query(value = "SELECT p.post_id, p.user_id, p.content, p.image, p.published_date,p.group_id FROM post p JOIN userprofile up "
			+ "ON p.user_id = up.user_id WHERE p.content like %:keyword% or up.first_name like %:keyword% "
			+ "or up.last_name LIKE %:keyword% ", nativeQuery = true)
	List<Post> findByKeyword(@Param("keyword") String keyword);
	
	@Query(value="SELECT * from post WHERE post.user_id is not null and post.user_id in "
			+ "(SELECT DISTINCT "
			+ "CASE "
			+ "when friendship.user_id_1 = :#{#user_id} then friendship.user_id_2 "
			+ "WHEN friendship.user_id_2 = :#{#user_id} THEN friendship.user_id_1 "
			+ "else null "
			+ "END "
			+ "from friendship) ORDER by `published_date` DESC",nativeQuery = true)
	List<Post> getFriendPostByUserID(@Param("user_id") Long userID);
	
	@Query(value="SELECT * from post where group_id =:#{#group_id} ORDER by `published_date` DESC",nativeQuery = true)
	List<Post> getPostsGroup(@Param("group_id") Long group_id);

	
	
	   
    @Query(value = "SELECT DISTINCT YEAR(published_date) FROM post ORDER by YEAR(published_date) DESC"
		//	+ "WHERE l.user_id = :#{#user_id} AND p.post_id = l.post_id"
			, nativeQuery = true)
	public List<Long> getYearByPost();
    
    // DISTINCT YEAR(registered_date) as 'Year'
    @Query(value = "SELECT COUNT(post_id) as 'Total Post Published' FROM post GROUP BY YEAR(published_date)"
    			, nativeQuery = true)
    public List<Long> countPostByYear();
    
    // MonthName nên phải List<String>
    @Query(value = "SELECT DISTINCT MONTHNAME(published_date) FROM post WHERE YEAR(published_date) = :#{#year} GROUP BY MONTH(published_date)"
    			, nativeQuery = true)
    public List<String> getMonthByPost(@Param("year") Long year);
        
    @Query(value = "SELECT COUNT(post_id) as 'Total Post Published' FROM post WHERE YEAR(published_date) = :#{#year} GROUP BY MONTH(published_date)"
        			, nativeQuery = true)
    public List<Long> countPostByMonth(@Param("year") Long year);
    
    /*
	@Query(value="SELECT count(`post_id`) as TotalPosts , MONTH(`published_date`) AS MonthPublished "
			+ "FROM post group by MONTH(`published_date`)",nativeQuery = true)
    public List<?> TotalsPerMonth();
	*/
	
	
	
}
