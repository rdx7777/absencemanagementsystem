package io.github.rdx7777.absencemanagementsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.rdx7777.absencemanagementsystem.generators.UserGenerator;
import io.github.rdx7777.absencemanagementsystem.model.User;
import io.github.rdx7777.absencemanagementsystem.service.UserService;

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
@WebMvcTest(SecurityController.class)
@WithMockUser(roles = "ADMIN")

class SecurityControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldReturnLoggedUserId() throws Exception {
        User loggedUser = UserGenerator.getRandomEmployee();
        Long loggedUserId = loggedUser.getId();
        when(userService.getUserByEmail(any())).thenReturn(Optional.of(loggedUser));

        String url = "/";

        mockMvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(loggedUserId)));

        verify(userService).getUserByEmail(any());
    }

    @Test
    void shouldReturnNotFoundStatusDuringGettingUserIdWhenUserWithSpecificEmailDoesNotExist() throws Exception {
        when(userService.getUserByEmail(any())).thenReturn(Optional.empty());

        String url = "/";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(userService).getUserByEmail(any());
    }
}