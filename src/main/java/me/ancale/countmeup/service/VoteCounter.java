package me.ancale.countmeup.service;

import me.ancale.countmeup.model.vote.AccountableVoteCountSummary;

public interface VoteCounter {

    int MAX_VOTES_PER_USER = 3;

    AccountableVoteCountSummary countAccountable();

}
