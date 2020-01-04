package io.github.rdx7777.absencemanagementsystem.generators;

import io.github.rdx7777.absencemanagementsystem.model.Case;
import io.github.rdx7777.absencemanagementsystem.model.PartDayType;

import java.time.LocalDate;

public class CaseGenerator {

    public static Case getRandomCaseWithMorningPartDayType() {
        Long id = IdGenerator.getNextId();
        Long userId = IdGenerator.getNextId();
        Long headTeacherId = IdGenerator.getNextId();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        PartDayType type = PartDayType.Morning;
        String absenceReason = WordGenerator.getRandomWord();
        String userComment = WordGenerator.getRandomWord();
        Boolean isCoverRequired = false;
        Boolean isCoverProvided = false;
        String coverSupervisorComment = WordGenerator.getRandomWord();
        Boolean isApprovedByHeadTeacher = false;
        Boolean isAbsencePaid = false;
        String headTeacherComment = WordGenerator.getRandomWord();
        String hrSupervisorComment = WordGenerator.getRandomWord();
        Boolean isCaseResolved = false;

        return Case.builder()
            .withId(id)
            .withUserId(userId)
            .withHeadTeacherId(headTeacherId)
            .withStartDate(startDate)
            .withEndDate(endDate)
            .withPartDayType(type)
            .withAbsenceReason(absenceReason)
            .withUserComment(userComment)
            .withIsCoverRequired(isCoverRequired)
            .withIsCoverProvided(isCoverProvided)
            .withCoverSupervisorComment(coverSupervisorComment)
            .withIsApprovedByHeadTeacher(isApprovedByHeadTeacher)
            .withIsAbsencePaid(isAbsencePaid)
            .withHeadTeacherComment(headTeacherComment)
            .withHrSupervisorComment(hrSupervisorComment)
            .withIsCaseResolved(isCaseResolved)
            .build();
    }

    public static Case getRandomCaseWithAfternoonPartDayType() {
        Long id = IdGenerator.getNextId();
        Long userId = IdGenerator.getNextId();
        Long headTeacherId = IdGenerator.getNextId();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        PartDayType type = PartDayType.Afternoon;
        String absenceReason = WordGenerator.getRandomWord();
        String userComment = WordGenerator.getRandomWord();
        Boolean isCoverRequired = false;
        Boolean isCoverProvided = false;
        String coverSupervisorComment = WordGenerator.getRandomWord();
        Boolean isApprovedByHeadTeacher = false;
        Boolean isAbsencePaid = false;
        String headTeacherComment = WordGenerator.getRandomWord();
        String hrSupervisorComment = WordGenerator.getRandomWord();
        Boolean isCaseResolved = false;

        return Case.builder()
            .withId(id)
            .withUserId(userId)
            .withHeadTeacherId(headTeacherId)
            .withStartDate(startDate)
            .withEndDate(endDate)
            .withPartDayType(type)
            .withAbsenceReason(absenceReason)
            .withUserComment(userComment)
            .withIsCoverRequired(isCoverRequired)
            .withIsCoverProvided(isCoverProvided)
            .withCoverSupervisorComment(coverSupervisorComment)
            .withIsApprovedByHeadTeacher(isApprovedByHeadTeacher)
            .withIsAbsencePaid(isAbsencePaid)
            .withHeadTeacherComment(headTeacherComment)
            .withHrSupervisorComment(hrSupervisorComment)
            .withIsCaseResolved(isCaseResolved)
            .build();
    }

    public static Case getRandomCaseWithAllDayPartDayType() {
        Long id = IdGenerator.getNextId();
        Long userId = IdGenerator.getNextId();
        Long headTeacherId = IdGenerator.getNextId();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
        PartDayType type = PartDayType.AllDay;
        String absenceReason = WordGenerator.getRandomWord();
        String userComment = WordGenerator.getRandomWord();
        Boolean isCoverRequired = false;
        Boolean isCoverProvided = false;
        String coverSupervisorComment = WordGenerator.getRandomWord();
        Boolean isApprovedByHeadTeacher = false;
        Boolean isAbsencePaid = false;
        String headTeacherComment = WordGenerator.getRandomWord();
        String hrSupervisorComment = WordGenerator.getRandomWord();
        Boolean isCaseResolved = false;

        return Case.builder()
            .withId(id)
            .withUserId(userId)
            .withHeadTeacherId(headTeacherId)
            .withStartDate(startDate)
            .withEndDate(endDate)
            .withPartDayType(type)
            .withAbsenceReason(absenceReason)
            .withUserComment(userComment)
            .withIsCoverRequired(isCoverRequired)
            .withIsCoverProvided(isCoverProvided)
            .withCoverSupervisorComment(coverSupervisorComment)
            .withIsApprovedByHeadTeacher(isApprovedByHeadTeacher)
            .withIsAbsencePaid(isAbsencePaid)
            .withHeadTeacherComment(headTeacherComment)
            .withHrSupervisorComment(hrSupervisorComment)
            .withIsCaseResolved(isCaseResolved)
            .build();
    }

    public static Case getRandomCaseWithAllDayPartDayTypeAndSpecificId(Long id) {
        Long userId = IdGenerator.getNextId();
        Long headTeacherId = IdGenerator.getNextId();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
        PartDayType type = PartDayType.AllDay;
        String absenceReason = WordGenerator.getRandomWord();
        String userComment = WordGenerator.getRandomWord();
        Boolean isCoverRequired = false;
        Boolean isCoverProvided = false;
        String coverSupervisorComment = WordGenerator.getRandomWord();
        Boolean isApprovedByHeadTeacher = false;
        Boolean isAbsencePaid = false;
        String headTeacherComment = WordGenerator.getRandomWord();
        String hrSupervisorComment = WordGenerator.getRandomWord();
        Boolean isCaseResolved = false;

        return Case.builder()
            .withId(id)
            .withUserId(userId)
            .withHeadTeacherId(headTeacherId)
            .withStartDate(startDate)
            .withEndDate(endDate)
            .withPartDayType(type)
            .withAbsenceReason(absenceReason)
            .withUserComment(userComment)
            .withIsCoverRequired(isCoverRequired)
            .withIsCoverProvided(isCoverProvided)
            .withCoverSupervisorComment(coverSupervisorComment)
            .withIsApprovedByHeadTeacher(isApprovedByHeadTeacher)
            .withIsAbsencePaid(isAbsencePaid)
            .withHeadTeacherComment(headTeacherComment)
            .withHrSupervisorComment(hrSupervisorComment)
            .withIsCaseResolved(isCaseResolved)
            .build();
    }

    public static Case getRandomCaseWithAllDayPartDayTypeAndNullId() {
        Long userId = IdGenerator.getNextId();
        Long headTeacherId = IdGenerator.getNextId();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
        PartDayType type = PartDayType.AllDay;
        String absenceReason = WordGenerator.getRandomWord();
        String userComment = WordGenerator.getRandomWord();
        Boolean isCoverRequired = false;
        Boolean isCoverProvided = false;
        String coverSupervisorComment = WordGenerator.getRandomWord();
        Boolean isApprovedByHeadTeacher = false;
        Boolean isAbsencePaid = false;
        String headTeacherComment = WordGenerator.getRandomWord();
        String hrSupervisorComment = WordGenerator.getRandomWord();
        Boolean isCaseResolved = false;

        return Case.builder()
            .withUserId(userId)
            .withHeadTeacherId(headTeacherId)
            .withStartDate(startDate)
            .withEndDate(endDate)
            .withPartDayType(type)
            .withAbsenceReason(absenceReason)
            .withUserComment(userComment)
            .withIsCoverRequired(isCoverRequired)
            .withIsCoverProvided(isCoverProvided)
            .withCoverSupervisorComment(coverSupervisorComment)
            .withIsApprovedByHeadTeacher(isApprovedByHeadTeacher)
            .withIsAbsencePaid(isAbsencePaid)
            .withHeadTeacherComment(headTeacherComment)
            .withHrSupervisorComment(hrSupervisorComment)
            .withIsCaseResolved(isCaseResolved)
            .build();
    }

    public static Case getRandomCaseWithAllDayPartDayTypeAndSpecificUserId(Long userId) {
        Long id = IdGenerator.getNextId();
        Long headTeacherId = IdGenerator.getNextId();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
        PartDayType type = PartDayType.AllDay;
        String absenceReason = WordGenerator.getRandomWord();
        String userComment = WordGenerator.getRandomWord();
        Boolean isCoverRequired = false;
        Boolean isCoverProvided = false;
        String coverSupervisorComment = WordGenerator.getRandomWord();
        Boolean isApprovedByHeadTeacher = false;
        Boolean isAbsencePaid = false;
        String headTeacherComment = WordGenerator.getRandomWord();
        String hrSupervisorComment = WordGenerator.getRandomWord();
        Boolean isCaseResolved = false;

        return Case.builder()
            .withId(id)
            .withUserId(userId)
            .withHeadTeacherId(headTeacherId)
            .withStartDate(startDate)
            .withEndDate(endDate)
            .withPartDayType(type)
            .withAbsenceReason(absenceReason)
            .withUserComment(userComment)
            .withIsCoverRequired(isCoverRequired)
            .withIsCoverProvided(isCoverProvided)
            .withCoverSupervisorComment(coverSupervisorComment)
            .withIsApprovedByHeadTeacher(isApprovedByHeadTeacher)
            .withIsAbsencePaid(isAbsencePaid)
            .withHeadTeacherComment(headTeacherComment)
            .withHrSupervisorComment(hrSupervisorComment)
            .withIsCaseResolved(isCaseResolved)
            .build();
    }

    public static Case getRandomCaseWithAllDayPartDayTypeAndSpecificHeadTeacherId(Long headTeacherId) {
        Long id = IdGenerator.getNextId();
        Long userId = IdGenerator.getNextId();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
        PartDayType type = PartDayType.AllDay;
        String absenceReason = WordGenerator.getRandomWord();
        String userComment = WordGenerator.getRandomWord();
        Boolean isCoverRequired = false;
        Boolean isCoverProvided = false;
        String coverSupervisorComment = WordGenerator.getRandomWord();
        Boolean isApprovedByHeadTeacher = false;
        Boolean isAbsencePaid = false;
        String headTeacherComment = WordGenerator.getRandomWord();
        String hrSupervisorComment = WordGenerator.getRandomWord();
        Boolean isCaseResolved = false;

        return Case.builder()
            .withId(id)
            .withUserId(userId)
            .withHeadTeacherId(headTeacherId)
            .withStartDate(startDate)
            .withEndDate(endDate)
            .withPartDayType(type)
            .withAbsenceReason(absenceReason)
            .withUserComment(userComment)
            .withIsCoverRequired(isCoverRequired)
            .withIsCoverProvided(isCoverProvided)
            .withCoverSupervisorComment(coverSupervisorComment)
            .withIsApprovedByHeadTeacher(isApprovedByHeadTeacher)
            .withIsAbsencePaid(isAbsencePaid)
            .withHeadTeacherComment(headTeacherComment)
            .withHrSupervisorComment(hrSupervisorComment)
            .withIsCaseResolved(isCaseResolved)
            .build();
    }
}
