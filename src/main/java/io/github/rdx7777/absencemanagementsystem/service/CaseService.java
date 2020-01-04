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
        Long caseId = aCase.getId();
        if (caseId != null && repository.existsById(caseId)) {
            logger.error("Attempt to add case already existing in database.");
            throw new ServiceOperationException("Case already exists in database.");
        }
        return repository.save(aCase);
    }

    public Case updateCase(Case aCase) throws ServiceOperationException {
        if (aCase == null) {
            logger.error("Attempt to update case providing null case.");
            throw new IllegalArgumentException("Case cannot be null.");
        }
        Long caseId = aCase.getId();
        if (caseId == null || !repository.existsById(caseId)) {
            logger.error("Attempt to update not existing case.");
            throw new ServiceOperationException("Given case does not exist in database.");
        }
        return repository.save(aCase);
    }

    public Optional<Case> getCaseById(Long id) {
        if (id == null) {
            logger.error("Attempt to get case by id providing null id.");
            throw new IllegalArgumentException("Id cannot be null.");
        }
        return repository.findById(id);
    }

    public Collection<Case> getAllCases() {
        return repository.findAll();
    }

    public Collection<Case> getAllActiveCases() {
        Example<Case> example = Example.of(new Case.Builder().withIsCaseResolved(false).build());
        return repository.findAll(example);
    }

    public Collection<Case> getAllUserCases(Long userId) {
        Example<Case> example = Example.of(new Case.Builder().withUserId(userId).build());
        return repository.findAll(example);
    }

    public Collection<Case> getAllActiveCasesForHeadTeacher(Long headTeacherId) {
        Example<Case> example = Example.of(new Case.Builder().withHeadTeacherId(headTeacherId).withIsCaseResolved(false).build());
        return repository.findAll(example);
    }

    public boolean caseExists(Long id) {
        if (id == null) {
            logger.error("Attempt to check if case exists providing null id.");
            throw new IllegalArgumentException("Id cannot be null.");
        }
        return repository.existsById(id);
    }

    public long casesCount() {
        return repository.count();
    }
}
