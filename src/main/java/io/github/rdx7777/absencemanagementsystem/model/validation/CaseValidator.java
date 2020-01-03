package io.github.rdx7777.absencemanagementsystem.model.validation;

import io.github.rdx7777.absencemanagementsystem.model.Case;
import io.github.rdx7777.absencemanagementsystem.model.PartDayType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CaseValidator extends Validator {

    public static List<String> validate(Case aCase) {
        if (aCase == null) {
            return Collections.singletonList("Case cannot be null.");
        }

        List<String> result = new ArrayList<>();

        addResultOfValidation(result, validateUserId(aCase.getUserId()));

        addResultOfValidation(result, validateHeadTeacherId(aCase.getHeadTeacherId()));

        String resultOfValidationStartDate = validateStartDate(aCase.getStartDate());
        addResultOfValidation(result, resultOfValidationStartDate);

        String resultOfValidationEndDate = validateEndDate(aCase.getEndDate());
        addResultOfValidation(result, resultOfValidationEndDate);

        if (resultOfValidationStartDate == null && resultOfValidationEndDate == null) {
            addResultOfValidation(result, validateRelationOfStartDateAndEndDate(aCase.getStartDate(), aCase.getEndDate()));
        }

        addResultOfValidation(result, validatePartDayType(aCase.getPartDayType()));

        addResultOfValidation(result, validateAbsenceReason(aCase.getAbsenceReason()));

        addResultOfValidation(result, validateIsCoverRequired(aCase.getIsCoverRequired()));

        addResultOfValidation(result, validateIsCoverProvided(aCase.getIsCoverProvided()));

        addResultOfValidation(result, validateIsApprovedByHeadTeacher(aCase.getIsApprovedByHeadTeacher()));

        addResultOfValidation(result, validateIsAbsencePaid(aCase.getIsAbsencePaid()));

        addResultOfValidation(result, validateIsCaseResolved(aCase.getIsCaseResolved()));

        return result;
    }

    private static String validateUserId(Long userId) {
        if (userId == null) {
            return "User id cannot be null.";
        }

        if (!(userId > 0)) {
            return "User id cannot be lower than or equal to zero.";
        }

        return null;
    }

    private static String validateHeadTeacherId(Long headTeacherId) {
        if (headTeacherId == null) {
            return "Head Teacher id cannot be null.";
        }

        if (!(headTeacherId > 0)) {
            return "Head Teacher id cannot be lower than or equal to zero.";
        }

        return null;
    }

    private static String validateStartDate(LocalDate startDate) {
        if (startDate == null) {
            return "Start date cannot be null.";
        }
        return null;
    }

    private static String validateEndDate(LocalDate endDate) {
        if (endDate == null) {
            return "End date cannot be null.";
        }
        return null;
    }

    private static String validateRelationOfStartDateAndEndDate(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            return "Start date must be earlier than end date.";
        }
        return null;
    }

    private static String validatePartDayType(PartDayType partDayType) {
        if (partDayType == null) {
            return "Part day type cannot be null.";
        }
        return null;
    }

    private static String validateAbsenceReason(String absenceReason) {
        if (absenceReason == null) {
            return "Absence reason cannot be null.";
        }

        if (absenceReason.trim().isEmpty()) {
            return "Absence reason must contain at least 1 character.";
        }

        return null;
    }

    private static String validateIsCoverRequired(Boolean isCoverRequired) {
        if (isCoverRequired == null) {
            return "Is cover required index cannot be null.";
        }
        return null;
    }

    private static String validateIsCoverProvided(Boolean isCoverProvided) {
        if (isCoverProvided == null) {
            return "Is cover provided index cannot be null.";
        }
        return null;
    }

    private static String validateIsApprovedByHeadTeacher(Boolean isApprovedByHeadTeacher) {
        if (isApprovedByHeadTeacher == null) {
            return "Is approved by Head Teacher index cannot be null.";
        }
        return null;
    }

    private static String validateIsAbsencePaid(Boolean isAbsencePaid) {
        if (isAbsencePaid == null) {
            return "Is absence paid index cannot be null.";
        }
        return null;
    }

    private static String validateIsCaseResolved(Boolean isCaseResolved) {
        if (isCaseResolved == null) {
            return "Is case resolved index cannot be null.";
        }
        return null;
    }
}
