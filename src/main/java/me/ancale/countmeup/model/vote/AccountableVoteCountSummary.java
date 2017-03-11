package me.ancale.countmeup.model.vote;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class AccountableVoteCountSummary {

    private final ImmutableMap<String, Long> accountablePerCandidate;

    public AccountableVoteCountSummary(Map<String, Long> accountablePerCandidate) {
        this.accountablePerCandidate = ImmutableMap.copyOf(accountablePerCandidate);
    }

    public ImmutableMap<String, Long> getAccountablePerCandidate() {
        return accountablePerCandidate;
    }
}
