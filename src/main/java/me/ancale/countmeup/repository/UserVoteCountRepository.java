package me.ancale.countmeup.repository;

import me.ancale.countmeup.model.vote.UserVoteCount;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface UserVoteCountRepository extends CrudRepository<UserVoteCount, Long> {

    UserVoteCount findByUserId(String userId);

}
