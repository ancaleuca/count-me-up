package me.ancale.countmeup.service;

import me.ancale.countmeup.model.vote.VoteCountsDto;
import me.ancale.countmeup.repository.AccountableVoteRepository;
import me.ancale.countmeup.repository.VoteCountPerCandidateQueryResult;
import me.ancale.countmeup.service.counter.PersistenceBasedVoteCounter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VoteCounterTest {

    private static final String CANDIDATE_1 = "c1";
    private static final String CANDIDATE_2 = "c2";
    private static final String CANDIDATE_3 = "c3";
    private static final long CANDIDATE_1_VOTES_CURRENT = 1L;
    private static final long CANDIDATE_2_VOTES_CURRENT = 2L;

    private static final long CANDIDATE_1_VOTES_EXTRA = 3L;
    private static final long CANDIDATE_2_VOTES_EXTRA = 4L;
    private static final long CANDIDATE_3_VOTES = 8L;

    private static final long CANDIDATE_1_VOTES_TOTAL = CANDIDATE_1_VOTES_CURRENT + CANDIDATE_1_VOTES_EXTRA;
    private static final long CANDIDATE_2_VOTES_TOTAL = CANDIDATE_2_VOTES_CURRENT + CANDIDATE_2_VOTES_EXTRA;

    private static final Instant CURRENT = Instant.now();
    private static final Instant CURRENT_PLUS_ONE = CURRENT.plus(1, SECONDS);

    private VoteCounter voteCounter;

    @Mock
    private AccountableVoteRepository accountableVoteRepository;
    @Mock
    private Clock clock;

    @Before
    public void before() {
        voteCounter = new PersistenceBasedVoteCounter(accountableVoteRepository, null, null, clock);

        when(clock.millis()).thenReturn(CURRENT.toEpochMilli()).thenReturn(CURRENT_PLUS_ONE.toEpochMilli());

        persistedVoteCountsBetweenStartAndEndTimestampAre(0L, CURRENT.toEpochMilli(),
                voteCountsFromBeginningToNow());

        persistedVoteCountsBetweenStartAndEndTimestampAre(CURRENT.toEpochMilli(), CURRENT_PLUS_ONE.toEpochMilli(),
                voteCountsFromNowToInASecondFromNow());
    }

    @Test
    public void shouldCountVotesPerCandidateBasedOnLatestPersistedVotesAndCachedVotes() {
        VoteCountsDto voteCountsDto = voteCounter.countAccountable();

        checkCurrentVoteCounts(voteCountsDto);

        // on second call should add up cached votes + votes persisted in the mean time
        voteCountsDto = voteCounter.countAccountable();

        checkInASecondVoteCounts(voteCountsDto);
    }

    private void checkInASecondVoteCounts(VoteCountsDto voteCountsDto) {
        assertThat(voteCountsDto, is(notNullValue()));
        assertThat(voteCountsDto.getVoteCounts(), is(notNullValue()));
        assertThat(voteCountsDto.getVoteCounts().size(), is(3));

        assertThat(voteCountsDto.getVoteCounts().get(0).getCandidateId(), is(CANDIDATE_1));
        assertThat(voteCountsDto.getVoteCounts().get(0).getCount(), is(CANDIDATE_1_VOTES_TOTAL));

        assertThat(voteCountsDto.getVoteCounts().get(1).getCandidateId(), is(CANDIDATE_2));
        assertThat(voteCountsDto.getVoteCounts().get(1).getCount(), is(CANDIDATE_2_VOTES_TOTAL));

        assertThat(voteCountsDto.getVoteCounts().get(2).getCandidateId(), is(CANDIDATE_3));
        assertThat(voteCountsDto.getVoteCounts().get(2).getCount(), is(CANDIDATE_3_VOTES));
    }

    private void checkCurrentVoteCounts(VoteCountsDto voteCountsDto) {
        assertThat(voteCountsDto, is(notNullValue()));
        assertThat(voteCountsDto.getVoteCounts(), is(notNullValue()));
        assertThat(voteCountsDto.getVoteCounts().size(), is(2));

        assertThat(voteCountsDto.getVoteCounts().get(0).getCandidateId(), is(CANDIDATE_1));
        assertThat(voteCountsDto.getVoteCounts().get(0).getCount(), is(CANDIDATE_1_VOTES_CURRENT));

        assertThat(voteCountsDto.getVoteCounts().get(1).getCandidateId(), is(CANDIDATE_2));
        assertThat(voteCountsDto.getVoteCounts().get(1).getCount(), is(CANDIDATE_2_VOTES_CURRENT));
    }

    private void persistedVoteCountsBetweenStartAndEndTimestampAre(
            long start, long end, List<VoteCountPerCandidateQueryResult> results) {
        when(accountableVoteRepository.countVotesPerCandidate(start, end)).thenReturn(results);
    }

    private List<VoteCountPerCandidateQueryResult> voteCountsFromBeginningToNow() {
        List<VoteCountPerCandidateQueryResult> nowResults = new ArrayList<>(2);
        nowResults.add(new VoteCountPerCandidateQueryResult(CANDIDATE_1, CANDIDATE_1_VOTES_CURRENT));
        nowResults.add(new VoteCountPerCandidateQueryResult(CANDIDATE_2, CANDIDATE_2_VOTES_CURRENT));
        return nowResults;
    }

    private List<VoteCountPerCandidateQueryResult> voteCountsFromNowToInASecondFromNow() {
        List<VoteCountPerCandidateQueryResult> aSecondLaterResults = new ArrayList<>(3);
        aSecondLaterResults.add(new VoteCountPerCandidateQueryResult(CANDIDATE_1, CANDIDATE_1_VOTES_EXTRA));
        aSecondLaterResults.add(new VoteCountPerCandidateQueryResult(CANDIDATE_2, CANDIDATE_2_VOTES_EXTRA));
        aSecondLaterResults.add(new VoteCountPerCandidateQueryResult(CANDIDATE_3, CANDIDATE_3_VOTES));
        return aSecondLaterResults;
    }
}
