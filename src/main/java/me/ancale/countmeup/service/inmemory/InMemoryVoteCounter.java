package me.ancale.countmeup.service.inmemory;

import me.ancale.countmeup.model.vote.Vote;
import me.ancale.countmeup.model.vote.VoteCountsDto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * This is a simple and fast memory-based solution for the counter.
 * However, it won't suffice because we cannot expect our server to run on a single machine
 * and thus have just the one cache to write and read from.
 * Also, of course, all data would be lost if the server went down.
 * I implemented this for fun to see how well Java will cope with the large dataset.
 */
public class InMemoryVoteCounter {

    private static final int MAX_VOTES_PER_USER = 3;

    private final Map<String, Long> votesPerCandidate;
    private final Map<String, Long> accountableVotesPerCandidate;
    private final Map<String, Long> votesPerUser;

    public InMemoryVoteCounter() {
        this.votesPerCandidate = new ConcurrentHashMap<>();
        this.accountableVotesPerCandidate = new ConcurrentHashMap<>();
        this.votesPerUser = new ConcurrentHashMap<>();
    }

    public synchronized VoteCountsDto countAccountable() {
        return VoteCountsDto.from(accountableVotesPerCandidate);
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
