package io.github.rdx7777.absencemanagementsystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import io.github.rdx7777.absencemanagementsystem.generators.AbsenceCaseGenerator;
import io.github.rdx7777.absencemanagementsystem.model.AbsenceCase;
import io.github.rdx7777.absencemanagementsystem.model.User;
import io.github.rdx7777.absencemanagementsystem.repository.AbsenceCaseRepository;
import io.github.rdx7777.absencemanagementsystem.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class AbsenceCaseServiceTest {

    @Mock
    AbsenceCaseRepository repository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    AbsenceCaseService absenceCaseService;

    @Test
    void shouldAddCase() throws ServiceOperationException {
        // given
        AbsenceCase caseToAdd = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        AbsenceCase addedCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(userRepository.findUserByEmail(caseToAdd.getUser().getEmail())).thenReturn(Optional.of(caseToAdd.getUser()));
        when(userRepository.findUserByEmail(caseToAdd.getHeadTeacher().getEmail())).thenReturn(Optional.of(caseToAdd.getHeadTeacher()));
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
        when(userRepository.findUserByEmail(caseToAdd.getUser().getEmail())).thenReturn(Optional.of(caseToAdd.getUser()));
        when(userRepository.findUserByEmail(caseToAdd.getHeadTeacher().getEmail())).thenReturn(Optional.of(caseToAdd.getHeadTeacher()));
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
    void addCaseMethodShouldThrowExceptionWhenAnErrorOccursDuringAddingCaseToDatabase() {
        // given
        AbsenceCase aCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(repository.existsById(aCase.getId())).thenReturn(false);
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).save(aCase);

        // then
        assertThrows(ServiceOperationException.class, () -> absenceCaseService.addCase(aCase));
        verify(repository).existsById(aCase.getId());
        verify(repository).save(aCase);
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
    void updateUserMethodShouldThrowExceptionWhenAnErrorOccursDuringUpdatingUserInDatabase() {
        // given
        AbsenceCase aCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(repository.existsById(aCase.getId())).thenReturn(true);
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).save(aCase);

        // then
        assertThrows(ServiceOperationException.class, () -> absenceCaseService.updateCase(aCase));
        verify(repository).existsById(aCase.getId());
        verify(repository).save(aCase);
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
    void getCaseByIdMethodShouldThrowExceptionWhenAnErrorOccursDuringGettingCaseById() {
        // given
        doThrow(new NoSuchElementException()).when(repository).findById(1L);

        // then
        assertThrows(ServiceOperationException.class, () -> absenceCaseService.getCaseById(1L));
        verify(repository).findById(1L);
    }

    @Test
    void shouldReturnAllCases() throws ServiceOperationException {
        // given
        List<AbsenceCase> cases = List.of(AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType(), AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType());
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        when(repository.findAll(sort)).thenReturn(cases);

        // when
        Collection<AbsenceCase> result = absenceCaseService.getAllCases();

        // then
        assertEquals(cases, result);
        verify(repository).findAll(sort);
    }

    @Test
    void getAllCasesMethodShouldThrowExceptionWhenAnErrorOccursDuringGettingAllCases() {
        // given
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).findAll(sort);

        // then
        assertThrows(ServiceOperationException.class, () -> absenceCaseService.getAllCases());
        verify(repository).findAll(sort);
    }

    @Test
    void shouldReturnAllActiveCases() throws ServiceOperationException {
        // given
        List<AbsenceCase> cases = List.of(AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType(), AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType());
        Example<AbsenceCase> example = Example.of(new AbsenceCase.Builder().withIsCaseResolved(false).build());
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        when(repository.findAll(example, sort)).thenReturn(cases);

        // when
        Collection<AbsenceCase> result = absenceCaseService.getAllActiveCases();

        // then
        assertEquals(cases, result);
        verify(repository).findAll(example, sort);
    }

    @Test
    void getAllActiveCasesMethodShouldThrowExceptionWhenAnErrorOccursDuringGettingAllActiveCases() {
        // given
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).findAll((Example<AbsenceCase>) any(), (Sort) any());

        // then
        assertThrows(ServiceOperationException.class, () -> absenceCaseService.getAllActiveCases());
        verify(repository).findAll((Example<AbsenceCase>) any(), (Sort) any());
    }

    @Test
    void shouldReturnAllUserCases() throws ServiceOperationException {
        // given
        List<AbsenceCase> cases = List.of(AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificUserId(2L), AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificUserId(2L));
        Example<AbsenceCase> example = Example.of(new AbsenceCase.Builder().withUser(User.builder().withId(2L).build()).build());
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        when(repository.findAll(example, sort)).thenReturn(cases);

        // when
        Collection<AbsenceCase> result = absenceCaseService.getAllUserCases(2L);

        // then
        assertEquals(cases, result);
        verify(repository).findAll(example, sort);
    }

    @Test
    void getAllUserCasesMethodShouldThrowIllegalArgumentExceptionForNullUserId() {
        assertThrows(IllegalArgumentException.class, () -> absenceCaseService.getAllUserCases(null));
        verify(repository, never()).findAll((Example<AbsenceCase>) any());
    }

    @Test
    void getAllUserCasesMethodShouldThrowExceptionWhenAnErrorOccursDuringGettingAllUserCases() {
        // given
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).findAll((Example<AbsenceCase>) any(), (Sort) any());

        // then
        assertThrows(ServiceOperationException.class, () -> absenceCaseService.getAllUserCases(1L));
        verify(repository).findAll((Example<AbsenceCase>) any(), (Sort) any());
    }

    @Test
    void shouldReturnAllActiveCasesForHeadTeacher() throws ServiceOperationException {
        // given
        List<AbsenceCase> cases = List.of(AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificHeadTeacherId(4L), AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificHeadTeacherId(4L));
        Example<AbsenceCase> example = Example.of(new AbsenceCase.Builder()
            .withHeadTeacher(User.builder().withId(4L).build())
            .withIsCaseResolved(false)
            .build());
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        when(repository.findAll(example, sort)).thenReturn(cases);

        // when
        Collection<AbsenceCase> result = absenceCaseService.getAllActiveCasesForHeadTeacher(4L);

        // then
        assertEquals(cases, result);
        verify(repository).findAll(example, sort);
    }

    @Test
    void getAllActiveCasesForHeadTeacherMethodShouldThrowIllegalArgumentExceptionForNullHeadTeacherId() {
        assertThrows(IllegalArgumentException.class, () -> absenceCaseService.getAllActiveCasesForHeadTeacher(null));
        verify(repository, never()).findAll((Example<AbsenceCase>) any());
    }

    @Test
    void getAllActiveCasesForHeadTeacherMethodShouldThrowExceptionWhenAnErrorOccursDuringGettingAllActiveCasesForHeadTeacher() {
        // given
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).findAll((Example<AbsenceCase>) any(), (Sort) any());

        // then
        assertThrows(ServiceOperationException.class, () -> absenceCaseService.getAllActiveCasesForHeadTeacher(1L));
        verify(repository).findAll((Example<AbsenceCase>) any(), (Sort) any());
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
    void deleteCaseMethodShouldThrowExceptionWhenAnErrorOccursDuringDeletingCase() {
        // given
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).deleteById(1L);

        // then
        assertThrows(ServiceOperationException.class, () -> absenceCaseService.deleteCase(1L));
        verify(repository).deleteById(1L);
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
    void caseExistsMethodShouldThrowExceptionWhenAnErrorOccursDuringCheckingCaseExists() {
        // given
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).existsById(1L);

        // then
        assertThrows(ServiceOperationException.class, () -> absenceCaseService.caseExists(1L));
        verify(repository).existsById(1L);
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

    @Test
    void casesCountMethodShouldThrowExceptionWhenAnErrorOccursDuringGettingNumberOfCases() {
        // given
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).count();

        // then
        assertThrows(ServiceOperationException.class, () -> absenceCaseService.casesCount());
        verify(repository).count();
    }
}
