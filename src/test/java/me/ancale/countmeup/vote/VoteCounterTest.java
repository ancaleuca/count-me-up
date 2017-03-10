package me.ancale.countmeup.vote;

import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class VoteCounterTest {

    @Test
    public void shouldCountTotalVotes() {
        Instant time = Instant.now();
        Vote vote1 = new Vote("u1", "c1", time);
        Vote vote2 = new Vote("u2", "c2", time);
        Set<Vote> votes = new HashSet<>(Arrays.asList(vote1, vote2));
        VoteCounter voteCounter = new InMemoryVoteCounter(votes);

        long count = voteCounter.countTotal();

        assertThat(count, is(2L));
    }

    @Test
    public void shouldNotCountDuplicateVotes() {
        Instant time = Instant.now();
        Vote vote = new Vote("u1", "c1", time);
        Vote duplicateVote = new Vote("u1", "c1", time);
        Set<Vote> votes = new HashSet<>(Arrays.asList(vote, duplicateVote));
        VoteCounter voteCounter = new InMemoryVoteCounter(votes);

        long count = voteCounter.countTotal();

        assertThat(count, is(1L));
    }

    @Test
    public void shouldCountTotalVotesPerCandidate() {
        Instant time = Instant.now();
        Vote vote1 = new Vote("u1", "c1", time);
        Vote vote2 = new Vote("u1", "c2", time);
        Vote vote3 = new Vote("u1", "c3", time);
        Vote vote4 = new Vote("u2", "c1", time);
        Vote vote5 = new Vote("u2", "c2", time);
        Vote vote6 = new Vote("u3", "c1", time);

        Set<Vote> votes = new HashSet<>(Arrays.asList(vote1, vote2, vote3, vote4, vote5, vote6));

        VoteCounter voteCounter = new InMemoryVoteCounter(votes);

        Map<String, Long> votesPerCandidate = voteCounter.countTotalPerCandidate();

        assertThat(votesPerCandidate, is(notNullValue()));
        assertThat(votesPerCandidate.size(), is(3));
        assertThat(votesPerCandidate.get("c1"), is(3L));
        assertThat(votesPerCandidate.get("c2"), is(2L));
        assertThat(votesPerCandidate.get("c3"), is(1L));
    }

    @Test
    public void shouldCountTotalAccountableVotesPerCandidate() {
        Instant now = Instant.now();
        Instant oneSecondAfter = now.plus(1, SECONDS);
        Instant twoSecondsAfter = now.plus(2, SECONDS);
        Instant oneSecondBefore = now.minus(1, SECONDS);

        Vote vote1 = new Vote("u1", "c1", now);
        Vote vote2 = new Vote("u1", "c2", oneSecondAfter);
        Vote vote3 = new Vote("u1", "c1", oneSecondBefore);
        Vote vote4 = new Vote("u1", "c1", twoSecondsAfter);

        Set<Vote> votes = new HashSet<>(Arrays.asList(vote1, vote2, vote3, vote4));

        VoteCounter voteCounter = new InMemoryVoteCounter(votes);

        Map<String, Long> votesPerCandidate = voteCounter.countTotalAccountablePerCandidate();

        assertThat(votesPerCandidate, is(notNullValue()));
        assertThat(votesPerCandidate.size(), is(2));
        assertThat(votesPerCandidate.get("c1"), is(2L));
        assertThat(votesPerCandidate.get("c2"), is(1L));
    }
}
