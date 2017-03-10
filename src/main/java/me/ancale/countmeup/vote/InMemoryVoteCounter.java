package me.ancale.countmeup.vote;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class InMemoryVoteCounter implements VoteCounter {

    private final ImmutableSet<Vote> votes;

    public InMemoryVoteCounter(Set<Vote> votes) {
        this.votes = ImmutableSet.copyOf(votes);
    }

    @Override
    public long countTotal() {
        return votes.size();
    }
}
