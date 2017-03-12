package me.ancale.countmeup.controller;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteCounterControllerTest extends AbstractIntegrationTest {

    @Test
    @Ignore
    public void canCountAllVotesPerCandidate() throws Exception {
        mockMvc.perform(get("/votes/count/all")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath(".totalVotes").value(0))
                .andExpect(jsonPath(".totalPerCandidate").value(Collections.emptyMap()))
                .andExpect(jsonPath(".accountablePerCandidate").value(Collections.emptyMap()))
                .andReturn();
    }

    @Test
    public void canCountAccountableVotesPerCandidate() throws Exception {
        mockMvc.perform(get("/votes/count/accountable")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath(".accountablePerCandidate").value(Collections.emptyMap()))
                .andReturn();
    }

}
