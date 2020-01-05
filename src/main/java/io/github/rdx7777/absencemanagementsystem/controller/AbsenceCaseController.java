package io.github.rdx7777.absencemanagementsystem.controller;

import io.github.rdx7777.absencemanagementsystem.model.AbsenceCase;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/cases")
public class AbsenceCaseController {

    private final Logger logger = LoggerFactory.getLogger(AbsenceCaseController.class);

    private AbsenceCaseService caseService;
    private UserService userService;
    private EmailService emailService;

    public AbsenceCaseController(AbsenceCaseService caseService, UserService userService, EmailService emailService) {
        this.caseService = caseService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<?> addCase(@RequestBody AbsenceCase aCase) throws ServiceOperationException {
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
            System.out.println(validations);
            logger.error("Attempt to add invalid case to database.");
            logger.error(String.valueOf(validations));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to add invalid case to database.");
        }
        AbsenceCase addedCase = caseService.addCase(aCase);
        logger.debug("New case added with id: {}.", addedCase.getId());
        User headTeacher = userService.getUserById(aCase.getHeadTeacherId()).get();
        User user = userService.getUserById(aCase.getUserId()).get();
        emailService.sendEmailToCoverSupervisor(headTeacher, user, aCase);
        URI location = URI.create(String.format("/api/cases/%d", addedCase.getId()));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(location);
        return new ResponseEntity<>(addedCase, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(params = {"id", "userId"})
    public ResponseEntity<?> updateCase(@RequestParam(name = "id") Long id,
                                        @RequestParam(name = "userId") Long editingUserId,
                                        @RequestBody AbsenceCase aCase) throws ServiceOperationException {
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
/*
        if (!userService.userExists(editingUserId)) {
            logger.error("Attempt to update case by not existing user.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to update case by not existing user.");
        }
*/
        List<String> validations = AbsenceCaseValidator.validate(aCase);
        if (validations.size() > 0) {
            logger.error("Attempt to update invalid case.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to update invalid case.");
        }
        logger.debug("Updated case with id {} by .", aCase.getId());
        User headTeacher = userService.getUserById(aCase.getHeadTeacherId()).get();
        User user = userService.getUserById(aCase.getUserId()).get();
        AbsenceCase updatedCase = caseService.updateCase(aCase);
        sendEmail(editingUserId, headTeacher, user, updatedCase);
        return ResponseEntity.ok(updatedCase);
    }

    private void sendEmail(Long editingUserId, User headTeacher, User user, AbsenceCase updatedCase) {
        Position position = userService.getUserById(editingUserId).get().getPosition();
        switch (position) {
            case CoverSupervisor:
                emailService.sendEmailToHeadTeacher(headTeacher, user, updatedCase);
                break;
            case HeadTeacher:
                emailService.sendEmailToHumanResourcesSupervisor(headTeacher, user, updatedCase);
                break;
            case HumanResourcesSupervisor:
                emailService.sendEmailToUser(headTeacher, user, updatedCase);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getCase(@PathVariable("id") Long id) {
        Optional<AbsenceCase> aCase = caseService.getCaseById(id);
        if (aCase.isEmpty()) {
            logger.error("Attempt to get case by id that does not exist in database.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attempt to get case by id that does not exist in database.");
        }
        return ResponseEntity.ok(aCase.get());
    }

    @GetMapping
    public ResponseEntity<?> getAllCases() {
        logger.info("Attempt to get all cases.");
        return ResponseEntity.ok(caseService.getAllCases());
    }

    @GetMapping(value = "/active")
    public ResponseEntity<?> getAllActiveCases() {
        logger.info("Attempt to get all active cases.");
        return ResponseEntity.ok(caseService.getAllActiveCases());
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<?> getAllUserCases(@PathVariable("id") Long id) {
        /*if (!userService.getUserById(id).get().getIsActive()) {
            logger.error("Attempt to get all user cases by not active user.");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }*/
        logger.info("Attempt to get all user cases.");
        return ResponseEntity.ok(caseService.getAllUserCases(id));
    }

    @GetMapping(value = "/active/ht/{id}")
    public ResponseEntity<?> getAllActiveCasesForHeadTeacher(@PathVariable("id") Long id) {
        logger.info("Attempt to get all active cases for Head Teacher with id {}.", id);
        return ResponseEntity.ok(caseService.getAllActiveCasesForHeadTeacher(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteCase(@PathVariable("id") Long id) {
        if (!caseService.caseExists(id)) {
            logger.error("Attempt to delete not existing case.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attempt to delete not existing case.");
        }
        caseService.deleteCase(id);
        logger.debug("Deleted case with id {}.", id);
        return ResponseEntity.noContent().build();
    }
}
