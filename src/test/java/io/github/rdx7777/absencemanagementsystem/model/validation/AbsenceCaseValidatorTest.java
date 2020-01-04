package io.github.rdx7777.absencemanagementsystem.model.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.rdx7777.absencemanagementsystem.model.AbsenceCase;
import io.github.rdx7777.absencemanagementsystem.model.PartDayType;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AbsenceCaseValidatorTest {

    private AbsenceCase correctCase;

    @BeforeEach
    void setup() {
        correctCase = AbsenceCase.builder()
            .withId(1000L)
            .withUserId(1000L)
            .withHeadTeacherId(1000L)
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
            .build();
    }

    @Test
    void shouldValidateCase() {
        List<String> resultOfValidation = AbsenceCaseValidator.validate(null);
        assertEquals(Arrays.asList("AbsenceCase cannot be null."), resultOfValidation);
    }

    @ParameterizedTest
    @MethodSource("SetOfUserIdsAndValidationResults")
    void shouldValidateUserId(Long userId, List<String> expected) {
//        AbsenceCase caseWithVariableUserId = AbsenceCase.builder()
//            .withId(1L)
//            .withUserId(userId)
//            .withHeadTeacherId(10L)
//            .withStartDate(LocalDate.of(2019, 10, 10))
//            .withEndDate(LocalDate.of(2019, 10, 12))
//            .withPartDayType(PartDayType.AllDay)
//            .withAbsenceReason("Sickness.")
//            .withUserComment("Flu.")
//            .withIsCoverRequired(true)
//            .withIsCoverProvided(true)
//            .withCoverSupervisorComment("Cover teacher name: John Lennon.")
//            .withIsApprovedByHeadTeacher(true)
//            .withIsAbsencePaid(true)
//            .withHeadTeacherComment("No comment.")
//            .withHrSupervisorComment("No comment.")
//            .withIsCaseResolved(true)
//            .build();

        AbsenceCase caseWithVariableUserId = AbsenceCase.builder()
            .withCase(correctCase)
            .withUserId(userId)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithVariableUserId);

        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> SetOfUserIdsAndValidationResults() {
        return Stream.of(
            Arguments.of(null, Arrays.asList("User id cannot be null.")),
            Arguments.of(-1L, Arrays.asList("User id cannot be lower than or equal to zero.")),
            Arguments.of(1L, Arrays.asList())
        );
    }

    @ParameterizedTest
    @MethodSource("SetOfHeadTeacherIdsAndValidationResults")
    void shouldValidateHeadTeacherId(Long headTeacherId, List<String> expected) {
        AbsenceCase caseWithVariableHeadTeacherId = AbsenceCase.builder()
            .withCase(correctCase)
            .withHeadTeacherId(headTeacherId)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithVariableHeadTeacherId);

        assertEquals(expected, resultOfValidation);
    }

    private static Stream<Arguments> SetOfHeadTeacherIdsAndValidationResults() {
        return Stream.of(
            Arguments.of(null, Arrays.asList("Head Teacher id cannot be null.")),
            Arguments.of(-1L, Arrays.asList("Head Teacher id cannot be lower than or equal to zero.")),
            Arguments.of(1L, Arrays.asList())
        );
    }

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
            Arguments.of(null, Arrays.asList("Start date cannot be null.")),
            Arguments.of(LocalDate.of(2019, 01, 10), Arrays.asList()),
            Arguments.of(LocalDate.now(), Arrays.asList())
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
            Arguments.of(null, Arrays.asList("End date cannot be null.")),
            Arguments.of(LocalDate.of(2020, 01, 15), Arrays.asList()),
            Arguments.of(LocalDate.now(), Arrays.asList())
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
                Arrays.asList("Start date must be earlier than end date.")),
            Arguments.of(LocalDate.of(2020, 01, 15), LocalDate.of(2019, 12, 31),
                Arrays.asList("Start date must be earlier than end date.")),
            Arguments.of(LocalDate.now(), LocalDate.now().minusDays(2),
                Arrays.asList("Start date must be earlier than end date.")),
            Arguments.of(LocalDate.of(2019, 10, 15), LocalDate.of(2019, 10, 25),
                Arrays.asList()),
            Arguments.of(LocalDate.of(2025, 05, 11), LocalDate.of(2026, 02, 13),
                Arrays.asList()),
            Arguments.of(LocalDate.now(), LocalDate.now().plusDays(2),
                Arrays.asList())
        );
    }

    @Test
    void shouldValidatePartDayType() {
        AbsenceCase caseWithNullPartDayType = AbsenceCase.builder()
            .withCase(correctCase)
            .withPartDayType(null)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithNullPartDayType);

        assertEquals(Arrays.asList("Part day type cannot be null."), resultOfValidation);
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
            Arguments.of(null, Arrays.asList("Absence reason cannot be null.")),
            Arguments.of("", Arrays.asList("Absence reason must contain at least 1 character.")),
            Arguments.of("     ", Arrays.asList("Absence reason must contain at least 1 character.")),
            Arguments.of("Winter holiday.", Arrays.asList()),
            Arguments.of("Child sickness.", Arrays.asList())
        );
    }

    @Test
    void shouldValidateIsCoverRequiredIndex() {
        AbsenceCase caseWithNullIsCoverRequiredIndex = AbsenceCase.builder()
            .withCase(correctCase)
            .withIsCoverRequired(null)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithNullIsCoverRequiredIndex);

        assertEquals(Arrays.asList("Is cover required index cannot be null."), resultOfValidation);
    }

    @Test
    void shouldValidateIsCoverProvidedIndex() {
        AbsenceCase caseWithNullIsCoverProvidedIndex = AbsenceCase.builder()
            .withCase(correctCase)
            .withIsCoverProvided(null)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithNullIsCoverProvidedIndex);

        assertEquals(Arrays.asList("Is cover provided index cannot be null."), resultOfValidation);
    }

    @Test
    void shouldValidateIsApprovedByHeadTeacherIndex() {
        AbsenceCase caseWithNullIsApprovedByHeadTeacherIndex = AbsenceCase.builder()
            .withCase(correctCase)
            .withIsApprovedByHeadTeacher(null)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithNullIsApprovedByHeadTeacherIndex);

        assertEquals(Arrays.asList("Is approved by Head Teacher index cannot be null."), resultOfValidation);
    }

    @Test
    void shouldValidateIsAbsencePaidIndex() {
        AbsenceCase caseWithNullIsAbsencePaidIndex = AbsenceCase.builder()
            .withCase(correctCase)
            .withIsAbsencePaid(null)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithNullIsAbsencePaidIndex);

        assertEquals(Arrays.asList("Is absence paid index cannot be null."), resultOfValidation);
    }

    @Test
    void shouldValidateIsCaseResolvedIndex() {
        AbsenceCase caseWithNullIsCaseResolvedIndex = AbsenceCase.builder()
            .withCase(correctCase)
            .withIsCaseResolved(null)
            .build();

        List<String> resultOfValidation = AbsenceCaseValidator.validate(caseWithNullIsCaseResolvedIndex);

        assertEquals(Arrays.asList("Is case resolved index cannot be null."), resultOfValidation);
    }
}
