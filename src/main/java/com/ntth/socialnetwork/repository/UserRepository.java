package com.ntth.socialnetwork.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ntth.socialnetwork.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    
    User findByEmailIgnoreCase(String email);
    
    @Query(value = "SELECT u.user_id, email, password, registered_date, username, is_enabled "
    		+ "FROM user u JOIN confirmation_token ct ON ct.user_id = u.user_id "
    		+ "WHERE confirmation_token = :#{#token}",nativeQuery = true)
    User findByConfirmToken(@Param("token")String token);
    
    @Query(value = "SELECT u.user_id, email, password, registered_date, username, is_enabled "
			+ "FROM joined_conver jc JOIN user u ON jc.user_id = u.user_id "
			+ "WHERE jc.user_id <> :#{#user_id} AND jc.conv_id = :#{#conv_id}",nativeQuery = true)
	List<User> getOtherConvMembers(@Param("conv_id") long conv_id, @Param("user_id") long user_id);
    
    	@Query(value = "SELECT DISTINCT YEAR(registered_date) FROM user ORDER by YEAR(registered_date) DESC"	
    			, nativeQuery = true)
    	public List<Long> getYearByUser();
        
        // DISTINCT YEAR(registered_date) as 'Year'
        @Query(value = "SELECT COUNT(user_id) as 'Total User Register' FROM user GROUP BY YEAR(registered_date)"
        			, nativeQuery = true)
        public List<Long> countUserByYear();
        
        // MonthName nên phải List<String> 
        @Query(value = "SELECT DISTINCT MONTHNAME(registered_date) FROM user WHERE YEAR(registered_date) = :#{#year} GROUP BY MONTH(registered_date)"
        			, nativeQuery = true)
        public List<String> getMonthByUser(@Param("year") Long year);
            
        @Query(value = "SELECT COUNT(user_id) as 'Total User Register' FROM user WHERE YEAR(registered_date) = :#{#year} GROUP BY MONTH(registered_date)"
            			, nativeQuery = true)
        public List<Long> countUserByMonth(@Param("year") Long year);
        
        @Query(value = "SELECT username FROM user"
    			, nativeQuery = true)
        public List<String> getListUserName();
        
        @Query(value = "SELECT name FROM role"
     			, nativeQuery = true)
        public List<String> getListRole();
        
        
        

    	@Query(value = "SELECT DISTINCT YEAR(`date_comment`) FROM post_comment"
    			, nativeQuery = true)
    	public List<Long> getTimeComment();
}