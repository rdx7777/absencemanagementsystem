package io.github.rdx7777.absencemanagementsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.rdx7777.absencemanagementsystem.generators.AbsenceCaseGenerator;
import io.github.rdx7777.absencemanagementsystem.generators.UserGenerator;
import io.github.rdx7777.absencemanagementsystem.model.AbsenceCase;
import io.github.rdx7777.absencemanagementsystem.model.AppModelMapper;
import io.github.rdx7777.absencemanagementsystem.model.User;
import io.github.rdx7777.absencemanagementsystem.repository.UserRepository;
import io.github.rdx7777.absencemanagementsystem.security.jwt.AuthEntryPointJwt;
import io.github.rdx7777.absencemanagementsystem.security.jwt.JwtUtils;
import io.github.rdx7777.absencemanagementsystem.security.services.UserDetailsServiceImpl;
import io.github.rdx7777.absencemanagementsystem.service.AbsenceCaseService;
import io.github.rdx7777.absencemanagementsystem.service.EmailService;
import io.github.rdx7777.absencemanagementsystem.service.ServiceOperationException;
import io.github.rdx7777.absencemanagementsystem.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AbsenceCaseController.class)
@WithMockUser(roles = "ADMIN")
class AbsenceCaseControllerTest {

    @MockBean
    private AbsenceCaseService caseService;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private AppModelMapper appModelMapper;

    @MockBean
    private UserRepository repository;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    private JwtUtils jwtUtils;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldAddCase() throws Exception {
        AppModelMapper unMockedAppModelMapper = new AppModelMapper(repository);
        AbsenceCase caseToAdd = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        AbsenceCase addedCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(caseService.caseExists(caseToAdd.getId())).thenReturn(false);
        when(caseService.addCase(caseToAdd)).thenReturn(addedCase);
        User headTeacher = UserGenerator.getRandomHeadTeacher();
        User user = UserGenerator.getRandomEmployee();
        when(userService.getUserById(caseToAdd.getHeadTeacher().getId())).thenReturn(Optional.of(headTeacher));
        when(userService.getUserById(caseToAdd.getUser().getId())).thenReturn(Optional.of(user));
        when(appModelMapper.mapToAbsenceCaseDTO(addedCase)).thenReturn(unMockedAppModelMapper.mapToAbsenceCaseDTO(addedCase));
        doNothing().when(emailService).sendEmailToCoverSupervisor(any(), any(), any());

        String url = "/api/cases";

        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(caseToAdd))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(appModelMapper.mapToAbsenceCaseDTO(addedCase))));

        verify(caseService).caseExists(caseToAdd.getId());
        verify(caseService).addCase(caseToAdd);
        verify(emailService).sendEmailToCoverSupervisor(any(), any(), any());
    }

    @Test
    void shouldReturnBadRequestStatusDuringAddingNullAsCase() throws Exception {
        String url = "/api/cases";

        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(null))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(caseService, never()).caseExists(any());
        verify(caseService, never()).addCase(any());
    }

    @Test
    void shouldReturnConflictStatusDuringAddingCaseWhenCaseExistsInDatabase() throws Exception {
        AbsenceCase aCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(caseService.caseExists(aCase.getId())).thenReturn(true);

        String url = "/api/cases";

        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(aCase))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());

        verify(caseService).caseExists(aCase.getId());
        verify(caseService, never()).addCase(aCase);
    }

    @Test
    void addMethodShouldReturnBadRequestForInvalidCase() throws Exception {
        AbsenceCase invalidCase = AbsenceCase.builder().withId(1L).withUser(null).build();
        when(caseService.caseExists(invalidCase.getId())).thenReturn(false);

        String url = "/api/cases";

        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(invalidCase))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(caseService).caseExists(invalidCase.getId());
        verify(caseService, never()).addCase(invalidCase);
    }

    @Test
    void shouldReturnUnsupportedMediaTypeStatusDuringAddingCaseWithNotSupportedMediaType() throws Exception {
        AbsenceCase caseToAdd = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        String url = "/api/cases";

        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_XML)
            .content(mapper.writeValueAsBytes(caseToAdd))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnsupportedMediaType());

        verify(caseService, never()).addCase(caseToAdd);
    }

    @Test
    void shouldReturnInternalServerErrorDuringAddingCaseWhenSomethingWentWrongOnServer() throws Exception {
        AbsenceCase aCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(caseService.caseExists(aCase.getId())).thenReturn(false);
        when(caseService.addCase(aCase)).thenThrow(new ServiceOperationException());

        String url = "/api/cases";

        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(aCase))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(caseService).caseExists(aCase.getId());
        verify(caseService).addCase(aCase);
    }

    @Test
    void shouldUpdateCase() throws Exception {

        AppModelMapper unMockedAppModelMapper = new AppModelMapper(repository);
        AbsenceCase aCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(caseService.caseExists(aCase.getId())).thenReturn(true);
        when(caseService.updateCase(aCase)).thenReturn(aCase);
        User headTeacher = aCase.getHeadTeacher();
        User user = aCase.getUser();
        when(userService.getUserByEmail(aCase.getUser().getEmail())).thenReturn(Optional.of(user));
        when(userService.getUserByEmail(aCase.getHeadTeacher().getEmail())).thenReturn(Optional.of(headTeacher));
        when(userService.getUserById(any())).thenReturn(Optional.of(headTeacher));
        when(appModelMapper.mapToAbsenceCaseDTO(aCase)).thenReturn(unMockedAppModelMapper.mapToAbsenceCaseDTO(aCase));
        doNothing().when(emailService).sendEmailToHumanResourcesSupervisor(any(), any(), any());

        String url = String.format("/api/cases?id=%d&userId=%d", aCase.getId(), headTeacher.getId());

        mockMvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(aCase))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(appModelMapper.mapToAbsenceCaseDTO(aCase))));

        verify(caseService).caseExists(aCase.getId());
        verify(caseService).updateCase(aCase);
        verify(emailService).sendEmailToHumanResourcesSupervisor(any(), any(), any());
    }

    @Test
    void shouldReturnBadRequestStatusDuringUpdatingNullAsCase() throws Exception {
        String url = "/api/cases?id=1&userId=1";

        mockMvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(null))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(caseService, never()).caseExists(any());
        verify(caseService, never()).updateCase(any());
    }

    @Test
    void shouldReturnBadRequestStatusDuringUpdatingCaseWhenPassedIdIsDifferentThanCaseId() throws Exception {
        AbsenceCase aCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();

        String url = String.format("/api/cases?id=%d&userId=%d", Long.valueOf(aCase.getId() + "1"), 1L);

        mockMvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(aCase))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(caseService, never()).caseExists(any());
        verify(caseService, never()).updateCase(any());
    }

    @Test
    void shouldReturnNotFoundStatusDuringUpdatingCaseWhenCaseDoesNotExistInDatabase() throws Exception {
        AbsenceCase aCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(caseService.caseExists(aCase.getId())).thenReturn(false);

        String url = String.format("/api/cases?id=%d&userId=%d", aCase.getId(), 1L);

        mockMvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(aCase))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(caseService).caseExists(aCase.getId());
        verify(caseService, never()).updateCase(aCase);
    }

    @Test
    void updateMethodShouldReturnBadRequestForInvalidCase() throws Exception {
        AbsenceCase invalidCase = AbsenceCase.builder().withId(1L).withUser(null).build();
        when(caseService.caseExists(invalidCase.getId())).thenReturn(true);

        String url = String.format("/api/cases?id=%d&userId=%d", invalidCase.getId(), 1L);

        mockMvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(invalidCase))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(caseService).caseExists(invalidCase.getId());
        verify(caseService, never()).updateCase(invalidCase);
    }

    @Test
    void shouldReturnUnsupportedMediaTypeStatusDuringUpdatingCaseWithNotSupportedMediaType() throws Exception {
        AbsenceCase aCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        String url = String.format("/api/cases?id=%d&userId=%d", aCase.getId(), 1L);

        mockMvc.perform(put(url)
            .contentType(MediaType.APPLICATION_XML)
            .content(mapper.writeValueAsBytes(aCase))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnsupportedMediaType());

        verify(caseService, never()).updateCase(aCase);
    }

    @Test
    void shouldReturnInternalServerErrorDuringUpdatingCaseWhenSomethingWentWrongOnServer() throws Exception {
        AbsenceCase aCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        User headTeacher = aCase.getHeadTeacher();
        User user = aCase.getUser();
        when(caseService.caseExists(aCase.getId())).thenReturn(true);
        when(userService.getUserByEmail(aCase.getUser().getEmail())).thenReturn(Optional.of(user));
        when(userService.getUserByEmail(aCase.getHeadTeacher().getEmail())).thenReturn(Optional.of(headTeacher));
        when(caseService.updateCase(aCase)).thenThrow(new ServiceOperationException());

        String url = String.format("/api/cases?id=%d&userId=%d", aCase.getId(), 1L);

        mockMvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(aCase))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(caseService).caseExists(aCase.getId());
        verify(caseService).updateCase(aCase);
    }

    @Test
    void shouldReturnCase() throws Exception {
        AbsenceCase aCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(caseService.getCaseById(aCase.getId())).thenReturn(Optional.of(aCase));

        String url = String.format("/api/cases/%d", aCase.getId());

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(aCase)));

        verify(caseService).getCaseById(aCase.getId());
    }

    @Test
    void shouldReturnNotFoundStatusDuringGettingCaseWhenCaseWithSpecificIdDoesNotExist() throws Exception {
        Long id = 1L;
        when(caseService.getCaseById(id)).thenReturn(Optional.empty());

        String url = String.format("/api/cases/%d", id);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(caseService).getCaseById(id);
    }

    @Test
    void shouldReturnCaseAsJsonIfIsPriorToOtherAcceptedHeaders() throws Exception {
        AbsenceCase aCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(caseService.getCaseById(aCase.getId())).thenReturn(Optional.of(aCase));

        String url = String.format("/api/cases/%d", aCase.getId());

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_PDF))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(aCase)));

        verify(caseService).getCaseById(aCase.getId());
    }

    @Test
    void shouldReturnInternalServerErrorDuringGettingCaseWhenSomethingWentWrongOnServer() throws Exception {
        Long id = 1L;
        when(caseService.getCaseById(id)).thenThrow(new ServiceOperationException());

        String url = String.format("/api/cases/%d", id);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(caseService).getCaseById(id);
    }

    @Test
    void shouldReturnAllCases() throws Exception {
        List<AbsenceCase> cases = Arrays.asList(AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType(),
            AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType());
        when(caseService.getAllCases()).thenReturn(cases);

        String url = "/api/cases";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(appModelMapper.mapToAbsenceCaseDTOList(cases))));

        verify(caseService).getAllCases();
    }

    @Test
    void shouldReturnEmptyListOfCasesWhenThereAreNotCasesInTheDatabase() throws Exception {
        List<AbsenceCase> cases = new ArrayList<>();
        when(caseService.getAllCases()).thenReturn(cases);

        String url = "/api/cases";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(appModelMapper.mapToAbsenceCaseDTOList(cases))));

        verify(caseService).getAllCases();
    }

    @Test
    void shouldReturnNotAcceptableStatusDuringGettingAllCasesWithNotSupportedMediaType() throws Exception {
        String url = "/api/cases";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_XML))
            .andExpect(status().isNotAcceptable());

        verify(caseService, never()).getAllCases();
    }

    @Test
    void shouldReturnInternalServerErrorDuringGettingAllCasesWhenSomethingWentWrongOnServer() throws Exception {
        when(caseService.getAllCases()).thenThrow(new ServiceOperationException());

        String url = "/api/cases";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(caseService).getAllCases();
    }

    @Test
    void shouldReturnAllActiveCases() throws Exception {
        List<AbsenceCase> cases = Arrays.asList(AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType(),
            AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType());
        when(caseService.getAllActiveCases()).thenReturn(cases);

        String url = "/api/cases/active";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(appModelMapper.mapToAbsenceCaseDTOList(cases))));

        verify(caseService).getAllActiveCases();
    }

    @Test
    void shouldReturnEmptyListOfActiveCasesWhenThereAreNotActiveCasesInTheDatabase() throws Exception {
        List<AbsenceCase> cases = new ArrayList<>();
        when(caseService.getAllActiveCases()).thenReturn(cases);

        String url = "/api/cases/active";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(appModelMapper.mapToAbsenceCaseDTOList(cases))));

        verify(caseService).getAllActiveCases();
    }

    @Test
    void shouldReturnNotAcceptableStatusDuringGettingAllActiveCasesWithNotSupportedMediaType() throws Exception {
        String url = "/api/cases/active";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_XML))
            .andExpect(status().isNotAcceptable());

        verify(caseService, never()).getAllActiveCases();
    }

    @Test
    void shouldReturnInternalServerErrorDuringGettingAllActiveCasesWhenSomethingWentWrongOnServer() throws Exception {
        when(caseService.getAllActiveCases()).thenThrow(new ServiceOperationException());

        String url = "/api/cases/active";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(caseService).getAllActiveCases();
    }

    @Test
    void shouldReturnAllUserCases() throws Exception {
        List<AbsenceCase> cases = Arrays.asList(AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificUserId(1L),
            AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificUserId(1L));
        when(caseService.getAllUserCases(1L)).thenReturn(cases);

        String url = String.format("/api/cases/user/%d", 1L);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(appModelMapper.mapToAbsenceCaseDTOList(cases))));

        verify(caseService).getAllUserCases(1L);
    }

    @Test
    void shouldReturnEmptyListOfUserCasesWhenThereAreNotUserCasesInTheDatabase() throws Exception {
        Long id = 1L;
        List<AbsenceCase> cases = new ArrayList<>();
        when(caseService.getAllUserCases(id)).thenReturn(cases);

        String url = String.format("/api/cases/user/%d", id);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(appModelMapper.mapToAbsenceCaseDTOList(cases))));

        verify(caseService).getAllUserCases(id);
    }

    @Test
    void shouldReturnNotAcceptableStatusDuringGettingAllUserCasesWithNotSupportedMediaType() throws Exception {
        Long id = 1L;
        String url = String.format("/api/cases/user/%d", id);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_XML))
            .andExpect(status().isNotAcceptable());

        verify(caseService, never()).getAllUserCases(id);
    }

    @Test
    void shouldReturnInternalServerErrorDuringGettingAllUserCasesWhenSomethingWentWrongOnServer() throws Exception {
        Long id = 1L;
        when(caseService.getAllUserCases(id)).thenThrow(new ServiceOperationException());

        String url = String.format("/api/cases/user/%d", id);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(caseService).getAllUserCases(id);
    }

    @Test
    void shouldReturnAllActiveCasesForHeadTeacher() throws Exception {
        List<AbsenceCase> cases = Arrays.asList(AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificHeadTeacherId(1L),
            AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificHeadTeacherId(1L));
        when(caseService.getAllActiveCasesForHeadTeacher(1L)).thenReturn(cases);

        String url = String.format("/api/cases/active/ht/%d", 1L);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(appModelMapper.mapToAbsenceCaseDTOList(cases))));

        verify(caseService).getAllActiveCasesForHeadTeacher(1L);
    }

    @Test
    void shouldReturnEmptyListOfAllActiveCasesForHeadTeacherWhenThereAreNotActiveCasesForHeadTeacherInTheDatabase() throws Exception {
        Long id = 1L;
        List<AbsenceCase> cases = new ArrayList<>();
        when(caseService.getAllActiveCasesForHeadTeacher(id)).thenReturn(cases);

        String url = String.format("/api/cases/active/ht/%d", id);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(appModelMapper.mapToAbsenceCaseDTOList(cases))));

        verify(caseService).getAllActiveCasesForHeadTeacher(id);
    }

    @Test
    void shouldReturnNotAcceptableStatusDuringGettingAllActiveCasesForHeadTeacherWithNotSupportedMediaType() throws Exception {
        Long id = 1L;
        String url = String.format("/api/cases/active/ht/%d", id);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_XML))
            .andExpect(status().isNotAcceptable());

        verify(caseService, never()).getAllActiveCasesForHeadTeacher(id);
    }

    @Test
    void shouldReturnInternalServerErrorDuringGettingAllActiveCasesForHeadTeacherWhenSomethingWentWrongOnServer() throws Exception {
        Long id = 1L;
        when(caseService.getAllActiveCasesForHeadTeacher(id)).thenThrow(new ServiceOperationException());

        String url = String.format("/api/cases/active/ht/%d", id);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(caseService).getAllActiveCasesForHeadTeacher(id);
    }

    @Test
    void shouldRemoveCase() throws Exception {
        AbsenceCase aCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(caseService.caseExists(aCase.getId())).thenReturn(true);
        doNothing().when(userService).deleteUser(aCase.getId());

        String url = String.format("/api/cases/%d", aCase.getId());

        mockMvc.perform(delete(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(caseService).caseExists(aCase.getId());
        verify(caseService).deleteCase(aCase.getId());
    }

    @Test
    void shouldReturnNotFoundStatusDuringRemovingCaseWhenCaseDoesNotExistInDatabase() throws Exception {
        when(caseService.caseExists(1L)).thenReturn(false);

        String url = "/api/cases/1";

        mockMvc.perform(delete(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(caseService).caseExists(1L);
        verify(caseService, never()).deleteCase(1L);
    }

    @Test
    void shouldReturnInternalServerErrorDuringRemovingCaseWhenSomethingWentWrongOnServer() throws Exception {
        Long id = 1L;
        when(caseService.caseExists(id)).thenReturn(true);
        doThrow(ServiceOperationException.class).when(caseService).deleteCase(id);

        String url = String.format("/api/cases/%d", id);

        mockMvc.perform(delete(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(caseService).caseExists(id);
        verify(caseService).deleteCase(id);
    }
}
