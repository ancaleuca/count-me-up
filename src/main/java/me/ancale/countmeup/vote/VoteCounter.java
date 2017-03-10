package me.ancale.countmeup.vote;

import java.util.Map;

public interface VoteCounter {

    long countTotal();

    Map<String, Long> countTotalPerCandidate();

    Map<String, Long> countTotalAccountablePerCandidate();
}
