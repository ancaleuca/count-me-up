package me.ancale.countmeup.votecounter;

import com.google.common.annotations.VisibleForTesting;
import me.ancale.countmeup.vote.Vote;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryVoteCounter implements VoteCounter {

    private static final int MAX_VOTES_PER_USER = 3;

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
