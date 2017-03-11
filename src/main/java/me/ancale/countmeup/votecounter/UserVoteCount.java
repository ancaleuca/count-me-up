package me.ancale.countmeup.votecounter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;

@Entity
public class UserVoteCount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String userId;

    private int voteCount;

    public UserVoteCount(String userId, int voteCount) {
        checkNotNull(userId, "'userId' cannot be null");

        this.userId = userId;
        this.voteCount = voteCount;
    }

    public UserVoteCount() {
    }

    public String getUserId() {
        return userId;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public long getId() {
        return id;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
