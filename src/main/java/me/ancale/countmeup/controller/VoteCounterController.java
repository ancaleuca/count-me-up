package me.ancale.countmeup.controller;

import me.ancale.countmeup.model.vote.VoteCountsDto;
import me.ancale.countmeup.counter.VoteCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/votes/count")
public class VoteCounterController {

    private final VoteCounter voteCounter;

    @Autowired
    public VoteCounterController(VoteCounter voteCounter) {
        this.voteCounter = voteCounter;
    }

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteCountsDto> accountableVotesPerCandidate() {
        return new ResponseEntity<>(voteCounter.countAccountable(), HttpStatus.OK);
    }
}
