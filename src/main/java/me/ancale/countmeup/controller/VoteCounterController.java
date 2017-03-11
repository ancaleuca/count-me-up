package me.ancale.countmeup.controller;

import me.ancale.countmeup.model.vote.VoteCountSummary;
import me.ancale.countmeup.service.VoteCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/votes/count")
public class VoteCounterController {

    private static final Logger log = LoggerFactory.getLogger(VoteCounterController.class);

    private final VoteCounter voteCounter;

    @Autowired
    public VoteCounterController(VoteCounter voteCounter) {
        this.voteCounter = voteCounter;
    }

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteCountSummary> countVotes() {
        log.info("Counting votes at {}", LocalDateTime.now());
        return new ResponseEntity<>(voteCounter.count(), HttpStatus.OK);
    }
}
