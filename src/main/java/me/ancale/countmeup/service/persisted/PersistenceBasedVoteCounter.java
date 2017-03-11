package me.ancale.countmeup.service.persisted;

import me.ancale.countmeup.model.vote.*;
import me.ancale.countmeup.repository.AccountableVoteRepository;
import me.ancale.countmeup.repository.UserVoteCountRepository;
import me.ancale.countmeup.repository.VoteRepository;
import me.ancale.countmeup.service.VoteCounter;
import me.ancale.countmeup.service.VoteStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Primary
public class PersistenceBasedVoteCounter implements VoteCounter, VoteStore {

    private final VoteRepository voteRepository;
    private final AccountableVoteRepository accountableVoteRepository;
    private final UserVoteCountRepository userVoteCountRepository;

    @Autowired
    public PersistenceBasedVoteCounter(VoteRepository voteRepository,
                                       AccountableVoteRepository accountableVoteRepository,
                                       UserVoteCountRepository userVoteCountRepository) {
        this.voteRepository = voteRepository;
        this.accountableVoteRepository = accountableVoteRepository;
        this.userVoteCountRepository = userVoteCountRepository;
    }

    @Override
    public VoteCountSummary count() {
        Instant timeNow = Instant.now();
        Map<String, Long> totalVotesPerCandidate = voteRepository.countVotesPerCandidate(timeNow.toEpochMilli())
                .stream()
                .collect(Collectors.toMap(VotePerCandidate::getCandidateId, VotePerCandidate::getCount));
        Map<String, Long> accountableVotesPerCandidate = accountableVoteRepository.countVotesPerCandidate(timeNow.toEpochMilli())
                .stream()
                .collect(Collectors.toMap(VotePerCandidate::getCandidateId, VotePerCandidate::getCount));
        return new VoteCountSummary(totalVotesPerCandidate, accountableVotesPerCandidate);
    }

    @Override
    @Transactional
    public void addVote(Vote vote) {
        voteRepository.save(vote);
        UserVoteCount existingCount = userVoteCountRepository.findByUserId(vote.getUserId());
        if (existingCount == null) {
            existingCount = new UserVoteCount(vote.getUserId(), 0);
        }
        existingCount.setVoteCount(existingCount.getVoteCount() + 1);
        userVoteCountRepository.save(existingCount);
        if (existingCount.getVoteCount() <= VoteCounter.MAX_VOTES_PER_USER) {
            AccountableVote accountableVote = new AccountableVote(vote);
            accountableVoteRepository.save(accountableVote);
        }
    }
}
