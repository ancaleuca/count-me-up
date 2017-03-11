package me.ancale.countmeup.service.inmemory;

import com.google.common.annotations.VisibleForTesting;
import me.ancale.countmeup.model.vote.Vote;
import me.ancale.countmeup.service.VoteStore;
import me.ancale.countmeup.model.vote.VoteCountSummary;
import me.ancale.countmeup.service.VoteCounter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryVoteCounter implements VoteCounter, VoteStore {

    private final Map<String, Long> votesPerCandidate;
    private final Map<String, Long> accountableVotesPerCandidate;
    private final Map<String, Long> votesPerUser;

    public InMemoryVoteCounter() {
        this.votesPerCandidate = new ConcurrentHashMap<>();
        this.accountableVotesPerCandidate = new ConcurrentHashMap<>();
        this.votesPerUser = new ConcurrentHashMap<>();
    }

    @VisibleForTesting
    public InMemoryVoteCounter(List<Vote> votes) {
        this();
        for (Vote vote: votes) {
            addVote(vote);
        }
    }

    @Override
    public synchronized VoteCountSummary count() {
        return new VoteCountSummary(votesPerCandidate, accountableVotesPerCandidate);
    }

    @Override
    public synchronized void addVote(Vote vote) {
        increaseCountPerKey(votesPerUser, vote.getUserId());
        increaseCountPerKey(votesPerCandidate, vote.getCandidateId());
        if (votesPerUser.get(vote.getUserId()) <= MAX_VOTES_PER_USER) {
            increaseCountPerKey(accountableVotesPerCandidate, vote.getCandidateId());
        }
    }

    private static void increaseCountPerKey(Map<String, Long> map, String key) {
        if (!map.containsKey(key)) {
            map.put(key, 0L);
        }
        map.put(key, map.get(key) + 1);
    }
}
