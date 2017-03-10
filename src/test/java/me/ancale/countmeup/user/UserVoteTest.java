package me.ancale.countmeup.user;

import me.ancale.countmeup.candidate.Candidate;
import me.ancale.countmeup.vote.Vote;
import org.junit.Test;

import java.time.Instant;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserVoteTest {

    @Test
    public void userShouldBeAbleToVote() {
        User user = new User("u1");
        Candidate candidate = new Candidate("c1");
        Instant now = Instant.now();

        Vote vote = user.voteFor(candidate, now);

        assertThat(vote.getUserId(), is("u1"));
        assertThat(vote.getCandidateId(), is("c1"));
        assertThat(vote.getTimestamp(), is(now));
    }

    @Test(expected = IllegalArgumentException.class)
    public void userShouldNotBeAbleToVoteForNullCandidate() {
        User user = new User("u1");

        user.voteFor(null, Instant.now());
    }

    @Test(expected = IllegalArgumentException.class)
    public void userShouldNotBeAbleToVoteAtNullDate() {
        User user = new User("u1");
        Candidate candidate = new Candidate("c1");

        user.voteFor(candidate, null);
    }

}
