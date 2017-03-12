package me.ancale.countmeup.counter;

import me.ancale.countmeup.model.vote.AccountableVote;
import me.ancale.countmeup.model.vote.UserVoteCount;
import me.ancale.countmeup.model.vote.Vote;
import me.ancale.countmeup.repository.AccountableVoteRepository;
import me.ancale.countmeup.repository.UserVoteCountRepository;
import me.ancale.countmeup.repository.VoteRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VoteApproverTest {

    private static final String USER_WITH_FEW_VOTES = "u1";
    private static final String USER_WITH_MANY_VOTES = "u2";

    private VoteApprover voteApprover;
    @Mock
    private AccountableVoteRepository accountableVoteRepository;
    @Mock
    private VoteRepository voteRepository;
    @Mock
    private UserVoteCountRepository userVoteCountRepository;

    @Before
    public void before() {
        voteApprover = new VoteApprover(accountableVoteRepository, voteRepository, userVoteCountRepository);

        when(userVoteCountRepository.findByUserId(USER_WITH_FEW_VOTES)).thenReturn(new UserVoteCount(USER_WITH_FEW_VOTES, 2));
        when(userVoteCountRepository.findByUserId(USER_WITH_MANY_VOTES)).thenReturn(new UserVoteCount(USER_WITH_MANY_VOTES, 4));
    }

    @Test
    public void shouldStoreAccountableVoteInBothTablesWhenUserHasFewVotes() {
        Instant now = Instant.now();
        Vote vote = new Vote(USER_WITH_FEW_VOTES, "c1", now);

        voteApprover.addVote(vote);

        verify(voteRepository).save(vote);
        verify(accountableVoteRepository).save(new AccountableVote(vote));
        ArgumentCaptor<UserVoteCount> argumentCaptor = ArgumentCaptor.forClass(UserVoteCount.class);
        verify(userVoteCountRepository).save(argumentCaptor.capture());
        UserVoteCount userVoteCount = argumentCaptor.getValue();
        assertThat(userVoteCount.getUserId(), is(USER_WITH_FEW_VOTES));
        assertThat(userVoteCount.getVoteCount(), is(3));
    }

    @Test
    public void shouldNotStoreAccountableVoteWhenUserHasManyVotes() {
        Instant now = Instant.now();
        Vote vote = new Vote(USER_WITH_MANY_VOTES, "c1", now);

        voteApprover.addVote(vote);

        verify(voteRepository).save(vote);
        verifyNoMoreInteractions(accountableVoteRepository);
        ArgumentCaptor<UserVoteCount> argumentCaptor = ArgumentCaptor.forClass(UserVoteCount.class);
        verify(userVoteCountRepository).save(argumentCaptor.capture());
        UserVoteCount userVoteCount = argumentCaptor.getValue();
        assertThat(userVoteCount.getUserId(), is(USER_WITH_MANY_VOTES));
        assertThat(userVoteCount.getVoteCount(), is(5));
    }
}
