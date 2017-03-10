package me.ancale.countmeup.vote;

import com.google.common.collect.ImmutableSet;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class InMemoryVoteCounter implements VoteCounter {

    private final ImmutableSet<Vote> votes;

    public InMemoryVoteCounter(Set<Vote> votes) {
        this.votes = ImmutableSet.copyOf(votes);
    }

    @Override
    public long countTotal() {
        return votes.size();
    }

    @Override
    public Map<String, Long> countTotalPerCandidate() {
        return votes.stream().collect(groupingBy(Vote::getCandidateId, counting()));
    }
}
