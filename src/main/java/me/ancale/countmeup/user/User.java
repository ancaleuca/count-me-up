package me.ancale.countmeup.user;

import me.ancale.countmeup.candidate.Candidate;
import me.ancale.countmeup.vote.Vote;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class User {

    private final String id;

    public User(String id) {
        checkNotNull(id, "'id' cannot be null");
        this.id = id;
    }

    public Vote voteFor(Candidate candidate, Instant time) {
        checkArgument(candidate != null, "'candidate' cannot be null");
        checkArgument(time != null, "'time' cannot be null");
        return new Vote(getId(), candidate.getId(), time);
    }

    public String getId() {
        return id;
    }
}
