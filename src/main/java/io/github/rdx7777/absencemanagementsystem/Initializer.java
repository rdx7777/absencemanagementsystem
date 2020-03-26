package io.github.rdx7777.absencemanagementsystem;

import io.github.rdx7777.absencemanagementsystem.model.AbsenceCase;
import io.github.rdx7777.absencemanagementsystem.model.PartDayType;
import io.github.rdx7777.absencemanagementsystem.model.Position;
import io.github.rdx7777.absencemanagementsystem.model.User;
import io.github.rdx7777.absencemanagementsystem.repository.AbsenceCaseRepository;
import io.github.rdx7777.absencemanagementsystem.repository.UserRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements CommandLineRunner {

    private final AbsenceCaseRepository caseRepository;
    private final UserRepository userRepository;

    @Autowired
    public Initializer(AbsenceCaseRepository caseRepository, UserRepository userRepository) {
        this.caseRepository = caseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        User firstUser = User.builder()
            .withName("Alice")
            .withSurname("Springfield")
            .withEmail("rdx7777.test@gmail.com")
            .withPassword("$2a$10$u3AJC2e8fQ7bapCZh6I6Re4siOLimyBkPp.E//Ae07CSdW1SrRrFu") // password: test
            .withJobTitle("Math Teacher")
            .withIsActive(true)
            .withPosition(Position.Employee)
            .withRole("ROLE_USER")
            .build();
        User secondUser = User.builder()
            .withName("Jim")
            .withSurname("Morrison")
            .withEmail("rdx7777.test1@gmail.com")
            .withPassword("$2a$10$u3AJC2e8fQ7bapCZh6I6Re4siOLimyBkPp.E//Ae07CSdW1SrRrFu") // password: test
            .withJobTitle("English Teacher")
            .withIsActive(true)
            .withPosition(Position.Employee)
            .withRole("ROLE_USER")
            .build();
        User thirdUser = User.builder()
            .withName("Kate")
            .withSurname("Bloom")
            .withEmail("rdx7777.test2@gmail.com")
            .withPassword("$2a$10$u3AJC2e8fQ7bapCZh6I6Re4siOLimyBkPp.E//Ae07CSdW1SrRrFu") // password: test
            .withJobTitle("Cover Supervisor")
            .withIsActive(true)
            .withPosition(Position.CoverSupervisor)
            .withRole("ROLE_CS_SUPERVISOR")
            .build();
        User fourthUser = User.builder()
            .withName("Janet")
            .withSurname("Barney")
            .withEmail("rdx7777.test3@gmail.com")
            .withPassword("$2a$10$u3AJC2e8fQ7bapCZh6I6Re4siOLimyBkPp.E//Ae07CSdW1SrRrFu") // password: test
            .withJobTitle("Head Teacher - Maths & Science")
            .withIsActive(true)
            .withPosition(Position.HeadTeacher)
            .withRole("ROLE_HEAD_TEACHER")
            .build();
        User fifthUser = User.builder()
            .withName("Leila")
            .withSurname("Smith")
            .withEmail("rdx7777.test4@gmail.com")
            .withPassword("$2a$10$u3AJC2e8fQ7bapCZh6I6Re4siOLimyBkPp.E//Ae07CSdW1SrRrFu") // password: test
            .withJobTitle("Head Teacher - Languages")
            .withIsActive(true)
            .withPosition(Position.HeadTeacher)
            .withRole("ROLE_HEAD_TEACHER")
            .build();
        User sixthUser = User.builder()
            .withName("Mark")
            .withSurname("Thompson")
            .withEmail("rdx7777.test5@gmail.com")
            .withPassword("$2a$10$u3AJC2e8fQ7bapCZh6I6Re4siOLimyBkPp.E//Ae07CSdW1SrRrFu") // password: test
            .withJobTitle("HR Supervisor")
            .withIsActive(true)
            .withPosition(Position.HumanResourcesSupervisor)
            .withRole("ROLE_HR_SUPERVISOR")
            .build();
        Collection<String> existingUserEmails = userRepository.findAll().stream().map(User::getEmail).collect(Collectors.toList());
//        System.out.println(existingUserEmails);
//        List<User> users = new ArrayList<>(Arrays.asList(firstUser, secondUser, thirdUser, fourthUser, fifthUser, sixthUser));
        userRepository.saveAll(Arrays.asList(thirdUser, sixthUser));
//        System.out.println(users);
//        for (User user : users) {
//            if (existingUserEmails.contains(user.getEmail())) {
//                userRepository.deleteById(userRepository.findUserByEmail(user.getEmail()).get().getId());
//            }
//            user = userRepository.save(user);
//        }
        AbsenceCase firstCase = AbsenceCase.builder()
            .withUser(firstUser)
            .withHeadTeacher(fourthUser)
            .withStartDate(LocalDate.of(2020, 1, 5))
            .withEndDate(LocalDate.of(2020, 1, 20))
            .withPartDayType(PartDayType.AllDay)
            .withAbsenceReason("Holiday.")
            .withIsCoverRequired(false)
            .withIsCoverProvided(false)
            .withIsApprovedByHeadTeacher(false)
            .withIsAbsencePaid(false)
            .withIsCaseResolved(false)
            .build();
        AbsenceCase secondCase = AbsenceCase.builder()
            .withUser(secondUser)
            .withHeadTeacher(fifthUser)
            .withStartDate(LocalDate.of(2020, 2, 1))
            .withEndDate(LocalDate.of(2020, 2, 4))
            .withPartDayType(PartDayType.AllDay)
            .withAbsenceReason("Sickness.")
            .withUserComment("Flu.")
            .withIsCoverRequired(true)
            .withIsCoverProvided(true)
            .withIsApprovedByHeadTeacher(true)
            .withIsAbsencePaid(true)
            .withIsCaseResolved(true)
            .build();
//        caseRepository.deleteAll();
        caseRepository.saveAll(Arrays.asList(firstCase, secondCase));
    }
}
