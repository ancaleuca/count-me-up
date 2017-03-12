package me.ancale.countmeup.repository;

public class VoteCountPerCandidateQueryResult {

    private String candidateId;
    private Long count;

    public VoteCountPerCandidateQueryResult(String candidateId, Long count) {
        this.candidateId = candidateId;
        this.count = count;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public Long getCount() {
        return count;
    }
}
