package me.ancale.countmeup.performance.util;

import me.ancale.countmeup.model.vote.Vote;
import me.ancale.countmeup.counter.VoteApprover;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Random;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public class LoadInitialDataTest {

    @Autowired
    private VoteApprover voteApprover;

    @Test
    @Ignore
    public void loadData() {
        Random random = new Random(100);
        int voteCount = 1_000_000;
        int userCount = 800_000;

        for (int i = 0; i < voteCount; i++) {
            String userId = "u" + random.nextInt(userCount);
            String candidateId = "c" + (1 + random.nextInt(4));
            voteApprover.addVote(new Vote(userId, candidateId, Instant.now()));
        }
    }

}
