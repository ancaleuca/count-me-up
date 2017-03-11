package me.ancale.countmeup.votecounter;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface UserVoteCountRepository extends CrudRepository<UserVoteCount, Long> {

    UserVoteCount findByUserId(String userId);

}
