package io.github.rdx7777.absencemanagementsystem.model.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.rdx7777.absencemanagementsystem.model.AbsenceCase;
import io.github.rdx7777.absencemanagementsystem.model.PartDayType;
import io.github.rdx7777.absencemanagementsystem.model.Position;
import io.github.rdx7777.absencemanagementsystem.model.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AbsenceCaseValidatorTest {

    private static AbsenceCase correctCase;
    private static User correctUser;
    private static User correctHeadTeacher;

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
        correctHeadTeacher = User.builder().withUser(correctUser).withId(200L).withPosition(Position.HeadTeacher).build();
        correctCase = AbsenceCase.builder()
            .withId(1000L)
            .withUser(correctUser)
            .withHeadTeacher(correctHeadTeacher)
            .withCreatedDate(LocalDate.of(2019, 01, 01))
            .withStartDate(LocalDate.of(2019, 01, 01))
            .withEndDate(LocalDate.of(2099, 12, 31))
            .withPartDayType(PartDayType.AllDay)
            .withAbsenceReason("Sickness.")
            .withUserComment("Flu.")
            .withIsCoverRequired(true)
            .withIsCoverProvided(true)
            .withCoverSupervisorComment("Cover teacher name: John Lennon.")
            .withIsApprovedByHeadTeacher(true)
            .withIsAbsencePaid(true)
            .withHeadTeacherComment("No comment.")
            .withHrSupervisorComment("No comment.")
            .withIsCaseResolved(true)
            .withResolvedDate(LocalDate.of(2099, 12, 31))
            .build();
    }

    @Test
    void shouldValidateCase() {
        List<String> resultOfValidation = AbsenceCaseValidator.validate(null);
        assertEquals(Collections.singletonList("AbsenceCase cannot be null."), resultOfValidation);
    }

    @ParameterizedTest
    @MethodSource("SetOfUsersAndValidationResults")
    void shouldValidateUser(User user, List<String> expected) {
        AbsenceCase caseWithVariableUser = AbsenceCase.builder()
            .withCase(correctCase)
            .withUser(user)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithVariableUser);

        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> SetOfUsersAndValidationResults() {
        return Stream.of(
            Arguments.of(null, Collections.singletonList("User cannot be null.")),
            Arguments.of(correctUser, Collections.emptyList())
        );
    }

    @ParameterizedTest
    @MethodSource("SetOfHeadTeachersAndValidationResults")
    void shouldValidateHeadTeacherId(User headTeacher, List<String> expected) {
        AbsenceCase caseWithVariableHeadTeacher = AbsenceCase.builder()
            .withCase(correctCase)
            .withHeadTeacher(headTeacher)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithVariableHeadTeacher);

        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> SetOfHeadTeachersAndValidationResults() {
        return Stream.of(
            Arguments.of(null, Collections.singletonList("Head Teacher cannot be null.")),
            Arguments.of(correctHeadTeacher, Collections.emptyList())
        );
    }

    // TODO: (done) validation method is unnecessary, this test's been removed
    /*@ParameterizedTest
    @MethodSource("SetOfCreatedDatesAndValidationResults")
    void shouldValidateCreatedDate(LocalDate createdDate, List<String> expected) {
        AbsenceCase caseWithVariableCreatedDate = AbsenceCase.builder()
            .withCase(correctCase)
            .withCreatedDate(createdDate)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithVariableCreatedDate);

        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> SetOfCreatedDatesAndValidationResults() {
        return Stream.of(
            Arguments.of(null, Collections.singletonList("Created date cannot be null.")),
            Arguments.of(LocalDate.of(2019, 1, 10), Collections.emptyList()),
            Arguments.of(LocalDate.now(), Collections.emptyList())
        );
    }*/

    @ParameterizedTest
    @MethodSource("SetOfStartDatesAndValidationResults")
    void shouldValidateStartDate(LocalDate startDate, List<String> expected) {
        AbsenceCase caseWithVariableStartDate = AbsenceCase.builder()
            .withCase(correctCase)
            .withStartDate(startDate)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithVariableStartDate);

        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> SetOfStartDatesAndValidationResults() {
        return Stream.of(
            Arguments.of(null, Collections.singletonList("Start date cannot be null.")),
            Arguments.of(LocalDate.of(2019, 1, 10), Collections.emptyList()),
            Arguments.of(LocalDate.now(), Collections.emptyList())
        );
    }

    @ParameterizedTest
    @MethodSource("SetOfEndDatesAndValidationResults")
    void shouldValidateEndDate(LocalDate endDate, List<String> expected) {
        AbsenceCase caseWithVariableEndDate = AbsenceCase.builder()
            .withCase(correctCase)
            .withEndDate(endDate)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithVariableEndDate);

        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> SetOfEndDatesAndValidationResults() {
        return Stream.of(
            Arguments.of(null, Collections.singletonList("End date cannot be null.")),
            Arguments.of(LocalDate.of(2020, 1, 15), Collections.emptyList()),
            Arguments.of(LocalDate.now(), Collections.emptyList())
        );
    }

    @ParameterizedTest
    @MethodSource("setOfRelationsBetweenStartDateEndDateAndValidationResults")
    void shouldValidateRelationOfStartDateAndEndDate(LocalDate startDate, LocalDate endDate, List<String> expected) {
        AbsenceCase caseWithDatesVariable = AbsenceCase.builder()
            .withCase(correctCase)
            .withStartDate(startDate)
            .withEndDate(endDate)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithDatesVariable);

        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> setOfRelationsBetweenStartDateEndDateAndValidationResults() {
        return Stream.of(
            Arguments.of(LocalDate.of(2019, 10, 15), LocalDate.of(2019, 10, 10),
                Collections.singletonList("Start date must be earlier than end date.")),
            Arguments.of(LocalDate.of(2020, 1, 15), LocalDate.of(2019, 12, 31),
                Collections.singletonList("Start date must be earlier than end date.")),
            Arguments.of(LocalDate.now(), LocalDate.now().minusDays(2),
                Collections.singletonList("Start date must be earlier than end date.")),
            Arguments.of(LocalDate.of(2019, 10, 15), LocalDate.of(2019, 10, 25),
                Collections.emptyList()),
            Arguments.of(LocalDate.of(2025, 5, 11), LocalDate.of(2026, 2, 13),
                Collections.emptyList()),
            Arguments.of(LocalDate.now(), LocalDate.now().plusDays(2),
                Collections.emptyList())
        );
    }

    @Test
    void shouldValidatePartDayType() {
        AbsenceCase caseWithNullPartDayType = AbsenceCase.builder()
            .withCase(correctCase)
            .withPartDayType(null)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithNullPartDayType);

        assertEquals(Collections.singletonList("Part day type cannot be null."), resultOfValidation);
    }

    @ParameterizedTest
    @MethodSource("SetOfAbsenceReasonsAndValidationResults")
    void shouldValidateAbsenceReason(String absenceReason, List<String> expected) {
        AbsenceCase caseWithVariableAbsenceReason = AbsenceCase.builder()
            .withCase(correctCase)
            .withAbsenceReason(absenceReason)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithVariableAbsenceReason);

        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> SetOfAbsenceReasonsAndValidationResults() {
        return Stream.of(
            Arguments.of(null, Collections.singletonList("Absence reason cannot be null.")),
            Arguments.of("", Collections.singletonList("Absence reason must contain at least 1 character.")),
            Arguments.of("     ", Collections.singletonList("Absence reason must contain at least 1 character.")),
            Arguments.of("Winter holiday.", Collections.emptyList()),
            Arguments.of("Child sickness.", Collections.emptyList())
        );
    }

    @Test
    void shouldValidateIsCoverRequiredIndex() {
        AbsenceCase caseWithNullIsCoverRequiredIndex = AbsenceCase.builder()
            .withCase(correctCase)
            .withIsCoverRequired(null)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithNullIsCoverRequiredIndex);

        assertEquals(Collections.singletonList("Is cover required index cannot be null."), resultOfValidation);
    }

    @Test
    void shouldValidateIsCoverProvidedIndex() {
        AbsenceCase caseWithNullIsCoverProvidedIndex = AbsenceCase.builder()
            .withCase(correctCase)
            .withIsCoverProvided(null)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithNullIsCoverProvidedIndex);

        assertEquals(Collections.singletonList("Is cover provided index cannot be null."), resultOfValidation);
    }

    @Test
    void shouldValidateIsApprovedByHeadTeacherIndex() {
        AbsenceCase caseWithNullIsApprovedByHeadTeacherIndex = AbsenceCase.builder()
            .withCase(correctCase)
            .withIsApprovedByHeadTeacher(null)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithNullIsApprovedByHeadTeacherIndex);

        assertEquals(Collections.singletonList("Is approved by Head Teacher index cannot be null."), resultOfValidation);
    }

    @Test
    void shouldValidateIsAbsencePaidIndex() {
        AbsenceCase caseWithNullIsAbsencePaidIndex = AbsenceCase.builder()
            .withCase(correctCase)
            .withIsAbsencePaid(null)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithNullIsAbsencePaidIndex);

        assertEquals(Collections.singletonList("Is absence paid index cannot be null."), resultOfValidation);
    }

    @Test
    void shouldValidateIsCaseResolvedIndex() {
        AbsenceCase caseWithNullIsCaseResolvedIndex = AbsenceCase.builder()
            .withCase(correctCase)
            .withIsCaseResolved(null)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithNullIsCaseResolvedIndex);

        assertEquals(Collections.singletonList("Is case resolved index cannot be null."), resultOfValidation);
    }

    // TODO: (done) validation method is unnecessary, this test's been removed
/*    @ParameterizedTest
    @MethodSource("SetOfResolvedDatesAndValidationResults")
    void shouldValidateResolvedDate(LocalDate resolvedDate, List<String> expected) {
        AbsenceCase caseWithVariableResolvedDate = AbsenceCase.builder()
            .withCase(correctCase)
            .withResolvedDate(resolvedDate)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithVariableResolvedDate);

        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> SetOfResolvedDatesAndValidationResults() {
        return Stream.of(
            Arguments.of(null, Collections.singletonList("Resolved date cannot be null.")),
            Arguments.of(LocalDate.of(2019, 1, 10), Collections.emptyList()),
            Arguments.of(LocalDate.now(), Collections.emptyList())
        );
    }*/
}
