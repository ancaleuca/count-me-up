package me.ancale.countmeup.votecounter;

public interface VoteCounter {

    int MAX_VOTES_PER_USER = 3;

    VoteCountSummary count();

}
