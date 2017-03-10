package me.ancale.countmeup.vote;

import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkNotNull;

public class Vote {

    private final String userId;

    private final String candidateId;

    private final LocalDateTime createdAt;

    public Vote(String userId, String candidateId, LocalDateTime createdAt) {
        checkNotNull(userId, "'userId' cannot be null");
        checkNotNull(candidateId, "'candidateId' cannot be null");
        checkNotNull(createdAt, "'createdAt' cannot be null");

        this.userId = userId;
        this.candidateId = candidateId;
        this.createdAt = createdAt;
    }
    public String getUserId() {
        return userId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
