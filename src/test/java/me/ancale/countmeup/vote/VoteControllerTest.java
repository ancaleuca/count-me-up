package me.ancale.countmeup.vote;

import me.ancale.countmeup.AbstractIntegrationTest;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteControllerTest extends AbstractIntegrationTest {

    @Test
    public void userCanVoteForCandidate() throws Exception {
        mockMvc.perform(post("/votes/{candidateId}", "c1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("userId", "u1"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }
}
