package me.ancale.countmeup.user;

import me.ancale.countmeup.candidate.Candidate;
import me.ancale.countmeup.vote.Vote;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserVoteTest {

    @Test
    public void userShouldBeAbleToCastVote() {
        User user = new User("u1");
        Candidate candidate = new Candidate("c1");
        LocalDateTime now = LocalDateTime.now();

        Vote vote = user.voteFor(candidate, now);

        assertThat(vote.getUserId(), is("u1"));
        assertThat(vote.getCandidateId(), is("c1"));
        assertThat(vote.getDateTime(), is(now));
    }
}
