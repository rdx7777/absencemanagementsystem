package io.github.rdx7777.absencemanagementsystem.model;

import io.github.rdx7777.absencemanagementsystem.repository.UserRepository;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class AppModelMapper {

    private UserRepository userRepository;

    public AppModelMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AbsenceCaseDTO mapToAbsenceCaseDTO(AbsenceCase absenceCase) {
        UserDTO userDTO = mapToUserDTO(absenceCase.getUser());
        UserDTO headTeacherDTO = mapToUserDTO(absenceCase.getHeadTeacher());
        return AbsenceCaseDTO.builder()
            .withId(absenceCase.getId())
            .withUser(userDTO)
            .withHeadTeacher(headTeacherDTO)
            .withStartDate(absenceCase.getStartDate())
            .withEndDate(absenceCase.getEndDate())
            .withPartDayType(absenceCase.getPartDayType())
            .withAbsenceReason(absenceCase.getAbsenceReason())
            .withUserComment(absenceCase.getUserComment())
            .withIsCoverRequired(absenceCase.getIsCoverRequired())
            .withIsCoverProvided(absenceCase.getIsCoverProvided())
            .withCoverSupervisorComment(absenceCase.getCoverSupervisorComment())
            .withIsApprovedByHeadTeacher(absenceCase.getIsApprovedByHeadTeacher())
            .withIsAbsencePaid(absenceCase.getIsAbsencePaid())
            .withHeadTeacherComment(absenceCase.getHeadTeacherComment())
            .withHrSupervisorComment(absenceCase.getHrSupervisorComment())
            .withIsCaseResolved(absenceCase.getIsCaseResolved())
            .build();
    }

    public UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
            .withId(user.getId())
            .withName(user.getName())
            .withSurname(user.getSurname())
            .withEmail(user.getEmail())
            .withJobTitle(user.getJobTitle())
            .withIsActive(user.getIsActive())
            .withPosition(user.getPosition())
            .withRole(user.getRole())
            .build();
    }

    public AbsenceCase mapToAbsenceCase(AbsenceCaseDTO caseDTO) {
        User user = mapToUser(caseDTO.getUser());
        User headTeacher = mapToUser(caseDTO.getHeadTeacher());
        return AbsenceCase.builder()
            .withId(caseDTO.getId())
            .withUser(user)
            .withHeadTeacher(headTeacher)
            .withStartDate(caseDTO.getStartDate())
            .withEndDate(caseDTO.getEndDate())
            .withPartDayType(caseDTO.getPartDayType())
            .withIsAbsencePaid(caseDTO.getIsAbsencePaid())
            .withUserComment(caseDTO.getUserComment())
            .withIsCoverRequired(caseDTO.getIsCoverRequired())
            .withIsCoverProvided(caseDTO.getIsCoverProvided())
            .withCoverSupervisorComment(caseDTO.getCoverSupervisorComment())
            .withIsApprovedByHeadTeacher(caseDTO.getIsApprovedByHeadTeacher())
            .withIsAbsencePaid(caseDTO.getIsAbsencePaid())
            .withHeadTeacherComment(caseDTO.getHeadTeacherComment())
            .withHrSupervisorComment(caseDTO.getHrSupervisorComment())
            .build();
    }

    // this method is called only when saving or updating absenceCase
    public User mapToUser(UserDTO userDTO) {
        Long id = userDTO.getId();
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }
}
