package me.ancale.countmeup.repository;

import me.ancale.countmeup.model.vote.Vote;
import me.ancale.countmeup.model.vote.VotePerCandidate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface VoteRepository extends CrudRepository<Vote, Long> {

    @Query(value = "select new me.ancale.countmeup.model.vote.VotePerCandidate(candidateId, count(*)) from Vote where timestamp <= :timestamp group by candidateId")
    List<VotePerCandidate> countVotesPerCandidate(@Param("timestamp") long timestamp);

}
