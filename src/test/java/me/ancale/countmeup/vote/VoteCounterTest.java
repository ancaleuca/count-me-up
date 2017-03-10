package me.ancale.countmeup.vote;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class VoteCounterTest {

    @Test
    public void shouldCountTotalVotes() {
        LocalDateTime time = LocalDateTime.now();
        Vote vote1 = new Vote("u1", "c1", time);
        Vote vote2 = new Vote("u2", "c2", time);
        Set<Vote> votes = new HashSet<>(Arrays.asList(vote1, vote2));
        VoteCounter voteCounter = new InMemoryVoteCounter(votes);

        long count = voteCounter.countTotal();

        assertThat(count, is(2L));
    }

    @Test
    public void shouldNotCountDuplicateVotes() {
        LocalDateTime time = LocalDateTime.now();
        Vote vote = new Vote("u1", "c1", time);
        Vote duplicateVote = new Vote("u1", "c1", time);
        Set<Vote> votes = new HashSet<>(Arrays.asList(vote, duplicateVote));
        VoteCounter voteCounter = new InMemoryVoteCounter(votes);

        long count = voteCounter.countTotal();

        assertThat(count, is(1L));
    }
}
