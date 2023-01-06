package com.ntth.socialnetwork.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ntth.socialnetwork.entity.ConverJoinDetails;
import com.ntth.socialnetwork.entity.Conversation;
import com.ntth.socialnetwork.entity.User;

@Repository
public interface ConvJoinDetailsRepository extends JpaRepository<ConverJoinDetails, Long> {
	@Transactional
	Optional<ConverJoinDetails> findByConverAndUser(Conversation conver, User user);
}
