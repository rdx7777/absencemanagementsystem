package io.github.rdx7777.absencemanagementsystem.service;

import io.github.rdx7777.absencemanagementsystem.model.Case;
import io.github.rdx7777.absencemanagementsystem.repository.CaseRepository;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class CaseService {

    private final CaseRepository repository;

    private final Logger logger = LoggerFactory.getLogger(CaseService.class);

    public CaseService(CaseRepository repository) {
        this.repository = repository;
    }

    public Case addCase(Case aCase) throws ServiceOperationException {
        if (aCase == null) {
            logger.error("Attempt to add null case.");
            throw new IllegalArgumentException("Case cannot be null.");
        }
        try {
            Long caseId = aCase.getId();
            if (caseId != null && repository.existsById(caseId)) {
                logger.error("Attempt to add case already existing in database.");
                throw new ServiceOperationException("Case already exists in database.");
            }
            return repository.save(aCase);
        } catch (Exception e) {
            String message = "An error occurred during adding case to database.";
            logger.error(message);
            throw new ServiceOperationException(message, e);
        }
    }

    public Case updateCase(Case aCase) throws ServiceOperationException {
        if (aCase == null) {
            logger.error("Attempt to update case providing null case.");
            throw new IllegalArgumentException("Case cannot be null.");
        }
        try {
            Long caseId = aCase.getId();
            if (caseId == null || !repository.existsById(caseId)) {
                logger.error("Attempt to update not existing case.");
                throw new ServiceOperationException("Given case does not exist in database.");
            }
            return repository.save(aCase);
        } catch (Exception e) {
            String message = "An error occurred during updating case.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Optional<Case> getCaseById(Long id) throws ServiceOperationException {
        if (id == null) {
            logger.error("Attempt to get case by id providing null id.");
            throw new IllegalArgumentException("Id cannot be null.");
        }
        try {
            return repository.findById(id);
        } catch (Exception e) {
            String message = "An error occurred during getting case by id.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<Case> getAllCases() throws ServiceOperationException {
        try {
            return repository.findAll();
        } catch (Exception e) {
            String message = "An error occurred during getting all cases.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<Case> getAllActiveCases() throws ServiceOperationException {
        Example<Case> example = Example.of(new Case.Builder().withIsCaseResolved(false).build());
        try {
            return repository.findAll(example);
        } catch (Exception e) {
            String message = "An error occurred during getting all active cases.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<Case> getAllUserCases(Long userId) throws ServiceOperationException {
        Example<Case> example = Example.of(new Case.Builder().withUserId(userId).build());
        try {
            return repository.findAll(example);
        } catch (Exception e) {
            String message = "An error occurred during getting all user cases.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<Case> getAllActiveCasesForHeadTeacher(Long headTeacherId) throws ServiceOperationException {
        Example<Case> example = Example.of(new Case.Builder().withHeadTeacherId(headTeacherId).withIsCaseResolved(false).build());
        try {
            return repository.findAll(example);
        } catch (Exception e) {
            String message = "An error occurred during getting all active cases for Head Teacher.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }


    public boolean caseExists(Long id) throws ServiceOperationException {
        if (id == null) {
            logger.error("Attempt to check if case exists providing null id.");
            throw new IllegalArgumentException("Id cannot be null.");
        }
        try {
            return repository.existsById(id);
        } catch (Exception e) {
            String message = "An error occurred during checking if case exists.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public long casesCount() throws ServiceOperationException {
        try {
            return repository.count();
        } catch (Exception e) {
            String message = "An error occurred during getting number of cases.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }
}
