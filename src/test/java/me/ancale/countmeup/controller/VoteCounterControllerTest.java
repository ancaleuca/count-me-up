package me.ancale.countmeup.controller;

import org.junit.Test;

import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteCounterControllerTest extends AbstractIntegrationTest {

    @Test
    public void canCountAccountableVotesPerCandidate() throws Exception {
        mockMvc.perform(get("/votes/count")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath(".accountableVotesPerCandidate").value(Collections.emptyMap()))
                .andReturn();
    }

}
