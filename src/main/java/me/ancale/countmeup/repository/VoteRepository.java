package me.ancale.countmeup.repository;

import me.ancale.countmeup.model.vote.Vote;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface VoteRepository extends CrudRepository<Vote, Long> {

}
