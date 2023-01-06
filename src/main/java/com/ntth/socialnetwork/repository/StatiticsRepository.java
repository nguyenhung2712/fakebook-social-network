package com.ntth.socialnetwork.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ntth.socialnetwork.entity.Post;

@Repository
public interface StatiticsRepository extends JpaRepository<Post, Long>{
	@Query(value="SELECT \r\n"
			+ "    SUM(IF(month = 'Jan', total, 0)) AS 'Jan',\r\n"
			+ "    SUM(IF(month = 'Feb', total, 0)) AS 'Feb',\r\n"
			+ "    SUM(IF(month = 'Mar', total, 0)) AS 'Mar',\r\n"
			+ "    SUM(IF(month = 'Apr', total, 0)) AS 'Apr',\r\n"
			+ "    SUM(IF(month = 'May', total, 0)) AS 'May',\r\n"
			+ "    SUM(IF(month = 'Jun', total, 0)) AS 'Jun',\r\n"
			+ "    SUM(IF(month = 'Jul', total, 0)) AS 'Jul',\r\n"
			+ "    SUM(IF(month = 'Aug', total, 0)) AS 'Aug',\r\n"
			+ "    SUM(IF(month = 'Sep', total, 0)) AS 'Sep',\r\n"
			+ "    SUM(IF(month = 'Oct', total, 0)) AS 'Oct',\r\n"
			+ "    SUM(IF(month = 'Nov', total, 0)) AS 'Nov',\r\n"
			+ "    SUM(IF(month = 'Dec', total, 0)) AS 'Dec'\r\n"			
			+ "    FROM (\r\n"
			+ "SELECT DATE_FORMAT(`published_date`, \"%b\") AS month, COUNT(`post_id`) as total\r\n"
			+ "FROM post\r\n"
			+ "WHERE Year(`published_date`) = :#{#year}\r\n"
			+ "GROUP BY DATE_FORMAT(`published_date`, \"%m-%Y\")) as sub",nativeQuery = true)
    public Optional<?> TotalsPostPerMonth(@Param("year") Long year);


	@Query(value="SELECT \r\n"
			+ "    SUM(IF(month = 'Jan', total, 0)) AS 'Jan',\r\n"
			+ "    SUM(IF(month = 'Feb', total, 0)) AS 'Feb',\r\n"
			+ "    SUM(IF(month = 'Mar', total, 0)) AS 'Mar',\r\n"
			+ "    SUM(IF(month = 'Apr', total, 0)) AS 'Apr',\r\n"
			+ "    SUM(IF(month = 'May', total, 0)) AS 'May',\r\n"
			+ "    SUM(IF(month = 'Jun', total, 0)) AS 'Jun',\r\n"
			+ "    SUM(IF(month = 'Jul', total, 0)) AS 'Jul',\r\n"
			+ "    SUM(IF(month = 'Aug', total, 0)) AS 'Aug',\r\n"
			+ "    SUM(IF(month = 'Sep', total, 0)) AS 'Sep',\r\n"
			+ "    SUM(IF(month = 'Oct', total, 0)) AS 'Oct',\r\n"
			+ "    SUM(IF(month = 'Nov', total, 0)) AS 'Nov',\r\n"
			+ "    SUM(IF(month = 'Dec', total, 0)) AS 'Dec'\r\n"			
			+ "    FROM (\r\n"
			+ "SELECT DATE_FORMAT(`date_comment`, \"%b\") AS month, COUNT(`post_comment_id`) as total\r\n"
			+ "FROM post_comment\r\n"
			+ "WHERE Year(`date_comment`) = :#{#year}\r\n"
			+ "GROUP BY DATE_FORMAT(`date_comment`, \"%m-%Y\")) as sub",nativeQuery = true)
    public Optional<?> TotalsCommentsPerMonth(@Param("year") Long year);
	
	@Query(value="SELECT \r\n"
			+ "    SUM(IF(month = 'Jan', total, 0)) AS 'Jan',\r\n"
			+ "    SUM(IF(month = 'Feb', total, 0)) AS 'Feb',\r\n"
			+ "    SUM(IF(month = 'Mar', total, 0)) AS 'Mar',\r\n"
			+ "    SUM(IF(month = 'Apr', total, 0)) AS 'Apr',\r\n"
			+ "    SUM(IF(month = 'May', total, 0)) AS 'May',\r\n"
			+ "    SUM(IF(month = 'Jun', total, 0)) AS 'Jun',\r\n"
			+ "    SUM(IF(month = 'Jul', total, 0)) AS 'Jul',\r\n"
			+ "    SUM(IF(month = 'Aug', total, 0)) AS 'Aug',\r\n"
			+ "    SUM(IF(month = 'Sep', total, 0)) AS 'Sep',\r\n"
			+ "    SUM(IF(month = 'Oct', total, 0)) AS 'Oct',\r\n"
			+ "    SUM(IF(month = 'Nov', total, 0)) AS 'Nov',\r\n"
			+ "    SUM(IF(month = 'Dec', total, 0)) AS 'Dec'\r\n"			
			+ "    FROM (\r\n"
			+ "SELECT DATE_FORMAT(`date_reply`, \"%b\") AS month, COUNT(`comment_reply_id`) as total\r\n"
			+ "FROM comment_reply\r\n"
			+ "WHERE Year(`date_reply`) = :#{#year}\r\n"
			+ "GROUP BY DATE_FORMAT(`date_reply`, \"%m-%Y\")) as sub",nativeQuery = true)
    public Optional<?> TotalsReplyPerMonth(@Param("year") Long year);
	
	@Query(value="SELECT \r\n"
			+ "    SUM(IF(month = 'Jan', total, 0)) AS 'Jan',\r\n"
			+ "    SUM(IF(month = 'Feb', total, 0)) AS 'Feb',\r\n"
			+ "    SUM(IF(month = 'Mar', total, 0)) AS 'Mar',\r\n"
			+ "    SUM(IF(month = 'Apr', total, 0)) AS 'Apr',\r\n"
			+ "    SUM(IF(month = 'May', total, 0)) AS 'May',\r\n"
			+ "    SUM(IF(month = 'Jun', total, 0)) AS 'Jun',\r\n"
			+ "    SUM(IF(month = 'Jul', total, 0)) AS 'Jul',\r\n"
			+ "    SUM(IF(month = 'Aug', total, 0)) AS 'Aug',\r\n"
			+ "    SUM(IF(month = 'Sep', total, 0)) AS 'Sep',\r\n"
			+ "    SUM(IF(month = 'Oct', total, 0)) AS 'Oct',\r\n"
			+ "    SUM(IF(month = 'Nov', total, 0)) AS 'Nov',\r\n"
			+ "    SUM(IF(month = 'Dec', total, 0)) AS 'Dec'\r\n"			
			+ "    FROM (\r\n"
			+ "SELECT DATE_FORMAT(`registered_date`, \"%b\") AS month, COUNT(`user_id`) as total\r\n"
			+ "FROM user\r\n"
			+ "WHERE Year(`registered_date`) = :#{#year}\r\n"
			+ "GROUP BY DATE_FORMAT(`registered_date`, \"%m-%Y\")) as sub",nativeQuery = true)
    public Optional<?> TotalsUserPerMonth(@Param("year") Long year);
}
