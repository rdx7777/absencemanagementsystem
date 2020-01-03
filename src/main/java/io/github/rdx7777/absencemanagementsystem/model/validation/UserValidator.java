package io.github.rdx7777.absencemanagementsystem.model.validation;

import io.github.rdx7777.absencemanagementsystem.model.Position;
import io.github.rdx7777.absencemanagementsystem.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserValidator extends Validator {

    public static List<String> validate(User user) {
        if (user == null) {
            return Collections.singletonList("User cannot be null.");
        }

        List<String> result = new ArrayList<>();

        addResultOfValidation(result, validateName(user.getName()));
        addResultOfValidation(result, validateSurname(user.getSurname()));
        addResultOfValidation(result, validateEmail(user.getEmail()));
        addResultOfValidation(result, validatePassword(user.getPassword()));
        addResultOfValidation(result, validateJobTitle(user.getJobTitle()));
        addResultOfValidation(result, validateIsActive(user.getIsActive()));
        addResultOfValidation(result, validatePosition(user.getPosition()));

        return result;
    }

    private static String validateName(String name) {
        if (name == null) {
            return "Name cannot be null.";
        }

        if (name.trim().isEmpty()) {
            return "Name must contain at least 1 character.";
        }

        return null;
    }

    private static String validateSurname(String surname) {
        if (surname == null) {
            return "Surname cannot be null.";
        }

        if (surname.trim().isEmpty()) {
            return "Surname must contain at least 1 character.";
        }

        return null;
    }

    private static String validateEmail(String email) {
        if (email == null) {
            return "Email cannot be null.";
        }

        if (!RegexPatterns.matchesEmailPattern(email)) {
            return "Email does not match correct email pattern.";
        }

        return null;
    }

    private static String validatePassword(String password) {
        if (password == null) {
            return "Password cannot be null.";
        }

        if (password.trim().isEmpty()) {
            return "Password must contain at least 1 character.";
        }

        return null;
    }

    private static String validateJobTitle(String jobTitle) {
        if (jobTitle == null) {
            return "Job title cannot be null.";
        }

        if (jobTitle.trim().isEmpty()) {
            return "Job title must contain at least 1 character.";
        }

        return null;
    }

    private static String validateIsActive(Boolean isActive) {
        if (isActive == null) {
            return "Is active index cannot be null.";
        }
        return null;
    }

    private static String validatePosition(Position position) {
        if (position == null) {
            return "Position cannot be null.";
        }
        return null;
    }
}
