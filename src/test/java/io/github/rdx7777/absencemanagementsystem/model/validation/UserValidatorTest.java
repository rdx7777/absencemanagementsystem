package io.github.rdx7777.absencemanagementsystem.model.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.rdx7777.absencemanagementsystem.model.Position;
import io.github.rdx7777.absencemanagementsystem.model.User;

import java.util.Collections;
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
            .withRole("ADMIN")
            .build();
    }

    @Test
    void shouldValidateUser() {
        List<String> resultOfValidation = UserValidator.validate(null);
        assertEquals(Collections.singletonList("User cannot be null."), resultOfValidation);
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
            Arguments.of(null, Collections.singletonList("Name cannot be null.")),
            Arguments.of("", Collections.singletonList("Name must contain at least 1 character.")),
            Arguments.of("     ", Collections.singletonList("Name must contain at least 1 character.")),
            Arguments.of("Mary", Collections.emptyList()),
            Arguments.of("John", Collections.emptyList())
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
            Arguments.of(null, Collections.singletonList("Surname cannot be null.")),
            Arguments.of("", Collections.singletonList("Surname must contain at least 1 character.")),
            Arguments.of("     ", Collections.singletonList("Surname must contain at least 1 character.")),
            Arguments.of("Johnson", Collections.emptyList()),
            Arguments.of("Drake", Collections.emptyList())
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
            Arguments.of(null, Collections.singletonList("Email cannot be null.")),
            Arguments.of("", Collections.singletonList("Email does not match correct email pattern.")),
            Arguments.of("     ", Collections.singletonList("Email does not match correct email pattern.")),
            Arguments.of("alice.springfield.gmail.com     ", Collections.singletonList("Email does not match correct email pattern.")),
            Arguments.of("john.doe@yahoo,us", Collections.singletonList("Email does not match correct email pattern.")),
            Arguments.of("@gmail.com", Collections.singletonList("Email does not match correct email pattern.")),
            Arguments.of("john>smith@gmail.com", Collections.singletonList("Email does not match correct email pattern.")),
            Arguments.of("alice.springfield@platform.u", Collections.singletonList("Email does not match correct email pattern.")),
            Arguments.of("John.Smith@gmail.com", Collections.emptyList()),
            Arguments.of("johnsmith@gmail.com", Collections.emptyList()),
            Arguments.of("john-smith@yahoo.us", Collections.emptyList())
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
            Arguments.of(null, Collections.singletonList("Password cannot be null.")),
            Arguments.of("", Collections.singletonList("Password must contain at least 1 character.")),
            Arguments.of("     ", Collections.singletonList("Password must contain at least 1 character.")),
            Arguments.of("r5678$ikn^bg(yu", Collections.emptyList()),
            Arguments.of("pass123", Collections.emptyList())
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
            Arguments.of(null, Collections.singletonList("Job title cannot be null.")),
            Arguments.of("", Collections.singletonList("Job title must contain at least 1 character.")),
            Arguments.of("     ", Collections.singletonList("Job title must contain at least 1 character.")),
            Arguments.of("Math teacher", Collections.emptyList()),
            Arguments.of("IT administrator", Collections.emptyList())
        );
    }

    @Test
    void shouldValidateIsActiveIndex() {
        User userWithNullIsActiveIndex = User.builder()
            .withUser(correctUser)
            .withIsActive(null)
            .build();

        List<String> resultOfValidation = UserValidator.validate(userWithNullIsActiveIndex);

        assertEquals(Collections.singletonList("Is active index cannot be null."), resultOfValidation);
    }

    @Test
    void shouldValidatePosition() {
        User userWithNullPosition = User.builder()
            .withUser(correctUser)
            .withPosition(null)
            .build();

        List<String> resultOfValidation = UserValidator.validate(userWithNullPosition);

        assertEquals(Collections.singletonList("Position cannot be null."), resultOfValidation);
    }

    @ParameterizedTest
    @MethodSource("SetOfRolesAndValidationResults")
    void shouldValidateRole(String role, List<String> expected) {
        User userWithVariableRole = User.builder()
            .withUser(correctUser)
            .withRole(role)
            .build();

        List<String> resultOfValidation = UserValidator.validate(userWithVariableRole);

        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> SetOfRolesAndValidationResults() {
        return Stream.of(
            Arguments.of(null, Collections.singletonList("Role cannot be null.")),
            Arguments.of("", Collections.singletonList("Role must contain at least 1 character.")),
            Arguments.of("     ", Collections.singletonList("Role must contain at least 1 character.")),
            Arguments.of("Johnson", Collections.emptyList()),
            Arguments.of("Drake", Collections.emptyList())
        );
    }
}
