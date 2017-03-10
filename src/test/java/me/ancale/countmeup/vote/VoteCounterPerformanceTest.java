package me.ancale.countmeup.vote;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VoteCounterPerformanceTest {

    public static void main(String... args) {
        System.out.println("Time start " + LocalDateTime.now());
        VoteCounter counter = new InMemoryVoteCounter(originalVotes());
        System.out.println("Time start counting " + LocalDateTime.now());
        Map<String, Long> votesPerCandidate = counter.countTotalAccountablePerCandidate();
        System.out.println(votesPerCandidate);
        System.out.println("Time end " + LocalDateTime.now());
    }

    private static Set<Vote> originalVotes() {
        int count = 10000000;
        Set<Vote> votes = new HashSet<>(count);
        for (int i = 0; i < count; i++) {
            votes.add(new Vote("u" + i, "1", Instant.now()));
        }
        return votes;
    }
}
