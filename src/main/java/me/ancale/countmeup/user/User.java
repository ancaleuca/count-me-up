package me.ancale.countmeup.user;

import me.ancale.countmeup.candidate.Candidate;
import me.ancale.countmeup.vote.Vote;

import java.time.LocalDateTime;

public class User {

    private final String id;

    public User(String id) {
        this.id = id;
    }

    public Vote voteFor(Candidate candidate, LocalDateTime now) {
        return new Vote(getId(), candidate.getId(), now);
    }

    public String getId() {
        return id;
    }
}
