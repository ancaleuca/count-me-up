package me.ancale.countmeup.controller;

import me.ancale.countmeup.model.Candidate;
import me.ancale.countmeup.model.User;
import me.ancale.countmeup.service.VoteStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/votes")
public class VoteController {

    private static final String VAR_CANDIDATE_ID = "candidateId";
    private static final String VAR_USER_ID = "userId";

    private final VoteStore voteStore;

    @Autowired
    public VoteController(VoteStore voteStore) {
        this.voteStore = voteStore;
    }

    // IMPORTANT: For the sake of brevity, I'm passing userId as a request parameter
    // Normally, there would be some sort of authentication in place, e.g., using spring security
    // Also, some validation could have been done around candidateId,
    // for example by keeping a list of known candidates and returning a 404 if candidateId is not in the list
    @RequestMapping(method = POST, path = "/{" + VAR_CANDIDATE_ID + "}")
    public ResponseEntity<?> vote(@PathVariable(VAR_CANDIDATE_ID) String candidateId,
                                  @RequestParam(VAR_USER_ID) String userId) {
        User user = new User(userId);
        Candidate candidate = new Candidate(candidateId);

        voteStore.addVote(user.voteFor(candidate, Instant.now()));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
