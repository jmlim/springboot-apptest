package io.jmlim.springboot.apptest;

public class Study {
    private StudyStatus status;

    private int limit;

    public Study(int limit) {
        this.limit = limit;
    }

    public StudyStatus getStatus() {
        return status;
    }

    public int getLimit() {
        return limit;
    }
}
