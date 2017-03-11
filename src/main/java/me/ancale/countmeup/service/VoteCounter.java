package me.ancale.countmeup.service;

import me.ancale.countmeup.model.vote.AccountableVoteCountSummary;
import me.ancale.countmeup.model.vote.TotalVoteCountSummary;

public interface VoteCounter {

    int MAX_VOTES_PER_USER = 3;

    TotalVoteCountSummary countAll();

    AccountableVoteCountSummary countAccountable();

}
