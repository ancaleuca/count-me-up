package me.ancale.countmeup.model.vote;

public class VotePerCandidate {

    private String candidateId;
    private Long count;

    public VotePerCandidate(String candidateId, Long count) {
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
