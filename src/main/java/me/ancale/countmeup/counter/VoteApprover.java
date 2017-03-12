package me.ancale.countmeup.counter;

import me.ancale.countmeup.model.vote.AccountableVote;
import me.ancale.countmeup.model.vote.UserVoteCount;
import me.ancale.countmeup.model.vote.Vote;
import me.ancale.countmeup.repository.AccountableVoteRepository;
import me.ancale.countmeup.repository.UserVoteCountRepository;
import me.ancale.countmeup.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class VoteApprover {

    private static final int MAX_VOTES_PER_USER = 3;

    private final AccountableVoteRepository accountableVoteRepository;
    private final VoteRepository voteRepository;
    private final UserVoteCountRepository userVoteCountRepository;

    @Autowired
    public VoteApprover(AccountableVoteRepository accountableVoteRepository,
                        VoteRepository voteRepository,
                        UserVoteCountRepository userVoteCountRepository) {
        this.accountableVoteRepository = accountableVoteRepository;
        this.voteRepository = voteRepository;
        this.userVoteCountRepository = userVoteCountRepository;
    }

    @Transactional
    public void addVote(Vote vote) {
        voteRepository.save(vote);
        UserVoteCount existingCount = userVoteCountRepository.findByUserId(vote.getUserId());
        if (existingCount == null) {
            existingCount = new UserVoteCount(vote.getUserId(), 0);
        }
        existingCount.setVoteCount(existingCount.getVoteCount() + 1);
        userVoteCountRepository.save(existingCount);
        if (existingCount.getVoteCount() <= MAX_VOTES_PER_USER) {
            AccountableVote accountableVote = new AccountableVote(vote);
            accountableVoteRepository.save(accountableVote);
        }
    }
}
