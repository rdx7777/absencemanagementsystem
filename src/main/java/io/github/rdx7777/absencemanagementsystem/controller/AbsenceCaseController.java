package io.github.rdx7777.absencemanagementsystem.controller;

import io.github.rdx7777.absencemanagementsystem.model.AbsenceCase;
import io.github.rdx7777.absencemanagementsystem.model.AbsenceCaseDTO;
import io.github.rdx7777.absencemanagementsystem.model.AppModelMapper;
import io.github.rdx7777.absencemanagementsystem.model.Position;
import io.github.rdx7777.absencemanagementsystem.model.User;
import io.github.rdx7777.absencemanagementsystem.model.validation.AbsenceCaseValidator;
import io.github.rdx7777.absencemanagementsystem.service.AbsenceCaseService;
import io.github.rdx7777.absencemanagementsystem.service.EmailService;
import io.github.rdx7777.absencemanagementsystem.service.ServiceOperationException;
import io.github.rdx7777.absencemanagementsystem.service.UserService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cases")
public class AbsenceCaseController {

    private final Logger logger = LoggerFactory.getLogger(AbsenceCaseController.class);

    private final AbsenceCaseService caseService;
    private final UserService userService;
    private final EmailService emailService;
    private final AppModelMapper appModelMapper;

    public AbsenceCaseController(AbsenceCaseService caseService, UserService userService, EmailService emailService, AppModelMapper appModelMapper) {
        this.caseService = caseService;
        this.userService = userService;
        this.emailService = emailService;
        this.appModelMapper = appModelMapper;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> addCase(@RequestBody (required = false) AbsenceCase aCase) throws ServiceOperationException {
        if (aCase == null) {
            logger.error("Attempt to add null case.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to add null case.");
        }
        if (aCase.getId() != null && caseService.caseExists(aCase.getId())) {
            logger.error("Attempt to add case already existing in database.");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Attempt to add case already existing in database.");
        }
        List<String> validations = AbsenceCaseValidator.validate(aCase);
        if (validations.size() > 0) {
            logger.error("Attempt to add invalid case to database.");
            logger.error(String.valueOf(validations));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to add invalid case to database.");
        }
        AbsenceCaseDTO addedCase = appModelMapper.mapToAbsenceCaseDTO(caseService.addCase(aCase));
        logger.debug("New case added with id: {}.", addedCase.getId());
        Optional<User> headTeacher = userService.getUserById(aCase.getHeadTeacher().getId());
        if (headTeacher.isEmpty()) {
            logger.error("Attempt to send email to Cover Supervisor with details of Head Teacher, who does not exist in database.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to send email to Cover Supervisor with details of Head Teacher, who does not exist in database.");
        }
        Optional<User> user = userService.getUserById(aCase.getUser().getId());
        if (user.isEmpty()) {
            logger.error("Attempt to send email to Cover Supervisor with details of user, who does not exist in database.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to send email to Cover Supervisor with details of user, who does not exist in database.");
        }
        emailService.sendEmailToCoverSupervisor(headTeacher.get(), user.get(), addedCase);
        URI location = URI.create(String.format("/api/cases/%d", addedCase.getId()));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(location);
        return new ResponseEntity<>(addedCase, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(params = {"id", "userId"}, produces = "application/json", consumes = "application/json") // "api/cases?id=%d&userId=%d"
    @PreAuthorize("hasRole('ADMIN') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> updateCase(@RequestParam(name = "id") Long id,
                                        @RequestParam(name = "userId") Long editingUserId,
                                        @RequestBody (required = false) AbsenceCase aCase) throws ServiceOperationException {
        if (aCase == null) {
            logger.error("Attempt to update case providing null case.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to update case providing null case.");
        }
        if (!id.equals(aCase.getId())) {
            logger.error("Attempt to update case providing different case id.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to update case providing different case id.");
        }
        if (!caseService.caseExists(id)) {
            logger.error("Attempt to update not existing case.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attempt to update not existing case.");
        }
        List<String> validations = AbsenceCaseValidator.validate(aCase);
        if (validations.size() > 0) {
            logger.error("Attempt to update invalid case.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to update invalid case.");
        }

        User userWithPassword = userService.getUserByEmail(aCase.getUser().getEmail()).get();
        User headTeacherWithPassword = userService.getUserByEmail(aCase.getHeadTeacher().getEmail()).get();
        AbsenceCase absenceCaseForUpdate = AbsenceCase.builder()
            .withCase(aCase)
            .withUser(userWithPassword)
            .withHeadTeacher(headTeacherWithPassword)
            .build();
        AbsenceCaseDTO updatedCase = appModelMapper.mapToAbsenceCaseDTO(caseService.updateCase(absenceCaseForUpdate));

        logger.debug("Updated case with id {} by .", aCase.getId());
//        Optional<User> headTeacher = userService.getUserById(aCase.getHeadTeacher().getId());
//        if (headTeacher.isEmpty()) {
//            logger.error("Attempt to send email with details of Head Teacher, who does not exist in database.");
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to send email with details of Head Teacher, who does not exist in database.");
//        }
//        Optional<User> user = userService.getUserById(aCase.getUser().getId());
//        if (user.isEmpty()) {
//            logger.error("Attempt to send email with details of user, who does not exist in database.");
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to send email with details of user, who does not exist in database.");
//        }
//        sendEmail(editingUserId, headTeacher.get(), user.get(), updatedCase);
        sendEmail(editingUserId, headTeacherWithPassword, userWithPassword, updatedCase);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(updatedCase, httpHeaders, HttpStatus.OK);
//        return ResponseEntity.ok(updatedCase);
    }

    private void sendEmail(Long editingUserId, User headTeacher, User user, AbsenceCaseDTO updatedCase) throws ServiceOperationException {
        Optional<User> emailSender = userService.getUserById(editingUserId);
        if (emailSender.isEmpty()) {
            logger.error("Attempt to send email from user, who does not exist in database.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to send email from user, who does not exist in database.");
        }
        Position position = emailSender.get().getPosition();
        switch (position) {
            case CoverSupervisor:
                emailService.sendEmailToHeadTeacher(headTeacher, user, updatedCase);
                break;
            case HeadTeacher:
                emailService.sendEmailToHumanResourcesSupervisor(headTeacher, user, updatedCase);
                break;
            case HumanResourcesSupervisor:
                emailService.sendEmailToUser(headTeacher, user, updatedCase);
                break;
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> getCase(@PathVariable("id") Long id) throws ServiceOperationException {
        Optional<AbsenceCase> aCase = caseService.getCaseById(id);
        if (aCase.isEmpty()) {
            logger.error("Attempt to get case by id that does not exist in database.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attempt to get case by id that does not exist in database.");
        }
        return ResponseEntity.ok(aCase.get());
    }

    @GetMapping(produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> getAllCases() throws ServiceOperationException {
        logger.info("Attempt to get all cases.");
        return ResponseEntity.ok(appModelMapper.mapToAbsenceCaseDTOList((List<AbsenceCase>) caseService.getAllCases()));
    }

    @GetMapping(value = "/active", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> getAllActiveCases() throws ServiceOperationException {
        logger.info("Attempt to get all active cases.");
        return ResponseEntity.ok(appModelMapper.mapToAbsenceCaseDTOList((List<AbsenceCase>) caseService.getAllActiveCases()));
    }

/*    @GetMapping(value = "/user", params = {"id", "caseId"}, produces = "application/json") // "api/cases/user?id=%d&caseId=%d"
    public ResponseEntity<?> getUserCase(@RequestParam(name = "id") Long id, @RequestParam(name = "caseId") Long caseId) throws ServiceOperationException {
//    @GetMapping(value = "/user/{id}/{caseId}") // "api/cases/user/%d/%d"
//    public ResponseEntity<?> getUserCase(@PathVariable("id") Long id, @PathVariable("caseId") Long caseId) {
        if (!caseService.getCaseById(caseId).get().getId().equals(id)) {
            logger.error("Attempt to get case which is not corresponding with user id.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to get case which is not corresponding with user id.");
        }
        logger.info("Attempt to get all user cases.");
         return ResponseEntity.ok(caseService.getCaseById(caseId).get());
    }*/

    @GetMapping(value = "/user/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> getAllUserCases(@PathVariable("id") Long id) throws ServiceOperationException {
        logger.info("Attempt to get all user cases.");
        return ResponseEntity.ok(appModelMapper.mapToAbsenceCaseDTOList((List<AbsenceCase>) caseService.getAllUserCases(id)));
    }

    @GetMapping(value = "/active/ht/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> getAllActiveCasesForHeadTeacher(@PathVariable("id") Long id) throws ServiceOperationException {
        logger.info("Attempt to get all active cases for Head Teacher with id {}.", id);
        return ResponseEntity.ok(appModelMapper.mapToAbsenceCaseDTOList((List<AbsenceCase>) caseService.getAllActiveCasesForHeadTeacher(id)));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HEAD_TEACHER')")
    public ResponseEntity<?> deleteCase(@PathVariable("id") Long id) throws ServiceOperationException {
        if (!caseService.caseExists(id)) {
            logger.error("Attempt to delete not existing case.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attempt to delete not existing case.");
        }
        caseService.deleteCase(id);
        logger.debug("Deleted case with id {}.", id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(httpHeaders, HttpStatus.NO_CONTENT);
    }
}
