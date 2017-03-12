package me.ancale.countmeup.repository;

import me.ancale.countmeup.model.vote.AccountableVote;
import me.ancale.countmeup.model.vote.VotePerCandidate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface AccountableVoteRepository extends CrudRepository<AccountableVote, Long> {

    @Query(value = "select new me.ancale.countmeup.model.vote.VotePerCandidate(candidateId, count(*)) " +
            "from AccountableVote " +
            "where timestamp > :from and timestamp <= :to " +
            "group by candidateId")
    List<VotePerCandidate> countVotesPerCandidate(@Param("from") long from, @Param("to") long to);

    @Query(value = "select new me.ancale.countmeup.model.vote.VotePerCandidate(candidateId, count(*)) " +
            "from AccountableVote " +
            "where timestamp <= :timestamp " +
            "group by candidateId")
    List<VotePerCandidate> countVotesPerCandidate(@Param("timestamp") long timestamp);
}
