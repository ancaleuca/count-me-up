package me.ancale.countmeup.counter;

import com.google.common.annotations.VisibleForTesting;
import me.ancale.countmeup.model.vote.VoteCountsDto;
import me.ancale.countmeup.repository.AccountableVoteRepository;
import me.ancale.countmeup.repository.VoteCountPerCandidateQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class VoteCounter {

    private final Map<String, Long> accountableVotesPerCandidate;
    private final AtomicLong lastReadTimestamp;

    private final AccountableVoteRepository accountableVoteRepository;
    private final Clock clock;

    @Autowired
    public VoteCounter(AccountableVoteRepository accountableVoteRepository) {
        this(accountableVoteRepository, Clock.systemUTC());
    }

    @VisibleForTesting
    public VoteCounter(AccountableVoteRepository accountableVoteRepository, Clock clock) {
        this.accountableVoteRepository = accountableVoteRepository;
        this.clock = clock;
        this.accountableVotesPerCandidate = new ConcurrentHashMap<>();
        this.lastReadTimestamp = new AtomicLong();
    }

    public synchronized VoteCountsDto countAccountable() {
        long currentTimestamp = clock.millis();
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
        return VoteCountsDto.from(accountableVotesPerCandidate);
    }


}
