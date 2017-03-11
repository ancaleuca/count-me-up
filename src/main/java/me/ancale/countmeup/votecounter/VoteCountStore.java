package me.ancale.countmeup.votecounter;

import me.ancale.countmeup.vote.Vote;

public interface VoteCountStore {

    void addVote(Vote vote);
}
