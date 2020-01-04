package io.github.rdx7777.absencemanagementsystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import io.github.rdx7777.absencemanagementsystem.generators.CaseGenerator;
import io.github.rdx7777.absencemanagementsystem.model.Case;
import io.github.rdx7777.absencemanagementsystem.repository.CaseRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

@ExtendWith(MockitoExtension.class)
class CaseServiceTest {

    @Mock
    CaseRepository repository;

    @InjectMocks
    CaseService caseService;

    @Test
    void shouldAddCase() throws ServiceOperationException {
        // given
        Case caseToAdd = CaseGenerator.getRandomCaseWithAllDayPartDayType();
        Case addedCase = CaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(repository.existsById(caseToAdd.getId())).thenReturn(false);
        when(repository.save(caseToAdd)).thenReturn(addedCase);

        // when
        Case result = caseService.addCase(caseToAdd);

        // then
        assertEquals(addedCase, result);
        verify(repository).existsById(caseToAdd.getId());
        verify(repository).save(caseToAdd);
    }

    @Test
    void shouldAddCaseWithNullId() throws ServiceOperationException {
        // given
        Case caseToAdd = CaseGenerator.getRandomCaseWithAllDayPartDayTypeAndNullId();
        Case addedCase = CaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(repository.save(caseToAdd)).thenReturn(addedCase);

        // when
        Case result = caseService.addCase(caseToAdd);

        // then
        assertEquals(addedCase, result);
        verify(repository, never()).existsById(caseToAdd.getId());
        verify(repository).save(caseToAdd);
    }

    @Test
    void addCaseMethodShouldThrowIllegalArgumentExceptionForNullCase() {
        assertThrows(IllegalArgumentException.class, () -> caseService.addCase(null));
        verify(repository, never()).existsById(any());
        verify(repository, never()).save(any());
    }

    @Test
    void addCaseMethodShouldThrowExceptionForCaseExistingInDatabase() {
        // given
        Case aCase = CaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(repository.existsById(aCase.getId())).thenReturn(true);

        // then
        assertThrows(ServiceOperationException.class, () -> caseService.addCase(aCase));
        verify(repository).existsById(aCase.getId());
        verify(repository, never()).save(aCase);
    }

    @Test
    void shouldUpdateGivenCaseInDatabase() throws ServiceOperationException {
        // given
        Case caseToUpdate = CaseGenerator.getRandomCaseWithAllDayPartDayType();
        Case updatedCase = CaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(repository.existsById(caseToUpdate.getId())).thenReturn(true);
        when(repository.save(caseToUpdate)).thenReturn(updatedCase);

        // when
        Case result = caseService.updateCase(caseToUpdate);

        // then
        assertEquals(updatedCase, result);
        verify(repository).existsById(caseToUpdate.getId());
        verify(repository).save(caseToUpdate);
    }

    @Test
    void updateCaseMethodShouldThrowIllegalArgumentExceptionForNullCase() {
        assertThrows(IllegalArgumentException.class, () -> caseService.updateCase(null));
        verify(repository, never()).existsById(any());
        verify(repository, never()).save(any());
    }

    @Test
    void updateCaseMethodShouldThrowExceptionForNullCaseId() {
        // given
        Case aCase = CaseGenerator.getRandomCaseWithAllDayPartDayTypeAndNullId();

        // then
        assertThrows(ServiceOperationException.class, () -> caseService.updateCase(aCase));
        verify(repository, never()).existsById(any());
        verify(repository, never()).save(any());
    }

    @Test
    void updateCaseMethodShouldThrowExceptionWhenCaseDoesNotExistInDatabase() {
        // given
        Case aCase = CaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(repository.existsById(aCase.getId())).thenReturn(false);

        // then
        assertThrows(ServiceOperationException.class, () -> caseService.updateCase(aCase));
        verify(repository).existsById(aCase.getId());
        verify(repository, never()).save(aCase);
    }

    @Test
    void shouldReturnCaseByGivenId() {
        // given
        Case aCase = CaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(repository.findById(1L)).thenReturn(Optional.of(aCase));

        // when
        Optional<Case> result = caseService.getCaseById(1L);

        // then
        assertTrue(result.isPresent());
        assertEquals(aCase, result.get());
        verify(repository).findById(1L);
    }

    @Test
    void getCaseByIdMethodShouldThrowIllegalArgumentExceptionForNullCaseId() {
        assertThrows(IllegalArgumentException.class, () -> caseService.getCaseById(null));
        verify(repository, never()).findById(1L);
    }

    @Test
    void shouldReturnAllCases() {
        // given
        List<Case> cases = List.of(CaseGenerator.getRandomCaseWithAllDayPartDayType(), CaseGenerator.getRandomCaseWithAllDayPartDayType());
        when(repository.findAll()).thenReturn(cases);

        // when
        Collection<Case> result = caseService.getAllCases();

        // then
        assertEquals(cases, result);
        verify(repository).findAll();
    }

    @Test
    void shouldReturnAllActiveCases() {
        // given
        List<Case> cases = List.of(CaseGenerator.getRandomCaseWithAllDayPartDayType(), CaseGenerator.getRandomCaseWithAllDayPartDayType());
        Example example = Example.of(new Case.Builder().withIsCaseResolved(false).build());
        when(repository.findAll(example)).thenReturn(cases);

        // when
        Collection<Case> result = caseService.getAllActiveCases();

        // then
        assertEquals(cases, result);
        verify(repository).findAll(example);
    }

    @Test
    void shouldReturnAllUserCases() {
        // given
        List<Case> cases = List.of(CaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificUserId(2L), CaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificUserId(2L));
        Example example = Example.of(new Case.Builder().withUserId(2L).build());
        when(repository.findAll(example)).thenReturn(cases);

        // when
        Collection<Case> result = caseService.getAllUserCases(2L);

        // then
        assertEquals(cases, result);
        verify(repository).findAll(example);
    }

    @Test
    void shouldReturnAllActiveCasesForHeadTeacher() {
        // given
        List<Case> cases = List.of(CaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificHeadTeacherId(4L), CaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificHeadTeacherId(4L));
        Example example = Example.of(new Case.Builder().withHeadTeacherId(4L).withIsCaseResolved(false).build());
        when(repository.findAll(example)).thenReturn(cases);

        // when
        Collection<Case> result = caseService.getAllActiveCasesForHeadTeacher(4L);

        // then
        assertEquals(cases, result);
        verify(repository).findAll(example);
    }

    @Test
    void shouldReturnTrueWhenCaseExistsInDatabase() {
        // given
        when(repository.existsById(1L)).thenReturn(true);

        // when
        boolean result = caseService.caseExists(1L);

        // then
        assertTrue(result);
        verify(repository).existsById(1L);
    }

    @Test
    void shouldReturnFalseWhenCaseDoesNotExistInDatabase() {
        // given
        when(repository.existsById(1L)).thenReturn(false);

        // when
        boolean result = caseService.caseExists(1L);

        // then
        assertFalse(result);
        verify(repository).existsById(1L);
    }

    @Test
    void caseExistsMethodShouldThrowIllegalArgumentExceptionForNullCaseId() {
        assertThrows(IllegalArgumentException.class, () -> caseService.caseExists(null));
    }

    @Test
    void shouldReturnNumberOfCases() {
        // given
        when(repository.count()).thenReturn(10L);

        // when
        long result = caseService.casesCount();

        // then
        assertEquals(10L, result);
        verify(repository).count();
    }
}
