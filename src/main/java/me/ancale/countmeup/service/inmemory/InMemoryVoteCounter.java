package me.ancale.countmeup.service.inmemory;

import com.google.common.annotations.VisibleForTesting;
import me.ancale.countmeup.model.vote.AccountableVoteCountSummary;
import me.ancale.countmeup.model.vote.Vote;
import me.ancale.countmeup.service.VoteCounter;
import me.ancale.countmeup.service.VoteStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is a simple and fast memory-based solution for the counter.
 * However, it won't suffice because we cannot expect our server to run on a single machine
 * and thus have just the one cache to write and read from.
 * Also, of course, all data would be lost if the server went down.
 * I implemented this for fun to see how well Java will cope with the large dataset.
 */
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
    public synchronized AccountableVoteCountSummary countAccountable() {
        return new AccountableVoteCountSummary(accountableVotesPerCandidate);
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
