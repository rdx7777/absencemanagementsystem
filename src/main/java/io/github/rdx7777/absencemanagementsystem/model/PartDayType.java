package io.github.rdx7777.absencemanagementsystem.model;

public enum PartDayType {

    Morning("Morning"),
    Afternoon("Afternoon"),
    AllDay("AllDay");

    private final String typeDescription;

    PartDayType(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getTypeDescription() {
        return typeDescription;
    }
}
