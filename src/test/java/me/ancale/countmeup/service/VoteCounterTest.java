package me.ancale.countmeup.service;

import me.ancale.countmeup.model.vote.Vote;
import me.ancale.countmeup.model.vote.VoteCountsDto;
import me.ancale.countmeup.service.inmemory.InMemoryVoteCounter;
import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class VoteCounterTest {

    private VoteCounter voteCounter;

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

        voteCounter = new InMemoryVoteCounter(Arrays.asList(vote1, vote2, vote3, vote4));

        VoteCountsDto voteCountsDto = voteCounter.countAccountable();

        assertThat(voteCountsDto, is(notNullValue()));
        assertThat(voteCountsDto.getVoteCounts(), is(notNullValue()));
        assertThat(voteCountsDto.getVoteCounts().size(), is(2));
        assertThat(voteCountsDto.getVoteCounts().get(0).getCandidateId(), is("c1"));
        assertThat(voteCountsDto.getVoteCounts().get(0).getCount(), is(2L));
        assertThat(voteCountsDto.getVoteCounts().get(1).getCandidateId(), is("c2"));
        assertThat(voteCountsDto.getVoteCounts().get(1).getCount(), is(1L));
    }
}
