package me.ancale.countmeup.votecounter;

import me.ancale.countmeup.vote.AccountableVoteRepository;
import me.ancale.countmeup.vote.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Primary
public class PersistenceBasedVoteCounter implements VoteCounter {

    private final VoteRepository voteRepository;
    private final AccountableVoteRepository accountableVoteRepository;

    @Autowired
    public PersistenceBasedVoteCounter(VoteRepository voteRepository,
                                       AccountableVoteRepository accountableVoteRepository) {
        this.voteRepository = voteRepository;
        this.accountableVoteRepository = accountableVoteRepository;
    }

    @Override
    public VoteCountSummary count() {
        Instant timeNow = Instant.now();
        Map<String, Long> totalVotesPerCandidate = voteRepository.countVotesPerCandidate(timeNow.toEpochMilli()).stream()
                .collect(Collectors.toMap(VotePerCandidate::getCandidateId, VotePerCandidate::getCount));
        Map<String, Long> accountableVotesPerCandidate = accountableVoteRepository.countVotesPerCandidate(timeNow.toEpochMilli()).stream()
                .collect(Collectors.toMap(VotePerCandidate::getCandidateId, VotePerCandidate::getCount));
        return new VoteCountSummary(totalVotesPerCandidate, accountableVotesPerCandidate);
    }
}
