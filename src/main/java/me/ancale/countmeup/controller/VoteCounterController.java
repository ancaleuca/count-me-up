package me.ancale.countmeup.controller;

import me.ancale.countmeup.model.vote.AccountableVoteCountSummary;
import me.ancale.countmeup.model.vote.TotalVoteCountSummary;
import me.ancale.countmeup.service.VoteCounter;
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

    @RequestMapping(method = GET, path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TotalVoteCountSummary> allVotesSummary() {
        return new ResponseEntity<>(voteCounter.countAll(), HttpStatus.OK);
    }

    @RequestMapping(method = GET, path = "/accountable", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountableVoteCountSummary> accountableVotesSummary() {
        return new ResponseEntity<>(voteCounter.countAccountable(), HttpStatus.OK);
    }
}
