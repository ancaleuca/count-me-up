package me.ancale.countmeup.vote;

import java.util.Set;

public class InMemoryVoteCounter implements VoteCounter {

    private final Set<Vote> votes;

    public InMemoryVoteCounter(Set<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public long countTotal() {
        return votes.size();
    }
}
