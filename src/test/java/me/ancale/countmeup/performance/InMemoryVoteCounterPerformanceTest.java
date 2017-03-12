package me.ancale.countmeup.performance;

import me.ancale.countmeup.PerformanceTest;
import me.ancale.countmeup.model.vote.Vote;
import me.ancale.countmeup.service.inmemory.InMemoryVoteCounter;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@Category(PerformanceTest.class)
public class InMemoryVoteCounterPerformanceTest {

    @Test
    public void multipleThreadsShouldCountVotesUnderOneSecond() throws Exception {
        InMemoryVoteCounter inMemoryVoteCounter = new InMemoryVoteCounter();
        int totalVoteCount = 10_000_000;
        int totalAddingThreads = 100;
        List<Thread> threads = new ArrayList<>(totalAddingThreads);

        for (int i = 0; i < totalAddingThreads; i++) {
            threads.add(new Thread(() -> addVotes(totalVoteCount / totalAddingThreads, inMemoryVoteCounter)));
        }

        Thread countingThread = new Thread(() -> countVotes(inMemoryVoteCounter));

        threads.add(countingThread);

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

    private void addVotes(int count, InMemoryVoteCounter inMemoryVoteCounter) {
        for (int i = 0; i < count; i++) {
            Vote vote = new Vote("u" + i, "c" + i % 5, Instant.now());
            inMemoryVoteCounter.addVote(vote);
        }
    }

    private void countVotes(InMemoryVoteCounter inMemoryVoteCounter) {
        int numberOfRuns = 30;
        int sleepForMillis = 1000;

        int i = 0;
        while(i++ <= numberOfRuns) {
            try {
                Thread.sleep(sleepForMillis);
            } catch (InterruptedException e) {
                fail(e.getMessage());
            }
            Instant start = Instant.now();
            inMemoryVoteCounter.countAccountable();
            Instant end = Instant.now();
            long millisSpentCounting = end.toEpochMilli() - start.toEpochMilli();
            assertThat("Took over a second to count = " + millisSpentCounting, millisSpentCounting < 1000, is(true));
        }
    }
}
