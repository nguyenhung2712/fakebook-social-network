package com.ntth.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ntth.socialnetwork.entity.EditHistory;

@Repository
public interface EditHistoryRepository extends JpaRepository<EditHistory, Long> {
	
	@Query(value = "SELECT cp.history_id, content, image, edited_date FROM edit_history eh JOIN changed_post cp "
			+ "ON eh.history_id = cp.history_id WHERE post_id = :#{#post_id}"
		, nativeQuery = true)
	public List<EditHistory> getEditHistoryDetails(@Param("post_id") Long post_id);
}
