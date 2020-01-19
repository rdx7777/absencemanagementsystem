package io.github.rdx7777.absencemanagementsystem.service;

import io.github.rdx7777.absencemanagementsystem.model.AbsenceCase;
import io.github.rdx7777.absencemanagementsystem.repository.AbsenceCaseRepository;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class AbsenceCaseService {

    private final Logger logger = LoggerFactory.getLogger(AbsenceCaseService.class);

    private final AbsenceCaseRepository repository;

    public AbsenceCaseService(AbsenceCaseRepository repository) {
        this.repository = repository;
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
            return repository.save(aCase);
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
            return repository.save(aCase);
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
        try {
            return repository.findAll();
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting all absence cases.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<AbsenceCase> getAllActiveCases() throws ServiceOperationException {
        Example<AbsenceCase> example = Example.of(new AbsenceCase.Builder().withIsCaseResolved(false).build());
        try {
            return repository.findAll(example);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting all active absence cases.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<AbsenceCase> getAllUserCases(Long userId) throws ServiceOperationException {
        if (userId == null) {
            logger.error("Attempt to get absence case for user id providing null id.");
            throw new IllegalArgumentException("User id cannot be null.");
        }
        Example<AbsenceCase> example = Example.of(new AbsenceCase.Builder().withUserId(userId).build());
        try {
            return repository.findAll(example);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting all absence cases for user id: " + userId + ".";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<AbsenceCase> getAllActiveCasesForHeadTeacher(Long headTeacherId) throws ServiceOperationException {
        if (headTeacherId == null) {
            logger.error("Attempt to get absence case for head teacher id providing null id.");
            throw new IllegalArgumentException("Head teacher id cannot be null.");
        }
        Example<AbsenceCase> example = Example.of(new AbsenceCase.Builder().withHeadTeacherId(headTeacherId).withIsCaseResolved(false).build());
        try {
            return repository.findAll(example);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting all absence cases for head teacher id: " + headTeacherId + ".";
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
}
