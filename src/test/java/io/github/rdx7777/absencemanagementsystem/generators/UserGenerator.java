package io.github.rdx7777.absencemanagementsystem.generators;

import io.github.rdx7777.absencemanagementsystem.model.Position;
import io.github.rdx7777.absencemanagementsystem.model.User;

public class UserGenerator {

    public static User getRandomEmployee() {
        Long id = IdGenerator.getNextId();
        String name = WordGenerator.getRandomWord();
        String surname = WordGenerator.getRandomWord();
        String email = RegexWordGenerator.getRandomRegexWord("[a-z]{3}\\@[a-z]{3}\\.[a-z]{3}");
        String password = WordGenerator.getRandomWord();
        String jobTitle = WordGenerator.getRandomWord();
        Boolean isActive = true;
        Position position = Position.Employee;
        String role = WordGenerator.getRandomWord();

        return User.builder()
            .withId(id)
            .withName(name)
            .withSurname(surname)
            .withEmail(email)
            .withPassword(password)
            .withJobTitle(jobTitle)
            .withIsActive(isActive)
            .withPosition(position)
            .withRole(role)
            .build();
    }

    public static User getRandomEmployeeWithNullId() {
        String name = WordGenerator.getRandomWord();
        String surname = WordGenerator.getRandomWord();
        String email = RegexWordGenerator.getRandomRegexWord("[a-z]{3}\\@[a-z]{3}\\.[a-z]{3}");
        String password = WordGenerator.getRandomWord();
        String jobTitle = WordGenerator.getRandomWord();
        Boolean isActive = true;
        Position position = Position.Employee;
        String role = WordGenerator.getRandomWord();

        return User.builder()
            .withName(name)
            .withSurname(surname)
            .withEmail(email)
            .withPassword(password)
            .withJobTitle(jobTitle)
            .withIsActive(isActive)
            .withPosition(position)
            .withRole(role)
            .build();
    }

    public static User getRandomEmployeeWithSpecificId(Long id) {
        String name = WordGenerator.getRandomWord();
        String surname = WordGenerator.getRandomWord();
        String email = RegexWordGenerator.getRandomRegexWord("[a-z]{3}\\@[a-z]{3}\\.[a-z]{3}");
        String password = WordGenerator.getRandomWord();
        String jobTitle = WordGenerator.getRandomWord();
        Boolean isActive = true;
        Position position = Position.Employee;
        String role = WordGenerator.getRandomWord();

        return User.builder()
            .withId(id)
            .withName(name)
            .withSurname(surname)
            .withEmail(email)
            .withPassword(password)
            .withJobTitle(jobTitle)
            .withIsActive(isActive)
            .withPosition(position)
            .withRole(role)
            .build();
    }

    public static User getRandomCoverSupervisor() {
        Long id = IdGenerator.getNextId();
        String name = WordGenerator.getRandomWord();
        String surname = WordGenerator.getRandomWord();
        String email = RegexWordGenerator.getRandomRegexWord("[a-z]{3}\\@[a-z]{3}\\.[a-z]{3}");
        String password = WordGenerator.getRandomWord();
        String jobTitle = WordGenerator.getRandomWord();
        Boolean isActive = true;
        Position position = Position.CoverSupervisor;
        String role = WordGenerator.getRandomWord();

        return User.builder()
            .withId(id)
            .withName(name)
            .withSurname(surname)
            .withEmail(email)
            .withPassword(password)
            .withJobTitle(jobTitle)
            .withIsActive(isActive)
            .withPosition(position)
            .withRole(role)
            .build();
    }

    public static User getRandomHeadTeacher() {
        Long id = IdGenerator.getNextId();
        String name = WordGenerator.getRandomWord();
        String surname = WordGenerator.getRandomWord();
        String email = RegexWordGenerator.getRandomRegexWord("[a-z]{3}\\@[a-z]{3}\\.[a-z]{3}");
        String password = WordGenerator.getRandomWord();
        String jobTitle = WordGenerator.getRandomWord();
        Boolean isActive = true;
        Position position = Position.HeadTeacher;
        String role = WordGenerator.getRandomWord();

        return User.builder()
            .withId(id)
            .withName(name)
            .withSurname(surname)
            .withEmail(email)
            .withPassword(password)
            .withJobTitle(jobTitle)
            .withIsActive(isActive)
            .withPosition(position)
            .withRole(role)
            .build();
    }

    public static User getRandomHumanResourcesSupervisor() {
        Long id = IdGenerator.getNextId();
        String name = WordGenerator.getRandomWord();
        String surname = WordGenerator.getRandomWord();
        String email = RegexWordGenerator.getRandomRegexWord("[a-z]{3}\\@[a-z]{3}\\.[a-z]{3}");
        String password = WordGenerator.getRandomWord();
        String jobTitle = WordGenerator.getRandomWord();
        Boolean isActive = true;
        Position position = Position.HumanResourcesSupervisor;
        String role = WordGenerator.getRandomWord();

        return User.builder()
            .withId(id)
            .withName(name)
            .withSurname(surname)
            .withEmail(email)
            .withPassword(password)
            .withJobTitle(jobTitle)
            .withIsActive(isActive)
            .withPosition(position)
            .withRole(role)
            .build();
    }
}
