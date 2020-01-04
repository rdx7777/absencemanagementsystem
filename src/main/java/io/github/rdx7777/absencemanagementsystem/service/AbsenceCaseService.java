package io.github.rdx7777.absencemanagementsystem.service;

import io.github.rdx7777.absencemanagementsystem.model.AbsenceCase;
import io.github.rdx7777.absencemanagementsystem.repository.AbsenceCaseRepository;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            logger.error("Attempt to add null case.");
            throw new IllegalArgumentException("AbsenceCase cannot be null.");
        }
        Long caseId = aCase.getId();
        if (caseId != null && repository.existsById(caseId)) {
            logger.error("Attempt to add case already existing in database.");
            throw new ServiceOperationException("AbsenceCase already exists in database.");
        }
        return repository.save(aCase);
    }

    public AbsenceCase updateCase(AbsenceCase aCase) throws ServiceOperationException {
        if (aCase == null) {
            logger.error("Attempt to update case providing null case.");
            throw new IllegalArgumentException("AbsenceCase cannot be null.");
        }
        Long caseId = aCase.getId();
        if (caseId == null || !repository.existsById(caseId)) {
            logger.error("Attempt to update not existing case.");
            throw new ServiceOperationException("Given case does not exist in database.");
        }
        return repository.save(aCase);
    }

    public Optional<AbsenceCase> getCaseById(Long id) {
        if (id == null) {
            logger.error("Attempt to get case by id providing null id.");
            throw new IllegalArgumentException("Id cannot be null.");
        }
        return repository.findById(id);
    }

    public Collection<AbsenceCase> getAllCases() {
        return repository.findAll();
    }

    public Collection<AbsenceCase> getAllActiveCases() {
        Example<AbsenceCase> example = Example.of(new AbsenceCase.Builder().withIsCaseResolved(false).build());
        return repository.findAll(example);
    }

    public Collection<AbsenceCase> getAllUserCases(Long userId) {
        Example<AbsenceCase> example = Example.of(new AbsenceCase.Builder().withUserId(userId).build());
        return repository.findAll(example);
    }

    public Collection<AbsenceCase> getAllActiveCasesForHeadTeacher(Long headTeacherId) {
        Example<AbsenceCase> example = Example.of(new AbsenceCase.Builder().withHeadTeacherId(headTeacherId).withIsCaseResolved(false).build());
        return repository.findAll(example);
    }

    public void deleteCase(Long id) {
        if (id == null) {
            logger.error("Attempt to delete case providing null id.");
            throw new IllegalArgumentException("Id cannot be null.");
        }
        repository.deleteById(id);
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
