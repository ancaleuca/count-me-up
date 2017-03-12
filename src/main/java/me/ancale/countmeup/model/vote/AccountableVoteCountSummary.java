package me.ancale.countmeup.model.vote;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class AccountableVoteCountSummary {

    private final ImmutableMap<String, Long> accountableVotesPerCandidate;

    public AccountableVoteCountSummary(Map<String, Long> accountableVotesPerCandidate) {
        this.accountableVotesPerCandidate = ImmutableMap.copyOf(accountableVotesPerCandidate);
    }

    public ImmutableMap<String, Long> getAccountableVotesPerCandidate() {
        return accountableVotesPerCandidate;
    }
}
