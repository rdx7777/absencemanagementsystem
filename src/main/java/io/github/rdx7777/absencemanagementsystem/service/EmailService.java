package io.github.rdx7777.absencemanagementsystem.service;

import io.github.rdx7777.absencemanagementsystem.model.AbsenceCaseDTO;
import io.github.rdx7777.absencemanagementsystem.model.User;

import java.util.Optional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    private final UserService userService;

    @Autowired
    public EmailService(JavaMailSender mailSender, MailProperties mailProperties, UserService userService) {
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
        this.userService = userService;
    }

    @Async
    public void sendEmailToCoverSupervisor(User headTeacher, User user, AbsenceCaseDTO aCase) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailProperties.getUsername());
            Optional<User> coverSupervisor = userService.getCoverSupervisor();
            if (coverSupervisor.isEmpty()) {
                logger.error("Attempt to get Cover Supervisor that does not exist in database.");
                throw new ServiceOperationException("Attempt to get Cover Supervisor that does not exist in database.");
            }
            helper.setTo(coverSupervisor.get().getEmail());
            helper.setSubject("New case in Absence Management System - action required");
            helper.setText("Dear Cover Supervisor, you have new case to deal with." + "\n"
                + "Absence case id: " + aCase.getId() + "\n"
                + "Staff name: " + user.getName() + " " + user.getSurname() + "\n"
                + "Staff job title: " + user.getJobTitle() + "\n"
                + "Absence reason: " + aCase.getAbsenceReason() + "\n"
                + "Staff comment: " + aCase.getUserComment() + "\n"
                + "Head Teacher: " + headTeacher.getName() + " " + headTeacher.getSurname());
            mailSender.send(message);
        } catch (MessagingException | ServiceOperationException e) {
            logger.error("An error occurred during sending email.", e);
        }
    }

    @Async
    public void sendEmailToHeadTeacher(User headTeacher, User user, AbsenceCaseDTO aCase) {
        String headTeacherEmail = headTeacher.getEmail();
        String isCoverRequired = aCase.getIsCoverRequired().getStatus();
        String isCoverProvided = aCase.getIsCoverProvided().getStatus();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailProperties.getUsername());
            helper.setTo(headTeacherEmail);
            helper.setSubject("New case in Absence Management System - action required");
            helper.setText("Dear Head Teacher, you have new case to deal with." + "\n"
                + "Absence case id: " + aCase.getId() + "\n"
                + "Staff name: " + user.getName() + " " + user.getSurname() + "\n"
                + "Staff job title: " + user.getJobTitle() + "\n"
                + "Absence reason: " + aCase.getAbsenceReason() + "\n"
                + "Staff comment: " + aCase.getUserComment() + "\n"
                + "Head Teacher: " + headTeacher.getName() + " " + headTeacher.getSurname() + "\n"
                + "Cover required: " + isCoverRequired + "\n"
                + "Cover provided: " + isCoverProvided + "\n"
                + "Cover Supervisor comment: " + aCase.getCoverSupervisorComment());
            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error("An error occurred during sending email.", e);
        }
    }

    @Async
    public void sendEmailToHumanResourcesSupervisor(User headTeacher, User user, AbsenceCaseDTO aCase) {
        String isCoverRequired = aCase.getIsCoverRequired().getStatus();
        String isCoverProvided = aCase.getIsCoverProvided().getStatus();
        String isApprovedByHeadTeacher = aCase.getIsApprovedByHeadTeacher().getStatus();
        String isAbsencePaid = aCase.getIsAbsencePaid().getStatus();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailProperties.getUsername());
            Optional<User> hrSupervisor = userService.getHRSupervisor();
            if (hrSupervisor.isEmpty()) {
                logger.error("Attempt to get HR Supervisor that does not exist in database.");
                throw new ServiceOperationException("Attempt to get HR Supervisor that does not exist in database.");
            }
            helper.setTo(hrSupervisor.get().getEmail());
            helper.setSubject("New case in Absence Management System - action required");
            helper.setText("Dear HR Supervisor, you have new case to deal with." + "\n"
                + "Absence case id: " + aCase.getId() + "\n"
                + "Staff name: " + user.getName() + " " + user.getSurname() + "\n"
                + "Staff job title: " + user.getJobTitle() + "\n"
                + "Absence reason: " + aCase.getAbsenceReason() + "\n"
                + "Staff comment: " + aCase.getUserComment() + "\n"
                + "Head Teacher: " + headTeacher.getName() + " " + headTeacher.getSurname() + "\n"
                + "Cover required: " + isCoverRequired + "\n"
                + "Cover provided: " + isCoverProvided + "\n"
                + "Cover Supervisor comment: " + aCase.getCoverSupervisorComment() + "\n"
                + "Absence approved by Head Teacher: " + isApprovedByHeadTeacher + "\n"
                + "Absence paid: " + isAbsencePaid + "\n"
                + "Head Teacher comment: " + aCase.getHeadTeacherComment());
            mailSender.send(message);
        } catch (MessagingException | ServiceOperationException e) {
            logger.error("An error occurred during sending email.", e);
        }
    }

    @Async
    public void sendEmailToUser(User headTeacher, User user, AbsenceCaseDTO aCase) {
        String userEmail = user.getEmail();
        String isCoverRequired = aCase.getIsCoverRequired().getStatus();
        String isCoverProvided = aCase.getIsCoverProvided().getStatus();
        String isApprovedByHeadTeacher = aCase.getIsApprovedByHeadTeacher().getStatus();
        String isAbsencePaid = aCase.getIsAbsencePaid().getStatus();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailProperties.getUsername());
            helper.setTo(userEmail);
            helper.setSubject("Your case in Absence Management System is finished");
            helper.setText("Dear User, your case is finally resolved." + "\n"
                + "Absence case id: " + aCase.getId() + "\n"
                + "Staff name: " + user.getName() + " " + user.getSurname() + "\n"
                + "Staff job title: " + user.getJobTitle() + "\n"
                + "Absence reason: " + aCase.getAbsenceReason() + "\n"
                + "Staff comment: " + aCase.getUserComment() + "\n"
                + "Head Teacher: " + headTeacher.getName() + " " + headTeacher.getSurname() + "\n"
                + "Cover required: " + isCoverRequired + "\n"
                + "Cover provided: " + isCoverProvided + "\n"
                + "Cover Supervisor comment: " + aCase.getCoverSupervisorComment() + "\n"
                + "Absence approved by Head Teacher: " + isApprovedByHeadTeacher + "\n"
                + "Absence paid: " + isAbsencePaid + "\n"
                + "Head Teacher comment: " + aCase.getHeadTeacherComment() + "\n"
                + "Good luck!");
            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error("An error occurred during sending email.", e);
        }
    }
}
