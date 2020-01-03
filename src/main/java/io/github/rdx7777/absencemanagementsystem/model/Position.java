package io.github.rdx7777.absencemanagementsystem.model;

public enum Position {

    Employee("Employee"),
    CoverSupervisor("Cover Supervisor"),
    HeadTeacher("Head Teacher"),
    HumanResourcesSupervisor("HR Supervisor");

    private final String positionDescription;

    Position(String positionDescription) {
        this.positionDescription = positionDescription;
    }

    public String getPositionDescription() {
        return positionDescription;
    }
}
