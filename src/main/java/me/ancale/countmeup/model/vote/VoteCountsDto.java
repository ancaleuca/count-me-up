package me.ancale.countmeup.model.vote;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class VoteCountsDto {

    private final List<CandidateVoteCountDto> voteCounts;

    private VoteCountsDto(List<CandidateVoteCountDto> voteCounts) {
        this.voteCounts = voteCounts;
    }

    public static VoteCountsDto from(Map<String, Long> votesPerCandidate) {
        checkNotNull(votesPerCandidate, "'votesPerCandidate' cannot be null");
        List<CandidateVoteCountDto> candidateVotesList = votesPerCandidate.entrySet()
                .stream().map(entry -> new CandidateVoteCountDto(entry.getKey(), entry.getValue()))
                .sorted()
                .collect(Collectors.toList());
        return new VoteCountsDto(candidateVotesList);
    }

    public List<CandidateVoteCountDto> getVoteCounts() {
        return voteCounts;
    }
}
