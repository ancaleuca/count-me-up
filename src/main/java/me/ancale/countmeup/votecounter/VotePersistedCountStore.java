package me.ancale.countmeup.votecounter;

import me.ancale.countmeup.vote.AccountableVote;
import me.ancale.countmeup.vote.AccountableVoteRepository;
import me.ancale.countmeup.vote.Vote;
import me.ancale.countmeup.vote.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Primary
public class VotePersistedCountStore implements VoteCountStore {

    private final VoteRepository voteRepository;
    private final AccountableVoteRepository accountableVoteRepository;
    private final UserVoteCountRepository userVoteCountRepository;

    @Autowired
    public VotePersistedCountStore(VoteRepository voteRepository, AccountableVoteRepository accountableVoteRepository,
                                   UserVoteCountRepository userVoteCountRepository) {
        this.voteRepository = voteRepository;
        this.accountableVoteRepository = accountableVoteRepository;
        this.userVoteCountRepository = userVoteCountRepository;
    }

    @Override
    @Transactional
    public void addVote(Vote vote) {
        voteRepository.save(vote);
        UserVoteCount existingCount = userVoteCountRepository.findByUserId(vote.getUserId());
        if (existingCount == null) {
            existingCount = new UserVoteCount(vote.getUserId(), 0);
        }
        existingCount.setVoteCount(existingCount.getVoteCount() + 1);
        userVoteCountRepository.save(existingCount);
        if (existingCount.getVoteCount() <= VoteCounter.MAX_VOTES_PER_USER) {
            AccountableVote accountableVote = new AccountableVote(vote);
            accountableVoteRepository.save(accountableVote);
        }
    }
}
