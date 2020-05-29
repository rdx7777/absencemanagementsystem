package io.github.rdx7777.absencemanagementsystem.service;

import io.github.rdx7777.absencemanagementsystem.model.AbsenceCase;
import io.github.rdx7777.absencemanagementsystem.model.User;
import io.github.rdx7777.absencemanagementsystem.repository.AbsenceCaseRepository;
import io.github.rdx7777.absencemanagementsystem.repository.UserRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AbsenceCaseService {

    private final Logger logger = LoggerFactory.getLogger(AbsenceCaseService.class);

    private final AbsenceCaseRepository repository;
    private final UserRepository userRepository;

    public AbsenceCaseService(AbsenceCaseRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public AbsenceCase addCase(AbsenceCase aCase) throws ServiceOperationException {
        if (aCase == null) {
            logger.error("Attempt to add null absence case.");
            throw new IllegalArgumentException("Absence case cannot be null.");
        }
        Long caseId = aCase.getId();
        if (caseId != null && repository.existsById(caseId)) {
            logger.error("Attempt to add absence case already existing in database.");
            throw new ServiceOperationException("Absence case already exists in database.");
        }
        try {
            Optional<User> user = userRepository.findUserByEmail(aCase.getUser().getEmail());
            if (user.isEmpty()) {
                userRepository.save(aCase.getUser());
            }
            Optional<User> headTeacher = userRepository.findUserByEmail(aCase.getHeadTeacher().getEmail());
            if (headTeacher.isEmpty()) {
                userRepository.save(aCase.getHeadTeacher());
            }
            AbsenceCase caseToSave;
            if (aCase.getIsCaseResolved()) {
                caseToSave = AbsenceCase.builder().withCase(aCase).withCreatedDate(LocalDate.now()).withResolvedDate(LocalDate.now()).build();
            } else {
                caseToSave = AbsenceCase.builder().withCase(aCase).withCreatedDate(LocalDate.now()).build();
            }
            return repository.save(caseToSave);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during adding absence case.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public AbsenceCase updateCase(AbsenceCase aCase) throws ServiceOperationException {
        if (aCase == null) {
            logger.error("Attempt to update case providing null absence case.");
            throw new IllegalArgumentException("Absence case cannot be null.");
        }
        Long caseId = aCase.getId();
        if (caseId == null || !repository.existsById(caseId)) {
            logger.error("Attempt to update not existing absence case.");
            throw new ServiceOperationException("Given absence case does not exist in database.");
        }
        try {
            AbsenceCase caseToUpdate;
            if (aCase.getIsCaseResolved() && (repository.findById(aCase.getId()).get().getResolvedDate() == null)) {
                caseToUpdate = AbsenceCase.builder().withCase(aCase).withResolvedDate(LocalDate.now()).build();
            } else {
                caseToUpdate = aCase;
            }
            return repository.save(caseToUpdate);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during updating absence case.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Optional<AbsenceCase> getCaseById(Long id) throws ServiceOperationException {
        if (id == null) {
            logger.error("Attempt to get absence case by id providing null id.");
            throw new IllegalArgumentException("Id cannot be null.");
        }
        try {
            return repository.findById(id);
        } catch (NoSuchElementException e) {
            String message = "An error occurred during getting absence case by id.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<AbsenceCase> getAllCases() throws ServiceOperationException {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        try {
            return repository.findAll(sort);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting all absence cases.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<AbsenceCase> getAllCasesPaginated(Long offset, Long limit) throws ServiceOperationException {
        if (offset < 0) {
            logger.error("Attempt to get all paginated cases with offset less than 0.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to get all paginated cases with offset less than 0.");
        }
        if (limit < 0) {
            logger.error("Attempt to get all paginated cases with limit less than 0.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to get all paginated cases with limit less than 0.");
        }
        try {
            return repository.findAllByOffsetAndLimit(offset, limit);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting all paginated absence cases.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<AbsenceCase> getAllActiveCases() throws ServiceOperationException {
        Example<AbsenceCase> example = Example.of(new AbsenceCase.Builder().withIsCaseResolved(false).build());
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        try {
            return repository.findAll(example, sort);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting all active absence cases.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<AbsenceCase> getAllActiveCasesPaginated(Long offset, Long limit) throws ServiceOperationException {
        if (offset < 0) {
            logger.error("Attempt to get all paginated active cases with offset less than 0.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to get all paginated active cases with offset less than 0.");
        }
        if (limit < 0) {
            logger.error("Attempt to get all paginated active cases with limit less than 0.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to get all paginated active cases with limit less than 0.");
        }
        try {
            return repository.findAllActiveByOffsetAndLimit(offset, limit);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting all paginated active absence cases.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<AbsenceCase> getAllUserCases(Long userId) throws ServiceOperationException {
        if (userId == null) {
            logger.error("Attempt to get absence cases for user id providing null id.");
            throw new IllegalArgumentException("User id cannot be null.");
        }
        User userExample = User.builder().withId(userId).build();
        Example<AbsenceCase> caseExample = Example.of(new AbsenceCase.Builder().withUser(userExample).build());
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        try {
            return repository.findAll(caseExample, sort);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting all absence cases for user id: " + userId + ".";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<AbsenceCase> getAllUserCasesPaginated(Long userId, Long offset, Long limit) throws ServiceOperationException {
        if (userId == null) {
            logger.error("Attempt to get absence cases for user id providing null id.");
            throw new IllegalArgumentException("User id cannot be null.");
        }
        if (offset < 0) {
            logger.error("Attempt to get absence cases for user id with offset less than 0.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to get absence cases for user id with offset less than 0.");
        }
        if (limit < 0) {
            logger.error("Attempt to get absence cases for user id with limit less than 0.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to get absence cases for user id with limit less than 0.");
        }
        try {
            return repository.findAllByUserIdByOffsetAndLimit(userId, offset, limit);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting absence cases for user id: " + userId + ".";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<AbsenceCase> getAllActiveCasesForHeadTeacher(Long headTeacherId) throws ServiceOperationException {
        if (headTeacherId == null) {
            logger.error("Attempt to get absence case for head teacher id providing null id.");
            throw new IllegalArgumentException("Head teacher id cannot be null.");
        }
        User headTeacherExample = User.builder().withId(headTeacherId).build();
        Example<AbsenceCase> caseExample = Example.of(new AbsenceCase.Builder().withHeadTeacher(headTeacherExample).withIsCaseResolved(false).build());
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        try {
            return repository.findAll(caseExample, sort);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting all absence cases for head teacher id: " + headTeacherId + ".";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<AbsenceCase> getAllActiveCasesForHeadTeacherPaginated(Long headTeacherId, Long offset, Long limit) throws ServiceOperationException {
        if (headTeacherId == null) {
            logger.error("Attempt to get all paginated absence case for head teacher id providing null id.");
            throw new IllegalArgumentException("Head teacher id cannot be null.");
        }
        try {
            return repository.findAllActiveManagedByHeadTeacherByOffsetAndLimit(headTeacherId, offset, limit);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting all paginated absence cases for head teacher id: " + headTeacherId + ".";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public void deleteCase(Long id) throws ServiceOperationException {
        if (id == null) {
            logger.error("Attempt to delete absence case providing null id.");
            throw new IllegalArgumentException("Id cannot be null.");
        }
        try {
            repository.deleteById(id);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during deleting absence case.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public boolean caseExists(Long id) throws ServiceOperationException {
        if (id == null) {
            logger.error("Attempt to check if absence case exists providing null id.");
            throw new IllegalArgumentException("Id cannot be null.");
        }
        try {
            return repository.existsById(id);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during checking if absence case exists.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public long casesCount() throws ServiceOperationException {
        try {
            return repository.count();
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting number of absence cases.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public long activeCasesCount() throws ServiceOperationException {
        Example<AbsenceCase> activeCaseExample = Example.of(new AbsenceCase.Builder().withIsCaseResolved(false).build());
        try {
            return repository.count(activeCaseExample);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting number of active absence cases.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public long activeCasesForHeadTeacherCount(Long headTeacherId) throws ServiceOperationException {
        User headTeacherExample = User.builder().withId(headTeacherId).build();
        Example<AbsenceCase> activeCaseForHeadTeacherExample = Example.of(new AbsenceCase.Builder().withHeadTeacher(headTeacherExample).withIsCaseResolved(false).build());
        try {
            return repository.count(activeCaseForHeadTeacherExample);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting number of active absence cases for Head Teacher id: " + headTeacherId + ".";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public long userCasesCount(Long userId) throws ServiceOperationException {
        User userExample = User.builder().withId(userId).build();
        Example<AbsenceCase> userCaseExample = Example.of(new AbsenceCase.Builder().withUser(userExample).build());
        try {
            return repository.count(userCaseExample);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting number of absence cases for user id: " + userId + ".";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }
}
