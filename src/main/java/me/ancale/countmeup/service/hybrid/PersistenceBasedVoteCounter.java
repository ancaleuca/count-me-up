package me.ancale.countmeup.service.hybrid;

import me.ancale.countmeup.model.vote.AccountableVote;
import me.ancale.countmeup.model.vote.AccountableVoteCountSummary;
import me.ancale.countmeup.model.vote.UserVoteCount;
import me.ancale.countmeup.model.vote.Vote;
import me.ancale.countmeup.repository.AccountableVoteRepository;
import me.ancale.countmeup.repository.UserVoteCountRepository;
import me.ancale.countmeup.repository.VoteCountPerCandidateQueryResult;
import me.ancale.countmeup.repository.VoteRepository;
import me.ancale.countmeup.service.VoteCounter;
import me.ancale.countmeup.service.VoteStore;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Primary
@Component
public class PersistenceBasedVoteCounter implements VoteCounter, VoteStore {

    private final Map<String, Long> accountableVotesPerCandidate;
    private final AtomicLong lastReadTimestamp;

    private final AccountableVoteRepository accountableVoteRepository;
    private final VoteRepository voteRepository;
    private final UserVoteCountRepository userVoteCountRepository;

    public PersistenceBasedVoteCounter(AccountableVoteRepository accountableVoteRepository, VoteRepository voteRepository,
                                       UserVoteCountRepository userVoteCountRepository) {
        this.accountableVoteRepository = accountableVoteRepository;
        this.voteRepository = voteRepository;
        this.userVoteCountRepository = userVoteCountRepository;
        this.accountableVotesPerCandidate = new ConcurrentHashMap<>();
        this.lastReadTimestamp = new AtomicLong();
    }

    @Override
    public synchronized AccountableVoteCountSummary countAccountable() {
        long currentTimestamp = Instant.now().toEpochMilli();
        Map<String, Long> latestVotes = accountableVoteRepository.countVotesPerCandidate(lastReadTimestamp.get(), currentTimestamp)
                .stream().collect(Collectors.toMap(VoteCountPerCandidateQueryResult::getCandidateId, VoteCountPerCandidateQueryResult::getCount));

        for (String candidateId: latestVotes.keySet()) {
            if (!accountableVotesPerCandidate.containsKey(candidateId)) {
                accountableVotesPerCandidate.put(candidateId, 0L);
            }
            accountableVotesPerCandidate.put(candidateId,
                    accountableVotesPerCandidate.get(candidateId) + latestVotes.get(candidateId));
        }

        lastReadTimestamp.set(currentTimestamp);
        return new AccountableVoteCountSummary(accountableVotesPerCandidate);
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
