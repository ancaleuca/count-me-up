package me.ancale.countmeup.model;

import me.ancale.countmeup.model.vote.VoteCountsDto;
import org.hamcrest.core.IsNull;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class VoteCountsFactoryTest {

    @Test
    public void noVoteCountsMappedCorrectly() {
        VoteCountsDto voteCounts = VoteCountsDto.from(Collections.emptyMap());

        assertThat(voteCounts, IsNull.notNullValue());
        assertThat(voteCounts.getVoteCounts(), IsNull.notNullValue());
        assertThat(voteCounts.getVoteCounts().isEmpty(), is(true));
    }

    @Test
    public void multipleVoteCountsMappedCorrectlyToListSortedByCandidateId() {
        Map<String, Long> voteCountsMap = new HashMap<>(3);
        voteCountsMap.put("c3", 3L);
        voteCountsMap.put("c2", 2L);
        voteCountsMap.put("c1", 3L);

        VoteCountsDto voteCounts = VoteCountsDto.from(voteCountsMap);

        assertThat(voteCounts, IsNull.notNullValue());
        assertThat(voteCounts.getVoteCounts(), IsNull.notNullValue());
        assertThat(voteCounts.getVoteCounts().size(), is(3));
        assertThat(voteCounts.getVoteCounts().get(0).getCandidateId(), is("c1"));
        assertThat(voteCounts.getVoteCounts().get(0).getCount(), is(3L));

        assertThat(voteCounts.getVoteCounts().get(1).getCandidateId(), is("c2"));
        assertThat(voteCounts.getVoteCounts().get(1).getCount(), is(2L));

        assertThat(voteCounts.getVoteCounts().get(2).getCandidateId(), is("c3"));
        assertThat(voteCounts.getVoteCounts().get(2).getCount(), is(3L));
    }
}
