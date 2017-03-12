package me.ancale.countmeup.controller;

import me.ancale.countmeup.model.vote.AccountableVote;
import me.ancale.countmeup.model.vote.Vote;
import org.junit.Test;

import java.time.Instant;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteCounterControllerTest extends AbstractIntegrationTest {

    @Test
    public void canCountAccountableVotesPerCandidate() throws Exception {
        Instant now = Instant.now();
        accountableVoteRepository.save(new AccountableVote(new Vote("user123", "anca", now)));

        mockMvc.perform(get("/votes/count")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.voteCounts").isArray())
                .andExpect(jsonPath("$.voteCounts", hasSize(1)))
                .andExpect(jsonPath("$.voteCounts[0].candidateId").value("anca"))
                .andExpect(jsonPath("$.voteCounts[0].count").value(1))
                .andReturn();
    }
}
