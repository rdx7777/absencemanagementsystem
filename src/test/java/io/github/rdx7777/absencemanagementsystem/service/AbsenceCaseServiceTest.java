package io.github.rdx7777.absencemanagementsystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import io.github.rdx7777.absencemanagementsystem.generators.AbsenceCaseGenerator;
import io.github.rdx7777.absencemanagementsystem.model.AbsenceCase;
import io.github.rdx7777.absencemanagementsystem.repository.AbsenceCaseRepository;

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
class AbsenceCaseServiceTest {

    @Mock
    AbsenceCaseRepository repository;

    @InjectMocks
    AbsenceCaseService absenceCaseService;

    @Test
    void shouldAddCase() throws ServiceOperationException {
        // given
        AbsenceCase caseToAdd = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        AbsenceCase addedCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(repository.existsById(caseToAdd.getId())).thenReturn(false);
        when(repository.save(caseToAdd)).thenReturn(addedCase);

        // when
        AbsenceCase result = absenceCaseService.addCase(caseToAdd);

        // then
        assertEquals(addedCase, result);
        verify(repository).existsById(caseToAdd.getId());
        verify(repository).save(caseToAdd);
    }

    @Test
    void shouldAddCaseWithNullId() throws ServiceOperationException {
        // given
        AbsenceCase caseToAdd = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayTypeAndNullId();
        AbsenceCase addedCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(repository.save(caseToAdd)).thenReturn(addedCase);

        // when
        AbsenceCase result = absenceCaseService.addCase(caseToAdd);

        // then
        assertEquals(addedCase, result);
        verify(repository, never()).existsById(caseToAdd.getId());
        verify(repository).save(caseToAdd);
    }

    @Test
    void addCaseMethodShouldThrowIllegalArgumentExceptionForNullCase() {
        assertThrows(IllegalArgumentException.class, () -> absenceCaseService.addCase(null));
        verify(repository, never()).existsById(any());
        verify(repository, never()).save(any());
    }

    @Test
    void addCaseMethodShouldThrowExceptionForCaseExistingInDatabase() {
        // given
        AbsenceCase aCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(repository.existsById(aCase.getId())).thenReturn(true);

        // then
        assertThrows(ServiceOperationException.class, () -> absenceCaseService.addCase(aCase));
        verify(repository).existsById(aCase.getId());
        verify(repository, never()).save(aCase);
    }

    @Test
    void shouldUpdateGivenCaseInDatabase() throws ServiceOperationException {
        // given
        AbsenceCase caseToUpdate = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        AbsenceCase updatedCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(repository.existsById(caseToUpdate.getId())).thenReturn(true);
        when(repository.save(caseToUpdate)).thenReturn(updatedCase);

        // when
        AbsenceCase result = absenceCaseService.updateCase(caseToUpdate);

        // then
        assertEquals(updatedCase, result);
        verify(repository).existsById(caseToUpdate.getId());
        verify(repository).save(caseToUpdate);
    }

    @Test
    void updateCaseMethodShouldThrowIllegalArgumentExceptionForNullCase() {
        assertThrows(IllegalArgumentException.class, () -> absenceCaseService.updateCase(null));
        verify(repository, never()).existsById(any());
        verify(repository, never()).save(any());
    }

    @Test
    void updateCaseMethodShouldThrowExceptionForNullCaseId() {
        // given
        AbsenceCase aCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayTypeAndNullId();

        // then
        assertThrows(ServiceOperationException.class, () -> absenceCaseService.updateCase(aCase));
        verify(repository, never()).existsById(any());
        verify(repository, never()).save(any());
    }

    @Test
    void updateCaseMethodShouldThrowExceptionWhenCaseDoesNotExistInDatabase() {
        // given
        AbsenceCase aCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(repository.existsById(aCase.getId())).thenReturn(false);

        // then
        assertThrows(ServiceOperationException.class, () -> absenceCaseService.updateCase(aCase));
        verify(repository).existsById(aCase.getId());
        verify(repository, never()).save(aCase);
    }

    @Test
    void shouldReturnCaseByGivenId() throws ServiceOperationException {
        // given
        AbsenceCase aCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(repository.findById(1L)).thenReturn(Optional.of(aCase));

        // when
        Optional<AbsenceCase> result = absenceCaseService.getCaseById(1L);

        // then
        assertTrue(result.isPresent());
        assertEquals(aCase, result.get());
        verify(repository).findById(1L);
    }

    @Test
    void getCaseByIdMethodShouldThrowIllegalArgumentExceptionForNullCaseId() {
        assertThrows(IllegalArgumentException.class, () -> absenceCaseService.getCaseById(null));
        verify(repository, never()).findById(1L);
    }

    @Test
    void shouldReturnAllCases() throws ServiceOperationException {
        // given
        List<AbsenceCase> cases = List.of(AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType(), AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType());
        when(repository.findAll()).thenReturn(cases);

        // when
        Collection<AbsenceCase> result = absenceCaseService.getAllCases();

        // then
        assertEquals(cases, result);
        verify(repository).findAll();
    }

    @Test
    void shouldReturnAllActiveCases() throws ServiceOperationException {
        // given
        List<AbsenceCase> cases = List.of(AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType(), AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType());
        Example example = Example.of(new AbsenceCase.Builder().withIsCaseResolved(false).build());
        when(repository.findAll(example)).thenReturn(cases);

        // when
        Collection<AbsenceCase> result = absenceCaseService.getAllActiveCases();

        // then
        assertEquals(cases, result);
        verify(repository).findAll(example);
    }

    @Test
    void shouldReturnAllUserCases() throws ServiceOperationException {
        // given
        List<AbsenceCase> cases = List.of(AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificUserId(2L), AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificUserId(2L));
        Example example = Example.of(new AbsenceCase.Builder().withUserId(2L).build());
        when(repository.findAll(example)).thenReturn(cases);

        // when
        Collection<AbsenceCase> result = absenceCaseService.getAllUserCases(2L);

        // then
        assertEquals(cases, result);
        verify(repository).findAll(example);
    }

    @Test
    void getAllUserCasesMethodShouldThrowIllegalArgumentExceptionForNullUserId() {
        assertThrows(IllegalArgumentException.class, () -> absenceCaseService.getAllUserCases(null));
        verify(repository, never()).findAll((Example<AbsenceCase>) any());
    }

    @Test
    void shouldReturnAllActiveCasesForHeadTeacher() throws ServiceOperationException {
        // given
        List<AbsenceCase> cases = List.of(AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificHeadTeacherId(4L), AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificHeadTeacherId(4L));
        Example example = Example.of(new AbsenceCase.Builder().withHeadTeacherId(4L).withIsCaseResolved(false).build());
        when(repository.findAll(example)).thenReturn(cases);

        // when
        Collection<AbsenceCase> result = absenceCaseService.getAllActiveCasesForHeadTeacher(4L);

        // then
        assertEquals(cases, result);
        verify(repository).findAll(example);
    }

    @Test
    void getAllActiveCasesForHeadTeacherMethodShouldThrowIllegalArgumentExceptionForNullHeadTeacherId() {
        assertThrows(IllegalArgumentException.class, () -> absenceCaseService.getAllActiveCasesForHeadTeacher(null));
        verify(repository, never()).findAll((Example<AbsenceCase>) any());
    }

    @Test
    void shouldDeleteCase() throws ServiceOperationException {
        // given
        doNothing().when(repository).deleteById(1L);

        // when
        absenceCaseService.deleteCase(1L);

        // then
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteCaseMethodShouldThrowIllegalArgumentExceptionForNullCaseId() {
        assertThrows(IllegalArgumentException.class, () -> absenceCaseService.deleteCase(null));
    }

    @Test
    void shouldReturnTrueWhenCaseExistsInDatabase() throws ServiceOperationException {
        // given
        when(repository.existsById(1L)).thenReturn(true);

        // when
        boolean result = absenceCaseService.caseExists(1L);

        // then
        assertTrue(result);
        verify(repository).existsById(1L);
    }

    @Test
    void shouldReturnFalseWhenCaseDoesNotExistInDatabase() throws ServiceOperationException {
        // given
        when(repository.existsById(1L)).thenReturn(false);

        // when
        boolean result = absenceCaseService.caseExists(1L);

        // then
        assertFalse(result);
        verify(repository).existsById(1L);
    }

    @Test
    void caseExistsMethodShouldThrowIllegalArgumentExceptionForNullCaseId() {
        assertThrows(IllegalArgumentException.class, () -> absenceCaseService.caseExists(null));
    }

    @Test
    void shouldReturnNumberOfCases() throws ServiceOperationException {
        // given
        when(repository.count()).thenReturn(10L);

        // when
        long result = absenceCaseService.casesCount();

        // then
        assertEquals(10L, result);
        verify(repository).count();
    }
}
