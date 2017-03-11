package me.ancale.countmeup.votecounter;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class VoteCountSummary {

    private final ImmutableMap<String, Long> totalPerCandidate;

    private final ImmutableMap<String, Long> accountablePerCandidate;

    private final long totalVotes;

    public VoteCountSummary(Map<String, Long> totalPerCandidate, Map<String, Long> accountablePerCandidate) {
        this.totalPerCandidate = ImmutableMap.copyOf(totalPerCandidate);
        this.accountablePerCandidate = ImmutableMap.copyOf(accountablePerCandidate);
        this.totalVotes = this.totalPerCandidate.entrySet().stream().mapToLong(Map.Entry::getValue).sum();
    }

    public ImmutableMap<String, Long> getTotalPerCandidate() {
        return totalPerCandidate;
    }

    public ImmutableMap<String, Long> getAccountablePerCandidate() {
        return accountablePerCandidate;
    }

    public long getTotalVotes() {
        return totalVotes;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("totalPerCandidate", totalPerCandidate)
                .add("accountablePerCandidate", accountablePerCandidate)
                .add("totalVotes", totalVotes)
                .toString();
    }
}
