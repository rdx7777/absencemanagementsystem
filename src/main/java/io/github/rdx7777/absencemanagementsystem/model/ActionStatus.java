package io.github.rdx7777.absencemanagementsystem.model;

public enum ActionStatus {

    Awaiting("Awaiting"),
    No("No"),
    Yes("Yes");

    private final String status;

    ActionStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
