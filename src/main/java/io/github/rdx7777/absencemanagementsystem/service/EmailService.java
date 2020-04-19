package io.github.rdx7777.absencemanagementsystem.service;

import io.github.rdx7777.absencemanagementsystem.model.AbsenceCaseDTO;
import io.github.rdx7777.absencemanagementsystem.model.User;

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

    @Autowired
    public EmailService(JavaMailSender mailSender, MailProperties mailProperties) {
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
    }

    @Async
    public void sendEmailToCoverSupervisor(User headTeacher, User user, AbsenceCaseDTO aCase) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // TODO: solve sending email FROM - sender cannot be taken from properties
            helper.setFrom(mailProperties.getUsername());
            // TODO: solve sending email to Cover Supervisor (below) - supervisor as this method argument
            helper.setTo(mailProperties.getProperties().get("to_cover_supervisor")); // permanent settings
            helper.setSubject("New case in Absence Management System - action required");
            helper.setText("Dear Cover Supervisor, you have new case to deal with." + "\n"
                + "Absence case id: " + aCase.getId() + "\n"
                + "Staff name: " + user.getName() + " " + user.getSurname() + "\n"
                + "Staff job title: " + user.getJobTitle() + "\n"
                + "Absence reason: " + aCase.getAbsenceReason() + "\n"
                + "Staff comment: " + aCase.getUserComment() + "\n"
                + "Head Teacher: " + headTeacher.getName() + " " + headTeacher.getSurname());
            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error("An error occurred during sending email.", e);
        }
    }

    @Async
    public void sendEmailToHeadTeacher(User headTeacher, User user, AbsenceCaseDTO aCase) {
        String headTeacherEmail = headTeacher.getEmail();
        String isCoverRequired = aCase.getIsCoverRequired() ? "yes" : "no";
        String isCoverProvided = aCase.getIsCoverProvided() ? "yes" : "no";
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // TODO: solve sending email FROM - sender cannot be taken from properties
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
        String isCoverRequired = aCase.getIsCoverRequired() ? "yes" : "no";
        String isCoverProvided = aCase.getIsCoverProvided() ? "yes" : "no";
        String isApprovedByHeadTeacher = aCase.getIsApprovedByHeadTeacher() ? "yes" : "no";
        String isAbsencePaid = aCase.getIsAbsencePaid() ? "yes" : "no";
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // TODO: solve sending email FROM - sender cannot be taken from properties
            helper.setFrom(mailProperties.getUsername());
            // TODO: solve sending email to HR supervisor (below) - supervisor as this method argument
            helper.setTo(mailProperties.getProperties().get("to_hr_supervisor")); // permanent settings
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
        } catch (MessagingException e) {
            logger.error("An error occurred during sending email.", e);
        }
    }

    @Async
    public void sendEmailToUser(User headTeacher, User user, AbsenceCaseDTO aCase) {
        String userEmail = user.getEmail();
        String isCoverRequired = aCase.getIsCoverRequired() ? "yes" : "no";
        String isCoverProvided = aCase.getIsCoverProvided() ? "yes" : "no";
        String isApprovedByHeadTeacher = aCase.getIsApprovedByHeadTeacher() ? "yes" : "no";
        String isAbsencePaid = aCase.getIsAbsencePaid() ? "yes" : "no";
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // TODO: solve sending email FROM - sender cannot be taken from properties
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
