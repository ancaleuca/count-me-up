package me.ancale.countmeup.vote;

import java.time.LocalDateTime;

public class Vote {

    private final String userId;

    private final String candidateId;

    private final LocalDateTime dateTime;

    public Vote(String userId, String candidateId, LocalDateTime dateTime) {
        this.userId = userId;
        this.candidateId = candidateId;
        this.dateTime = dateTime;
    }
    public String getUserId() {
        return userId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
