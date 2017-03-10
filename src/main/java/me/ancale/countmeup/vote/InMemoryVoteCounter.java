package me.ancale.countmeup.vote;

import com.google.common.collect.ImmutableSet;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class InMemoryVoteCounter implements VoteCounter {

    private static final int MAX_VOTES_PER_USER = 3;

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

    @Override
    public Map<String, Long> countTotalAccountablePerCandidate() {
        return accountableVotes().stream().collect(groupingBy(Vote::getCandidateId, counting()));
    }

    private Set<Vote> accountableVotes() {
        Map<String, SortedSet<Vote>> votesByUser = new HashMap<>();
        Comparator<Vote> timeComparator = timeComparator();

        for (Vote vote: votes) {
            if (!votesByUser.containsKey(vote.getUserId())) {
                votesByUser.put(vote.getUserId(), new TreeSet<>(timeComparator));
            }

            Set<Vote> existingVotes = votesByUser.get(vote.getUserId());
            if (existingVotes.size() < MAX_VOTES_PER_USER) {
                votesByUser.get(vote.getUserId()).add(vote);
            }
        }

        return votesByUser.entrySet().stream().flatMap(entry -> entry.getValue().stream()).collect(Collectors.toSet());
    }

    private Comparator<Vote> timeComparator() {
        return Comparator
                .comparing(Vote::getTimestamp)
                .thenComparing(Vote::getCandidateId);
    }
}
