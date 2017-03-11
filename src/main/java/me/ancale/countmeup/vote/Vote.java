package me.ancale.countmeup.vote;

import com.google.common.base.Objects;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

public class Vote {

    private final String userId;

    private final String candidateId;

    private final long timestamp;

    public Vote(String userId, String candidateId, Instant time) {
        checkNotNull(userId, "'userId' cannot be null");
        checkNotNull(candidateId, "'candidateId' cannot be null");
        checkNotNull(time, "'time' cannot be null");

        this.userId = userId;
        this.candidateId = candidateId;
        this.timestamp = time.toEpochMilli();
    }

    public String getUserId() {
        return userId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equal(userId, vote.userId) &&
                Objects.equal(candidateId, vote.candidateId) &&
                Objects.equal(timestamp, vote.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId, candidateId, timestamp);
    }
}
