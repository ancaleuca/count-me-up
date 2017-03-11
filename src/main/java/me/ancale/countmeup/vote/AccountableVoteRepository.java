package me.ancale.countmeup.vote;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface AccountableVoteRepository extends CrudRepository<AccountableVote, Long> {

}
