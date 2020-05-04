package io.github.rdx7777.absencemanagementsystem;

import io.github.rdx7777.absencemanagementsystem.model.AbsenceCase;
import io.github.rdx7777.absencemanagementsystem.model.PartDayType;
import io.github.rdx7777.absencemanagementsystem.model.Position;
import io.github.rdx7777.absencemanagementsystem.model.User;
import io.github.rdx7777.absencemanagementsystem.service.AbsenceCaseService;
import io.github.rdx7777.absencemanagementsystem.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements CommandLineRunner {

    private final AbsenceCaseService caseService;
    private final UserService userService;

    @Autowired
    public Initializer(AbsenceCaseService caseService, UserService userService) {
        this.caseService = caseService;
        this.userService = userService;
    }

    @Override
    public void run(String... strings) throws Exception {
        List<AbsenceCase> caseList = new ArrayList<>(caseService.getAllCases());
        if (caseList.size() > 0) {
            for (AbsenceCase absenceCase : caseList) {
                caseService.deleteCase(absenceCase.getId());
            }
        }
        List<User> userList = new ArrayList<>(userService.getAllUsers());
        if (userList.size() > 0) {
            for (User user : userList) {
                userService.deleteUser(user.getId());
            }
        }
        User firstUserToSave = User.builder()
            .withName("Alice")
            .withSurname("Springfield")
            .withEmail("rdx7777.test@gmail.com")
            .withPassword("test")
            .withJobTitle("Math Teacher")
            .withIsActive(true)
            .withPosition(Position.Employee)
            .withRole("ROLE_USER")
            .build();
        User secondUserToSave = User.builder()
            .withName("Jim")
            .withSurname("Morrison")
            .withEmail("rdx7777.test1@gmail.com")
            .withPassword("test")
            .withJobTitle("English Teacher")
            .withIsActive(true)
            .withPosition(Position.Employee)
            .withRole("ROLE_USER")
            .build();
        User thirdUserToSave = User.builder()
            .withName("Kate")
            .withSurname("Bloom")
            .withEmail("rdx7777.test2@gmail.com")
            .withPassword("test")
            .withJobTitle("Cover Supervisor")
            .withIsActive(true)
            .withPosition(Position.CoverSupervisor)
            .withRole("ROLE_CS_SUPERVISOR")
            .build();
        User fourthUserToSave = User.builder()
            .withName("Janet")
            .withSurname("Barney")
            .withEmail("rdx7777.test3@gmail.com")
            .withPassword("test")
            .withJobTitle("Head Teacher - Maths & Science")
            .withIsActive(true)
            .withPosition(Position.HeadTeacher)
            .withRole("ROLE_HEAD_TEACHER")
            .build();
        User fifthUserToSave = User.builder()
            .withName("Leila")
            .withSurname("Smith")
            .withEmail("rdx7777.test4@gmail.com")
            .withPassword("test")
            .withJobTitle("Head Teacher - Languages")
            .withIsActive(true)
            .withPosition(Position.HeadTeacher)
            .withRole("ROLE_HEAD_TEACHER")
            .build();
        User sixthUserToSave = User.builder()
            .withName("Mark")
            .withSurname("Thompson")
            .withEmail("rdx7777.test5@gmail.com")
            .withPassword("test")
            .withJobTitle("HR Supervisor")
            .withIsActive(true)
            .withPosition(Position.HumanResourcesSupervisor)
            .withRole("ROLE_HR_SUPERVISOR")
            .build();
        User adminToSave = User.builder()
            .withName("Rdx")
            .withSurname("Jxxx")
            .withEmail("radek.jerzynski@gmail.com")
            .withPassword("test")
            .withJobTitle("Admin")
            .withIsActive(true)
            .withPosition(Position.Employee)
            .withRole("ROLE_ADMIN")
            .build();

        User firstUser = userService.addUser(firstUserToSave);
        User secondUser = userService.addUser(secondUserToSave);
        User thirdUser = userService.addUser(fourthUserToSave);
        User fourthUser = userService.addUser(fifthUserToSave);
        User fifthUser = userService.addUser(thirdUserToSave);
        User sixthUser = userService.addUser(sixthUserToSave);
        User admin = userService.addUser(adminToSave);

        AbsenceCase firstCase = AbsenceCase.builder()
            .withUser(firstUser)
            .withHeadTeacher(fourthUser)
            .withStartDate(LocalDate.of(2020, 1, 5))
            .withEndDate(LocalDate.of(2020, 1, 20))
            .withPartDayType(PartDayType.AllDay)
            .withAbsenceReason("Private 1.")
            .withIsCoverRequired(false)
            .withIsCoverProvided(false)
            .withIsApprovedByHeadTeacher(false)
            .withIsAbsencePaid(false)
            .withIsCaseResolved(true)
            .build();
        AbsenceCase secondCase = AbsenceCase.builder()
            .withUser(secondUser)
            .withHeadTeacher(fifthUser)
            .withStartDate(LocalDate.of(2020, 2, 1))
            .withEndDate(LocalDate.of(2020, 2, 4))
            .withPartDayType(PartDayType.AllDay)
            .withAbsenceReason("Private 2.")
            .withUserComment("Flu.")
            .withIsCoverRequired(true)
            .withIsCoverProvided(true)
            .withIsApprovedByHeadTeacher(true)
            .withIsAbsencePaid(true)
            .withIsCaseResolved(true)
            .build();
        AbsenceCase thirdCase = AbsenceCase.builder()
            .withUser(firstUser)
            .withHeadTeacher(fourthUser)
            .withStartDate(LocalDate.of(2020, 3, 11))
            .withEndDate(LocalDate.of(2020, 3, 20))
            .withPartDayType(PartDayType.AllDay)
            .withAbsenceReason("Private 3.")
            .withIsCoverRequired(true)
            .withIsCoverProvided(true)
            .withIsApprovedByHeadTeacher(true)
            .withIsAbsencePaid(true)
            .withIsCaseResolved(true)
            .build();
        AbsenceCase fourthCase = AbsenceCase.builder()
            .withUser(firstUser)
            .withHeadTeacher(fourthUser)
            .withStartDate(LocalDate.of(2020, 3, 12))
            .withEndDate(LocalDate.of(2020, 3, 20))
            .withPartDayType(PartDayType.AllDay)
            .withAbsenceReason("Private 4.")
            .withIsCoverRequired(true)
            .withIsCoverProvided(true)
            .withIsApprovedByHeadTeacher(true)
            .withIsAbsencePaid(true)
            .withIsCaseResolved(true)
            .build();
        AbsenceCase fifthCase = AbsenceCase.builder()
            .withUser(firstUser)
            .withHeadTeacher(fourthUser)
            .withStartDate(LocalDate.of(2020, 3, 13))
            .withEndDate(LocalDate.of(2020, 3, 20))
            .withPartDayType(PartDayType.AllDay)
            .withAbsenceReason("Private 5.")
            .withIsCoverRequired(true)
            .withIsCoverProvided(true)
            .withIsApprovedByHeadTeacher(true)
            .withIsAbsencePaid(true)
            .withIsCaseResolved(true)
            .build();
        AbsenceCase sixthCase = AbsenceCase.builder()
            .withUser(firstUser)
            .withHeadTeacher(fourthUser)
            .withStartDate(LocalDate.of(2020, 3, 14))
            .withEndDate(LocalDate.of(2020, 3, 20))
            .withPartDayType(PartDayType.AllDay)
            .withAbsenceReason("Private 6.")
            .withIsCoverRequired(true)
            .withIsCoverProvided(true)
            .withIsApprovedByHeadTeacher(true)
            .withIsAbsencePaid(true)
            .withIsCaseResolved(true)
            .build();
        AbsenceCase seventhCase = AbsenceCase.builder()
            .withUser(firstUser)
            .withHeadTeacher(fourthUser)
            .withStartDate(LocalDate.of(2020, 3, 15))
            .withEndDate(LocalDate.of(2020, 3, 20))
            .withPartDayType(PartDayType.AllDay)
            .withAbsenceReason("Private 7.")
            .withIsCoverRequired(true)
            .withIsCoverProvided(true)
            .withIsApprovedByHeadTeacher(true)
            .withIsAbsencePaid(true)
            .withIsCaseResolved(true)
            .build();
        AbsenceCase eighthCase = AbsenceCase.builder()
            .withUser(firstUser)
            .withHeadTeacher(fourthUser)
            .withStartDate(LocalDate.of(2020, 3, 16))
            .withEndDate(LocalDate.of(2020, 3, 20))
            .withPartDayType(PartDayType.AllDay)
            .withAbsenceReason("Private 8.")
            .withIsCoverRequired(true)
            .withIsCoverProvided(true)
            .withIsApprovedByHeadTeacher(true)
            .withIsAbsencePaid(true)
            .withIsCaseResolved(false)
            .build();
        AbsenceCase ninthCase = AbsenceCase.builder()
            .withUser(firstUser)
            .withHeadTeacher(fourthUser)
            .withStartDate(LocalDate.of(2020, 3, 17))
            .withEndDate(LocalDate.of(2020, 3, 20))
            .withPartDayType(PartDayType.AllDay)
            .withAbsenceReason("Private 9.")
            .withIsCoverRequired(true)
            .withIsCoverProvided(true)
            .withIsApprovedByHeadTeacher(true)
            .withIsAbsencePaid(true)
            .withIsCaseResolved(true)
            .build();
        AbsenceCase tenthCase = AbsenceCase.builder()
            .withUser(firstUser)
            .withHeadTeacher(fourthUser)
            .withStartDate(LocalDate.of(2020, 3, 18))
            .withEndDate(LocalDate.of(2020, 3, 20))
            .withPartDayType(PartDayType.AllDay)
            .withAbsenceReason("Private 10.")
            .withIsCoverRequired(true)
            .withIsCoverProvided(true)
            .withIsApprovedByHeadTeacher(true)
            .withIsAbsencePaid(true)
            .withIsCaseResolved(true)
            .build();
        AbsenceCase a1 = AbsenceCase.builder()
            .withUser(firstUser)
            .withHeadTeacher(fourthUser)
            .withStartDate(LocalDate.of(2020, 1, 12))
            .withEndDate(LocalDate.of(2020, 1, 20))
            .withPartDayType(PartDayType.AllDay)
            .withAbsenceReason("Holiday.")
            .withIsCoverRequired(false)
            .withIsCoverProvided(false)
            .withIsApprovedByHeadTeacher(false)
            .withIsAbsencePaid(false)
            .withIsCaseResolved(false)
            .build();
        AbsenceCase a2 = AbsenceCase.builder()
            .withCase(a1)
            .withStartDate(LocalDate.of(2020, 1, 13))
            .build();
        AbsenceCase a3 = AbsenceCase.builder()
            .withCase(a1)
            .withStartDate(LocalDate.of(2020, 1, 14))
            .build();
        AbsenceCase a4 = AbsenceCase.builder()
            .withCase(a1)
            .withStartDate(LocalDate.of(2020, 1, 15))
            .build();
        AbsenceCase a5 = AbsenceCase.builder()
            .withCase(a1)
            .withStartDate(LocalDate.of(2020, 1, 16))
            .build();
        AbsenceCase a6 = AbsenceCase.builder()
            .withCase(a1)
            .withStartDate(LocalDate.of(2020, 1, 17))
            .build();
        AbsenceCase a7 = AbsenceCase.builder()
            .withCase(a1)
            .withStartDate(LocalDate.of(2020, 1, 18))
            .build();
        AbsenceCase a8 = AbsenceCase.builder()
            .withCase(a1)
            .withStartDate(LocalDate.of(2020, 1, 19))
            .build();
        AbsenceCase a9 = AbsenceCase.builder()
            .withCase(a1)
            .withStartDate(LocalDate.of(2020, 1, 20))
            .build();
        AbsenceCase b1 = AbsenceCase.builder()
            .withUser(secondUser)
            .withHeadTeacher(fifthUser)
            .withStartDate(LocalDate.of(2020, 2, 5))
            .withEndDate(LocalDate.of(2020, 2, 7))
            .withPartDayType(PartDayType.AllDay)
            .withAbsenceReason("Sickness.")
            .withUserComment("Flu.")
            .withIsCoverRequired(true)
            .withIsCoverProvided(true)
            .withIsApprovedByHeadTeacher(true)
            .withIsAbsencePaid(true)
            .withIsCaseResolved(false)
            .build();
        AbsenceCase b2 = AbsenceCase.builder()
            .withCase(b1)
            .withEndDate(LocalDate.of(2020, 2, 8))
            .build();
        AbsenceCase b3 = AbsenceCase.builder()
            .withCase(b1)
            .withEndDate(LocalDate.of(2020, 2, 9))
            .build();
        AbsenceCase b4 = AbsenceCase.builder()
            .withCase(b1)
            .withEndDate(LocalDate.of(2020, 2, 10))
            .build();
        AbsenceCase b5 = AbsenceCase.builder()
            .withCase(b1)
            .withEndDate(LocalDate.of(2020, 2, 11))
            .build();
        AbsenceCase b6 = AbsenceCase.builder()
            .withCase(b1)
            .withEndDate(LocalDate.of(2020, 2, 12))
            .build();
        AbsenceCase b7 = AbsenceCase.builder()
            .withCase(b1)
            .withEndDate(LocalDate.of(2020, 2, 13))
            .build();
        AbsenceCase b8 = AbsenceCase.builder()
            .withCase(b1)
            .withEndDate(LocalDate.of(2020, 2, 14))
            .build();
        AbsenceCase b9 = AbsenceCase.builder()
            .withCase(b1)
            .withEndDate(LocalDate.of(2020, 2, 15))
            .build();
        AbsenceCase b10 = AbsenceCase.builder()
            .withCase(b1)
            .withStartDate(LocalDate.of(2020, 6, 5))
            .withEndDate(LocalDate.of(2020, 6, 8))
            .build();
        AbsenceCase b11 = AbsenceCase.builder()
            .withCase(b1)
            .withStartDate(LocalDate.of(2020, 6, 5))
            .withEndDate(LocalDate.of(2020, 6, 9))
            .build();
        AbsenceCase b12 = AbsenceCase.builder()
            .withCase(b1)
            .withStartDate(LocalDate.of(2020, 6, 5))
            .withEndDate(LocalDate.of(2020, 6, 10))
            .build();
        AbsenceCase b13 = AbsenceCase.builder()
            .withCase(b1)
            .withStartDate(LocalDate.of(2020, 6, 5))
            .withEndDate(LocalDate.of(2020, 6, 11))
            .build();
        AbsenceCase b14 = AbsenceCase.builder()
            .withCase(b1)
            .withStartDate(LocalDate.of(2020, 6, 5))
            .withEndDate(LocalDate.of(2020, 6, 12))
            .build();
        AbsenceCase b15 = AbsenceCase.builder()
            .withCase(b1)
            .withStartDate(LocalDate.of(2020, 6, 5))
            .withEndDate(LocalDate.of(2020, 6, 13))
            .build();
        AbsenceCase b16 = AbsenceCase.builder()
            .withCase(b1)
            .withStartDate(LocalDate.of(2020, 6, 5))
            .withEndDate(LocalDate.of(2020, 6, 14))
            .build();
        AbsenceCase b17 = AbsenceCase.builder()
            .withCase(b1)
            .withStartDate(LocalDate.of(2020, 6, 5))
            .withEndDate(LocalDate.of(2020, 6, 15))
            .build();
        AbsenceCase b18 = AbsenceCase.builder()
            .withCase(b1)
            .withStartDate(LocalDate.of(2020, 6, 5))
            .withEndDate(LocalDate.of(2020, 6, 16))
            .build();
        AbsenceCase b19 = AbsenceCase.builder()
            .withCase(b1)
            .withStartDate(LocalDate.of(2020, 6, 5))
            .withEndDate(LocalDate.of(2020, 6, 17))
            .build();

        caseService.addCase(firstCase);
        caseService.addCase(secondCase);
        caseService.addCase(thirdCase);
        caseService.addCase(fourthCase);
        caseService.addCase(fifthCase);
        caseService.addCase(sixthCase);
        caseService.addCase(seventhCase);
        caseService.addCase(eighthCase);
        caseService.addCase(ninthCase);
        caseService.addCase(tenthCase);
        caseService.addCase(a1);
        caseService.addCase(a2);
        caseService.addCase(a3);
        caseService.addCase(a4);
        caseService.addCase(a5);
        caseService.addCase(a6);
        caseService.addCase(a7);
        caseService.addCase(a8);
        caseService.addCase(a9);
        caseService.addCase(b1);
        caseService.addCase(b2);
        caseService.addCase(b3);
        caseService.addCase(b4);
        caseService.addCase(b5);
        caseService.addCase(b6);
        caseService.addCase(b7);
        caseService.addCase(b8);
        caseService.addCase(b9);
        caseService.addCase(b10);
        caseService.addCase(b11);
        caseService.addCase(b12);
        caseService.addCase(b13);
        caseService.addCase(b14);
        caseService.addCase(b15);
        caseService.addCase(b16);
        caseService.addCase(b17);
        caseService.addCase(b18);
        caseService.addCase(b19);
    }
}
