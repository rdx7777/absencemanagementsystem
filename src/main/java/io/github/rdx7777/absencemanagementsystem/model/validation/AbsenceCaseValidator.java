package io.github.rdx7777.absencemanagementsystem.model.validation;

import io.github.rdx7777.absencemanagementsystem.model.AbsenceCase;
import io.github.rdx7777.absencemanagementsystem.model.ActionStatus;
import io.github.rdx7777.absencemanagementsystem.model.PartDayType;
import io.github.rdx7777.absencemanagementsystem.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbsenceCaseValidator extends Validator {

    public static List<String> validate(AbsenceCase aCase) {
        if (aCase == null) {
            return Collections.singletonList("AbsenceCase cannot be null.");
        }

        List<String> result = new ArrayList<>();

        addResultOfValidation(result, validateUser(aCase.getUser()));

        addResultOfValidation(result, validateHeadTeacher(aCase.getHeadTeacher()));

        // TODO: (done) this validation's been removed, because createdDate is added automatically
//        addResultOfValidation(result, validateCreatedDate(aCase.getCreatedDate()));

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

        // TODO: (done) this validation's been removed, because resolvedDate is added automatically
//        addResultOfValidation(result, validateResolvedDate(aCase.getResolvedDate()));

        return result;
    }

    private static String validateUser(User user) {
        if (user == null) {
            return "User cannot be null.";
        }
        return null;
    }

    private static String validateHeadTeacher(User headTeacher) {
        if (headTeacher == null) {
            return "Head Teacher cannot be null.";
        }
        return null;
    }

    private static String validateCreatedDate(LocalDate createdDate) {
        if (createdDate == null) {
            return "Created date cannot be null.";
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

    private static String validateIsCoverRequired(ActionStatus isCoverRequired) {
        if (isCoverRequired == null) {
            return "Is cover required index cannot be null.";
        }
        return null;
    }

    private static String validateIsCoverProvided(ActionStatus isCoverProvided) {
        if (isCoverProvided == null) {
            return "Is cover provided index cannot be null.";
        }
        return null;
    }

    private static String validateIsApprovedByHeadTeacher(ActionStatus isApprovedByHeadTeacher) {
        if (isApprovedByHeadTeacher == null) {
            return "Is approved by Head Teacher index cannot be null.";
        }
        return null;
    }

    private static String validateIsAbsencePaid(ActionStatus isAbsencePaid) {
        if (isAbsencePaid == null) {
            return "Is absence paid index cannot be null.";
        }
        return null;
    }

    private static String validateIsCaseResolved(ActionStatus isCaseResolved) {
        if (isCaseResolved == null) {
            return "Is case resolved index cannot be null.";
        }
        return null;
    }

    private static String validateResolvedDate(LocalDate resolvedDate) {
        if (resolvedDate == null) {
            return "Resolved date cannot be null.";
        }
        return null;
    }
}
