package me.ancale.countmeup.controller;

import me.ancale.countmeup.repository.AccountableVoteRepository;
import me.ancale.countmeup.repository.UserVoteCountRepository;
import me.ancale.countmeup.repository.VoteRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    AccountableVoteRepository accountableVoteRepository;
    @Autowired
    private UserVoteCountRepository userVoteCountRepository;
    @Autowired
    MockMvc mockMvc;

    @Before
    public void before() {
        voteRepository.deleteAll();
        accountableVoteRepository.deleteAll();
        userVoteCountRepository.deleteAll();
    }

}
