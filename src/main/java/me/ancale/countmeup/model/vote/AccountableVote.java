package me.ancale.countmeup.model.vote;

import com.google.common.base.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;

@Entity
public class AccountableVote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String userId;

    @NotNull
    private String candidateId;

    @NotNull
    private long timestamp;

    public AccountableVote(Vote vote) {
        checkNotNull(vote, "'controller' cannot be null");

        this.userId = vote.getUserId();
        this.candidateId = vote.getCandidateId();
        this.timestamp = vote.getTimestamp();
    }

    // for JPA
    public AccountableVote() {
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

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountableVote vote = (AccountableVote) o;
        return Objects.equal(userId, vote.userId) &&
                Objects.equal(candidateId, vote.candidateId) &&
                Objects.equal(timestamp, vote.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId, candidateId, timestamp);
    }
}
