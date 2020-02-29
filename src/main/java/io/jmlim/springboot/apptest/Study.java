package io.jmlim.springboot.apptest;

public class Study {
    private StudyStatus status;

    private int limit;

    public Study(int limit) {
        // 만약 limit 값이 0보다 작을경우 exception 발생.
        if(limit < 0) {
            throw new IllegalArgumentException("limit은 0보다 커야 합니다.");
        }
        this.limit = limit;
    }

    public StudyStatus getStatus() {
        return status;
    }

    public int getLimit() {
        return limit;
    }
}
