package me.ancale.countmeup.model.vote;

public class CandidateVoteCountDto implements Comparable<CandidateVoteCountDto> {

    private final String candidateId;
    private final long count;

    public CandidateVoteCountDto(String candidateId, long count) {
        this.candidateId = candidateId;
        this.count = count;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public long getCount() {
        return count;
    }

    @Override
    public int compareTo(CandidateVoteCountDto o) {
        return this.candidateId.compareTo(o.candidateId);
    }
}
