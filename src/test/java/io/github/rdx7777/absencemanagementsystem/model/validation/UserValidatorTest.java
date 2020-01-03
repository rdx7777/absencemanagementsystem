package io.github.rdx7777.absencemanagementsystem.model.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.rdx7777.absencemanagementsystem.model.Position;
import io.github.rdx7777.absencemanagementsystem.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UserValidatorTest {

    private User correctUser;

    @BeforeEach
    void setup() {
        correctUser = User.builder()
            .withId(100L)
            .withName("Alice")
            .withSurname("Springfield")
            .withEmail("alice.springfield@gmail.com")
            .withPassword("pass")
            .withJobTitle("Math Teacher")
            .withIsActive(true)
            .withPosition(Position.Employee)
            .build();
    }

    @Test
    void shouldValidateUser() {
        List<String> resultOfValidation = UserValidator.validate(null);
        assertEquals(Arrays.asList("User cannot be null."), resultOfValidation);
    }

    @ParameterizedTest
    @MethodSource("SetOfNamesAndValidationResults")
    void shouldValidateName(String name, List<String> expected) {
        User userWithVariableName = User.builder()
            .withUser(correctUser)
            .withName(name)
            .build();

        List<String> resultOfValidation = UserValidator.validate(userWithVariableName);

        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> SetOfNamesAndValidationResults() {
        return Stream.of(
            Arguments.of(null, Arrays.asList("Name cannot be null.")),
            Arguments.of("", Arrays.asList("Name must contain at least 1 character.")),
            Arguments.of("     ", Arrays.asList("Name must contain at least 1 character.")),
            Arguments.of("Mary", Arrays.asList()),
            Arguments.of("John", Arrays.asList())
        );
    }

    @ParameterizedTest
    @MethodSource("SetOfSurnamesAndValidationResults")
    void shouldValidateSurname(String surname, List<String> expected) {
        User userWithVariableSurname = User.builder()
            .withUser(correctUser)
            .withSurname(surname)
            .build();

        List<String> resultOfValidation = UserValidator.validate(userWithVariableSurname);

        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> SetOfSurnamesAndValidationResults() {
        return Stream.of(
            Arguments.of(null, Arrays.asList("Surname cannot be null.")),
            Arguments.of("", Arrays.asList("Surname must contain at least 1 character.")),
            Arguments.of("     ", Arrays.asList("Surname must contain at least 1 character.")),
            Arguments.of("Johnson", Arrays.asList()),
            Arguments.of("Drake", Arrays.asList())
        );
    }

    @ParameterizedTest
    @MethodSource("SetOfEmailsAndValidationResults")
    void shouldValidateEmail(String email, List<String> expected) {
        User userWithVariableEmail = User.builder()
            .withUser(correctUser)
            .withEmail(email)
            .build();

        List<String> resultOfValidation = UserValidator.validate(userWithVariableEmail);

        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> SetOfEmailsAndValidationResults() {
        return Stream.of(
            Arguments.of(null, Arrays.asList("Email cannot be null.")),
            Arguments.of("", Arrays.asList("Email does not match correct email pattern.")),
            Arguments.of("     ", Arrays.asList("Email does not match correct email pattern.")),
            Arguments.of("alice.springfield.gmail.com     ", Arrays.asList("Email does not match correct email pattern.")),
            Arguments.of("john.doe@yahoo,us", Arrays.asList("Email does not match correct email pattern.")),
            Arguments.of("@gmail.com", Arrays.asList("Email does not match correct email pattern.")),
            Arguments.of("john>smith@gmail.com", Arrays.asList("Email does not match correct email pattern.")),
            Arguments.of("alice.springfield@platform.u", Arrays.asList("Email does not match correct email pattern.")),
            Arguments.of("John.Smith@gmail.com", Arrays.asList()),
            Arguments.of("johnsmith@gmail.com", Arrays.asList()),
            Arguments.of("john-smith@yahoo.us", Arrays.asList())
        );
    }

    @ParameterizedTest
    @MethodSource("SetOfPasswordsAndValidationResults")
    void shouldValidatePassword(String password, List<String> expected) {
        User userWithVariablePassword = User.builder()
            .withUser(correctUser)
            .withPassword(password)
            .build();

        List<String> resultOfValidation = UserValidator.validate(userWithVariablePassword);

        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> SetOfPasswordsAndValidationResults() {
        return Stream.of(
            Arguments.of(null, Arrays.asList("Password cannot be null.")),
            Arguments.of("", Arrays.asList("Password must contain at least 1 character.")),
            Arguments.of("     ", Arrays.asList("Password must contain at least 1 character.")),
            Arguments.of("r5678$ikn^bg(yu", Arrays.asList()),
            Arguments.of("pass123", Arrays.asList())
        );
    }

    @ParameterizedTest
    @MethodSource("SetOfJobTitlesAndValidationResults")
    void shouldValidateJobTitle(String jobTitle, List<String> expected) {
        User userWithVariableJobTitle = User.builder()
            .withUser(correctUser)
            .withJobTitle(jobTitle)
            .build();

        List<String> resultOfValidation = UserValidator.validate(userWithVariableJobTitle);

        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> SetOfJobTitlesAndValidationResults() {
        return Stream.of(
            Arguments.of(null, Arrays.asList("Job title cannot be null.")),
            Arguments.of("", Arrays.asList("Job title must contain at least 1 character.")),
            Arguments.of("     ", Arrays.asList("Job title must contain at least 1 character.")),
            Arguments.of("Math teacher", Arrays.asList()),
            Arguments.of("IT administrator", Arrays.asList())
        );
    }

    @Test
    void shouldValidateIsActiveIndex() {
        User userWithNullIsActiveIndex = User.builder()
            .withUser(correctUser)
            .withIsActive(null)
            .build();

        List<String> resultOfValidation = UserValidator.validate(userWithNullIsActiveIndex);

        assertEquals(Arrays.asList("Is active index cannot be null."), resultOfValidation);
    }

    @Test
    void shouldValidatePosition() {
        User userWithNullPosition = User.builder()
            .withUser(correctUser)
            .withPosition(null)
            .build();

        List<String> resultOfValidation = UserValidator.validate(userWithNullPosition);

        assertEquals(Arrays.asList("Position cannot be null."), resultOfValidation);
    }
}
