package me.ancale.countmeup.vote;

import com.google.common.annotations.VisibleForTesting;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InMemoryVoteCounter implements VoteCounter {

    private final Map<String, Long> votesPerCandidate;
    private final Map<String, Long> accountableVotesPerCandidate;
    private final Map<String, Long> votesPerUser;
    private final Queue<Vote> allVotes;

    public InMemoryVoteCounter() {
        this.votesPerCandidate = new ConcurrentHashMap<>();
        this.accountableVotesPerCandidate = new ConcurrentHashMap<>();
        this.votesPerUser = new ConcurrentHashMap<>();
        this.allVotes = new ConcurrentLinkedQueue<>();
    }

    @VisibleForTesting
    InMemoryVoteCounter(List<Vote> votes) {
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
        allVotes.add(vote);
        increaseCountPerKey(votesPerUser, vote.getUserId());
        increaseCountPerKey(votesPerCandidate, vote.getCandidateId());
        if (votesPerUser.get(vote.getUserId()) <= 3) {
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
