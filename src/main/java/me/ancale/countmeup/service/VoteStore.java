package me.ancale.countmeup.service;

import me.ancale.countmeup.model.vote.Vote;

public interface VoteStore {

    void addVote(Vote vote);
}
