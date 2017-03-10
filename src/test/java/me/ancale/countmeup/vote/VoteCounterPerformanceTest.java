package me.ancale.countmeup.vote;

import java.time.Instant;
import java.time.LocalDateTime;

public class VoteCounterPerformanceTest {

    public static void main(String... args) {
        System.out.println("Time start " + LocalDateTime.now());
        int count = 10_000_000;
        InMemoryVoteCounter inMemoryVoteCounter = new InMemoryVoteCounter();
        for (int i = 0; i < count; i++) {
            Vote vote = new Vote("u" + i, "c" + i % 5, Instant.now());
            inMemoryVoteCounter.addVote(vote);
            if (i % 300_000 == 0) {
                Instant start = Instant.now();
                VoteCountSummary summary = inMemoryVoteCounter.count();
                Instant end = Instant.now();
                System.out.println("Time spent counting (ms): " + (end.toEpochMilli() - start.toEpochMilli()));
                System.out.println("Total votes so far: " + summary.getTotalVotes());
            }
        }
        System.out.println("Total votes: " + inMemoryVoteCounter.count().getTotalVotes());
        System.out.println("Time end " + LocalDateTime.now());
    }
}
