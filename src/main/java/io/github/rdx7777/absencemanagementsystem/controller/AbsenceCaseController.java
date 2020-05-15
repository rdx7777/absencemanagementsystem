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

@CrossOrigin(origins = {"https://a-m-system.herokuapp.com/", "*"}, maxAge = 3600)
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
                                        @RequestBody (required = false) AbsenceCaseDTO caseDTO) throws ServiceOperationException {
        if (caseDTO == null) {
            logger.error("Attempt to update case providing null case.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to update case providing null case.");
        }
        if (!id.equals(caseDTO.getId())) {
            logger.error("Attempt to update case providing different case id.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to update case providing different case id.");
        }
        if (!caseService.caseExists(id)) {
            logger.error("Attempt to update not existing case.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attempt to update not existing case.");
        }
        AbsenceCase aCase = appModelMapper.mapToAbsenceCase(caseDTO);
        List<String> validations = AbsenceCaseValidator.validate(aCase);
        if (validations.size() > 0) {
            logger.error("Attempt to update invalid case.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to update invalid case.");
        }
        AbsenceCaseDTO updatedCase = appModelMapper.mapToAbsenceCaseDTO(caseService.updateCase(aCase));
        logger.debug("Updated case with id {} by .", caseDTO.getId());
        Optional<User> headTeacher = userService.getUserById(caseDTO.getHeadTeacher().getId());
        if (headTeacher.isEmpty()) {
            logger.error("Attempt to send email with details of Head Teacher, who does not exist in database.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to send email with details of Head Teacher, who does not exist in database.");
        }
        Optional<User> user = userService.getUserById(caseDTO.getUser().getId());
        if (user.isEmpty()) {
            logger.error("Attempt to send email with details of user, who does not exist in database.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to send email with details of user, who does not exist in database.");
        }
        sendEmail(editingUserId, headTeacher.get(), user.get(), updatedCase);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(updatedCase, httpHeaders, HttpStatus.OK);
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
                if (updatedCase.getIsCaseResolved()) {
                    emailService.sendEmailToUser(headTeacher, user, updatedCase);
                    break;
                }
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
        return ResponseEntity.ok(appModelMapper.mapToAbsenceCaseDTO(aCase.get()));
    }

    @GetMapping(produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> getAllCases() throws ServiceOperationException {
        logger.info("Attempt to get all cases.");
        return ResponseEntity.ok(appModelMapper.mapToAbsenceCaseDTOList((List<AbsenceCase>) caseService.getAllCases()));
    }

    @GetMapping(params = {"offset", "limit"}, produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> getAllCasesPaginated(@RequestParam(name = "offset") Long offset,
                                                  @RequestParam(name = "limit") Long limit) throws ServiceOperationException {
        if (offset == null) {
            logger.error("Attempt to get all paginated cases providing null offset.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to get all paginated cases providing null offset.");
        }
        if (limit == null) {
            logger.error("Attempt to get all paginated cases providing null limit.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to get all paginated cases providing null limit.");
        }
        logger.info("Attempt to get all paginated cases with offset {} and limit {}.", offset, limit);
        return ResponseEntity.ok(appModelMapper.mapToAbsenceCaseDTOList((List<AbsenceCase>) caseService.getAllCasesPaginated(offset, limit)));
    }

    @GetMapping(value = "/active", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> getAllActiveCases() throws ServiceOperationException {
        logger.info("Attempt to get all active cases.");
        return ResponseEntity.ok(appModelMapper.mapToAbsenceCaseDTOList((List<AbsenceCase>) caseService.getAllActiveCases()));
    }

    @GetMapping(value = "/active", params = {"offset", "limit"}, produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> getAllActiveCasesPaginated(@RequestParam(name = "offset") Long offset,
                                                        @RequestParam(name = "limit") Long limit) throws ServiceOperationException {
        if (offset == null) {
            logger.error("Attempt to get all paginated active cases providing null offset.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to get all paginated active cases providing null offset.");
        }
        if (limit == null) {
            logger.error("Attempt to get all paginated active cases providing null limit.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to get all paginated active cases providing null limit.");
        }
        logger.info("Attempt to get all paginated active cases with offset {} and limit {}.", offset, limit);
        return ResponseEntity.ok(appModelMapper.mapToAbsenceCaseDTOList((List<AbsenceCase>) caseService.getAllActiveCasesPaginated(offset, limit)));
    }

    @GetMapping(value = "/user/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> getAllUserCases(@PathVariable("id") Long id) throws ServiceOperationException {
        logger.debug("Attempt to get all user cases.");
        return ResponseEntity.ok(appModelMapper.mapToAbsenceCaseDTOList((List<AbsenceCase>) caseService.getAllUserCases(id)));
    }

    @GetMapping(value = "/user/{id}", params = {"offset", "limit"}, produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> getAllUserCasesPaginated(@PathVariable("id") Long id,
                                                      @RequestParam(name = "offset") Long offset,
                                                      @RequestParam(name = "limit") Long limit) throws ServiceOperationException {
        logger.debug("Attempt to get all paginated user cases.");
        return ResponseEntity.ok(appModelMapper.mapToAbsenceCaseDTOList((List<AbsenceCase>) caseService.getAllUserCasesPaginated(id, offset, limit)));
    }

    @GetMapping(value = "/active/ht/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> getAllActiveCasesForHeadTeacher(@PathVariable("id") Long id) throws ServiceOperationException {
        logger.debug("Attempt to get all active cases for Head Teacher with id {}.", id);
        return ResponseEntity.ok(appModelMapper.mapToAbsenceCaseDTOList((List<AbsenceCase>) caseService.getAllActiveCasesForHeadTeacher(id)));
    }

    @GetMapping(value = "/active/ht/{id}", params = {"offset", "limit"}, produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> getAllActiveCasesForHeadTeacherPaginated(@PathVariable("id") Long id,
                                                                      @RequestParam(name = "offset") Long offset,
                                                                      @RequestParam(name = "limit") Long limit) throws ServiceOperationException {
        if (offset == null) {
            logger.error("Attempt to get all paginated active cases for Head Teacher with id {} providing null offset.", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format("Attempt to get all paginated active cases for Head Teacher with id %d providing null offset.", id));
        }
        if (limit == null) {
            logger.error("Attempt to get all paginated active cases for Head Teacher with id {} providing null limit.", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format("Attempt to get all paginated active cases for Head Teacher with id %d providing null limit.", id));
        }
        logger.info("Attempt to get all paginated active cases for Head Teacher with id {} with offset {} and limit {}.", id, offset, limit);
        return ResponseEntity.ok(appModelMapper.mapToAbsenceCaseDTOList((List<AbsenceCase>) caseService.getAllActiveCasesForHeadTeacherPaginated(id, offset, limit)));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping(value = "/count", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> countCases() throws ServiceOperationException {
        logger.info("Attempt to get number of all cases.");
        return ResponseEntity.ok(caseService.casesCount());
    }

    @GetMapping(value = "/active/count", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> countActiveCases() throws ServiceOperationException {
        logger.info("Attempt to get number of all active cases.");
        return ResponseEntity.ok(caseService.activeCasesCount());
    }

    @GetMapping(value = "/active/count/ht/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> countActiveCasesForHeadTeacher(@PathVariable("id") Long headTeacherId) throws ServiceOperationException {
        logger.info("Attempt to get number of all active cases for Head Teacher.");
        return ResponseEntity.ok(caseService.activeCasesForHeadTeacherCount(headTeacherId));
    }

    @GetMapping(value = "/count/user/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> countUserCases(@PathVariable("id") Long userId) throws ServiceOperationException {
        logger.info("Attempt to get number of all user cases.");
        return ResponseEntity.ok(caseService.userCasesCount(userId));
    }
}
