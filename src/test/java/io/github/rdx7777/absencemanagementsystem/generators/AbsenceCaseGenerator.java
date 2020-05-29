package io.github.rdx7777.absencemanagementsystem.generators;

import io.github.rdx7777.absencemanagementsystem.model.AbsenceCase;
import io.github.rdx7777.absencemanagementsystem.model.PartDayType;
import io.github.rdx7777.absencemanagementsystem.model.User;

import java.time.LocalDate;

public class AbsenceCaseGenerator {

    public static AbsenceCase getRandomCaseWithMorningPartDayType() {
        Long id = IdGenerator.getNextId();
        User user = UserGenerator.getRandomEmployee();
        User headTeacher = UserGenerator.getRandomHeadTeacher();
        LocalDate createdDate = LocalDate.now();
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
        LocalDate resolvedDate = LocalDate.now();

        return AbsenceCase.builder()
            .withId(id)
            .withUser(user)
            .withHeadTeacher(headTeacher)
            .withCreatedDate(createdDate)
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
            .withResolvedDate(resolvedDate)
            .build();
    }

    public static AbsenceCase getRandomCaseWithAfternoonPartDayType() {
        Long id = IdGenerator.getNextId();
        User user = UserGenerator.getRandomEmployee();
        User headTeacher = UserGenerator.getRandomHeadTeacher();
        LocalDate createdDate = LocalDate.now();
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
        LocalDate resolvedDate = LocalDate.now();

        return AbsenceCase.builder()
            .withId(id)
            .withUser(user)
            .withHeadTeacher(headTeacher)
            .withCreatedDate(createdDate)
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
            .withResolvedDate(resolvedDate)
            .build();
    }

    public static AbsenceCase getRandomCaseWithAllDayPartDayType() {
        Long id = IdGenerator.getNextId();
        User user = UserGenerator.getRandomEmployee();
        User headTeacher = UserGenerator.getRandomHeadTeacher();
        LocalDate createdDate = LocalDate.now();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
        PartDayType partDayType = PartDayType.AllDay;
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
        LocalDate resolvedDate = LocalDate.now();

        return AbsenceCase.builder()
            .withId(id)
            .withUser(user)
            .withHeadTeacher(headTeacher)
            .withCreatedDate(createdDate)
            .withStartDate(startDate)
            .withEndDate(endDate)
            .withPartDayType(partDayType)
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
            .withResolvedDate(resolvedDate)
            .build();
    }

    public static AbsenceCase getRandomCaseWithAllDayPartDayTypeAndSpecificId(Long id) {
        User user = UserGenerator.getRandomEmployee();
        User headTeacher = UserGenerator.getRandomHeadTeacher();
        LocalDate createdDate = LocalDate.now();
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
        LocalDate resolvedDate = LocalDate.now();

        return AbsenceCase.builder()
            .withId(id)
            .withUser(user)
            .withHeadTeacher(headTeacher)
            .withCreatedDate(createdDate)
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
            .withResolvedDate(resolvedDate)
            .build();
    }

    public static AbsenceCase getRandomCaseWithAllDayPartDayTypeAndNullId() {
        User user = UserGenerator.getRandomEmployee();
        User headTeacher = UserGenerator.getRandomHeadTeacher();
        LocalDate createdDate = LocalDate.now();
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
        LocalDate resolvedDate = LocalDate.now();

        return AbsenceCase.builder()
            .withUser(user)
            .withHeadTeacher(headTeacher)
            .withCreatedDate(createdDate)
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
            .withResolvedDate(resolvedDate)
            .build();
    }

    public static AbsenceCase getRandomCaseWithAllDayPartDayTypeAndSpecificUserId(Long userId) {
        Long id = IdGenerator.getNextId();
        User headTeacher = UserGenerator.getRandomHeadTeacher();
        LocalDate createdDate = LocalDate.now();
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
        LocalDate resolvedDate = LocalDate.now();

        return AbsenceCase.builder()
            .withId(id)
            .withUser(UserGenerator.getRandomEmployeeWithSpecificId(userId))
            .withHeadTeacher(headTeacher)
            .withCreatedDate(createdDate)
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
            .withResolvedDate(resolvedDate)
            .build();
    }

    public static AbsenceCase getRandomCaseWithAllDayPartDayTypeAndSpecificHeadTeacherId(Long headTeacherId) {
        Long id = IdGenerator.getNextId();
        User user = UserGenerator.getRandomEmployee();
        LocalDate createdDate = LocalDate.now();
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
        LocalDate resolvedDate = LocalDate.now();

        return AbsenceCase.builder()
            .withId(id)
            .withUser(user)
            .withHeadTeacher(UserGenerator.getRandomHeadTeacherWithSpecificId(headTeacherId))
            .withCreatedDate(createdDate)
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
            .withResolvedDate(resolvedDate)
            .build();
    }
}
