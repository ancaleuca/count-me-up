package me.ancale.countmeup.candidate;

import static com.google.common.base.Preconditions.checkNotNull;

public class Candidate {
    private final String id;

    public Candidate(String id) {
        checkNotNull(id, "'id' cannot be null");
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
