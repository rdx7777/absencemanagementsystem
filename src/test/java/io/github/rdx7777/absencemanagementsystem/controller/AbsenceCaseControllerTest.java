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
import io.github.rdx7777.absencemanagementsystem.model.User;
import io.github.rdx7777.absencemanagementsystem.service.AbsenceCaseService;
import io.github.rdx7777.absencemanagementsystem.service.EmailService;
import io.github.rdx7777.absencemanagementsystem.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AbsenceCaseController.class)
class AbsenceCaseControllerTest {

    @MockBean
    private AbsenceCaseService caseService;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailService emailService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldAddCase() throws Exception {
        AbsenceCase caseToAdd = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        AbsenceCase addedCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(caseService.caseExists(caseToAdd.getId())).thenReturn(false);
        when(caseService.addCase(caseToAdd)).thenReturn(addedCase);
        User headTeacher = UserGenerator.getRandomHeadTeacher();
        User user = UserGenerator.getRandomEmployee();
        when(userService.getUserById(caseToAdd.getHeadTeacherId())).thenReturn(Optional.of(headTeacher));
        when(userService.getUserById(caseToAdd.getUserId())).thenReturn(Optional.of(user));
        doNothing().when(emailService).sendEmailToCoverSupervisor(headTeacher, user, addedCase);

        String url = "/api/cases";

        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(caseToAdd))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(addedCase)));

        verify(caseService).caseExists(caseToAdd.getId());
        verify(caseService).addCase(caseToAdd);
        verify(emailService, never()).sendEmailToCoverSupervisor(headTeacher, user, addedCase);
    }

    @Test
    void shouldReturnBadRequestStatusDuringAddingNullAsCase() throws Exception {
        String url = "/api/cases";

        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(null))
            .accept(MediaType.APPLICATION_JSON))
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
            .andExpect(status().isConflict());

        verify(caseService).caseExists(aCase.getId());
        verify(caseService, never()).addCase(aCase);
    }

    @Test
    void addMethodShouldReturnBadRequestForInvalidCase() throws Exception {
        AbsenceCase invalidCase = AbsenceCase.builder().withId(1L).withUserId(null).build();
        when(caseService.caseExists(invalidCase.getId())).thenReturn(false);

        String url = "/api/cases";

        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(invalidCase)).accept(MediaType.APPLICATION_JSON))
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
    void shouldUpdateCase() throws Exception {
        AbsenceCase aCase = AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType();
        when(caseService.caseExists(aCase.getId())).thenReturn(true);
        when(caseService.updateCase(aCase)).thenReturn(aCase);
        User headTeacher = UserGenerator.getRandomHeadTeacher();
        User user = UserGenerator.getRandomEmployee();
        when(userService.getUserById(aCase.getHeadTeacherId())).thenReturn(Optional.of(headTeacher));
        when(userService.getUserById(aCase.getUserId())).thenReturn(Optional.of(user));
        when(userService.getUserById(any())).thenReturn(Optional.of(headTeacher));
        doNothing().when(emailService).sendEmailToHumanResourcesSupervisor(headTeacher, user, aCase);

        String url = String.format("/api/cases?id=%d&userId=%d", aCase.getId(), headTeacher.getId());

        mockMvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(aCase))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(aCase)));

        verify(caseService).caseExists(aCase.getId());
        verify(caseService).updateCase(aCase);
        verify(emailService, never()).sendEmailToHumanResourcesSupervisor(headTeacher, user, aCase);
    }

    @Test
    void shouldReturnBadRequestStatusDuringUpdatingNullAsCase() throws Exception {
        String url = "/api/cases?id=1&userId=1";

        mockMvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(null))
            .accept(MediaType.APPLICATION_JSON))
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
            .andExpect(status().isNotFound());

        verify(caseService).caseExists(aCase.getId());
        verify(caseService, never()).updateCase(aCase);
    }

//    @Test
//    void shouldReturnBadRequestStatusDuringUpdatingCaseByNotExistingUser() {
//
//    }

    @Test
    void updateMethodShouldReturnBadRequestForInvalidCase() throws Exception {
        AbsenceCase invalidCase = AbsenceCase.builder().withId(1L).withUserId(null).build();
        when(caseService.caseExists(invalidCase.getId())).thenReturn(true);

        String url = String.format("/api/cases?id=%d&userId=%d", invalidCase.getId(), 1L);

        mockMvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(invalidCase))
            .accept(MediaType.APPLICATION_JSON))
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
    void shouldReturnAllCases() throws Exception {
        Collection<AbsenceCase> cases = Arrays.asList(AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType(),
            AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType());
        when(caseService.getAllCases()).thenReturn(cases);

        String url = "/api/cases";

        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(cases)));

        verify(caseService).getAllCases();
    }

    @Test
    void shouldReturnEmptyListOfCasesWhenThereAreNotCasesInTheDatabase() throws Exception {
        Collection<AbsenceCase> cases = new ArrayList<>();
        when(caseService.getAllCases()).thenReturn(cases);

        String url = "/api/cases";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(cases)));

        verify(caseService).getAllCases();
    }

    @Test
    void shouldReturnNotAcceptableStatusDuringGettingAllCasesWithNotSupportedMediaType() throws Exception {
        String url = "/api/cases";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_XML))
            .andExpect(status().isNotAcceptable());

        verify(caseService).getAllCases(); // ???????????????
    }

    @Test
    void shouldReturnAllActiveCases() throws Exception {
        Collection<AbsenceCase> cases = Arrays.asList(AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType(),
            AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayType());
        when(caseService.getAllActiveCases()).thenReturn(cases);

        String url = "/api/cases/active";

        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(cases)));

        verify(caseService).getAllActiveCases();
    }

    @Test
    void shouldReturnAllUserCases() throws Exception {
        Collection<AbsenceCase> cases = Arrays.asList(AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificUserId(1L),
            AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificUserId(1L));
        when(caseService.getAllUserCases(1L)).thenReturn(cases);

        String url = String.format("/api/cases/user/%d", 1L);

        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(cases)));

        verify(caseService).getAllUserCases(1L);
    }

    @Test
    void shouldReturnAllActiveCasesForHeadTeacher() throws Exception {
        Collection<AbsenceCase> cases = Arrays.asList(AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificHeadTeacherId(1L),
            AbsenceCaseGenerator.getRandomCaseWithAllDayPartDayTypeAndSpecificHeadTeacherId(1L));
        when(caseService.getAllActiveCasesForHeadTeacher(1L)).thenReturn(cases);

        String url = String.format("/api/cases/active/ht/%d", 1L);

        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(cases)));

        verify(caseService).getAllActiveCasesForHeadTeacher(1L);
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
}
