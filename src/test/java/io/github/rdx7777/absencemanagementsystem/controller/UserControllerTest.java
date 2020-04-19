package io.github.rdx7777.absencemanagementsystem.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.rdx7777.absencemanagementsystem.generators.UserGenerator;
import io.github.rdx7777.absencemanagementsystem.model.AppModelMapper;
import io.github.rdx7777.absencemanagementsystem.model.User;
import io.github.rdx7777.absencemanagementsystem.repository.UserRepository;
import io.github.rdx7777.absencemanagementsystem.security.jwt.AuthEntryPointJwt;
import io.github.rdx7777.absencemanagementsystem.security.jwt.JwtUtils;
import io.github.rdx7777.absencemanagementsystem.security.services.UserDetailsServiceImpl;
import io.github.rdx7777.absencemanagementsystem.service.ServiceOperationException;
import io.github.rdx7777.absencemanagementsystem.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
@WebMvcTest(UserController.class)
@WithMockUser(roles = "ADMIN")
class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private AppModelMapper appModelMapper;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldAddUser() throws Exception {
        AppModelMapper unMockedAppModelMapper = new AppModelMapper(repository);
        User userToAdd = UserGenerator.getRandomEmployee();
        User addedUser = UserGenerator.getRandomEmployee();
        when(userService.userExists(userToAdd.getId())).thenReturn(false);
        when(userService.addUser(userToAdd)).thenReturn(addedUser);
        when(appModelMapper.mapToUserDTO(addedUser)).thenReturn(unMockedAppModelMapper.mapToUserDTO(addedUser));

        String url = "/api/users";

        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(userToAdd))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(appModelMapper.mapToUserDTO(addedUser))));

        verify(userService).userExists(userToAdd.getId());
        verify(userService).addUser(userToAdd);
    }

    @Test
    void shouldReturnBadRequestStatusDuringAddingNullAsUser() throws Exception {
        String url = "/api/users";

        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(null))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(userService, never()).userExists(any());
        verify(userService, never()).addUser(any());
    }

    @Test
    void shouldReturnConflictStatusDuringAddingUserWhenUserExistsInDatabase() throws Exception {
        User user = UserGenerator.getRandomEmployee();
        when(userService.userExists(user.getId())).thenReturn(true);

        String url = "/api/users";

        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(user))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());

        verify(userService).userExists(user.getId());
        verify(userService, never()).addUser(user);
    }

    @Test
    void addMethodShouldReturnBadRequestForInvalidUser() throws Exception {
        User invalidUser = User.builder().withId(1L).withEmail("aaa&aaa.com").build();
        when(userService.userExists(invalidUser.getId())).thenReturn(false);

        String url = "/api/users";

        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(invalidUser))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(userService).userExists(invalidUser.getId());
        verify(userService, never()).addUser(invalidUser);
    }

    @Test
    void shouldReturnUnsupportedMediaTypeStatusDuringAddingUserWithNotSupportedMediaType() throws Exception {
        User userToAdd = UserGenerator.getRandomEmployee();
        String url = "/api/users";

        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_XML)
            .content(mapper.writeValueAsBytes(userToAdd))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnsupportedMediaType());

        verify(userService, never()).addUser(userToAdd);
    }

    @Test
    void shouldReturnInternalServerErrorDuringAddingUserWhenSomethingWentWrongOnServer() throws Exception {
        User user = UserGenerator.getRandomEmployee();
        when(userService.userExists(user.getId())).thenReturn(false);
        when(userService.addUser(user)).thenThrow(new ServiceOperationException());

        String url = "/api/users";

        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(user))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(userService).userExists(user.getId());
        verify(userService).addUser(user);
    }

    @Test
    void shouldUpdateUser() throws Exception {
        AppModelMapper unMockedAppModelMapper = new AppModelMapper(repository);
        User user = UserGenerator.getRandomEmployee();
        when(userService.userExists(user.getId())).thenReturn(true);
        when(userService.updateUser(user)).thenReturn(user);
        when(appModelMapper.mapToUserDTO(user)).thenReturn(unMockedAppModelMapper.mapToUserDTO(user));

        String url = String.format("/api/users/%d", user.getId());

        mockMvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(user))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(appModelMapper.mapToUserDTO(user))));

        verify(userService).userExists(user.getId());
        verify(userService).updateUser(user);
    }

    @Test
    void shouldReturnBadRequestStatusDuringUpdatingNullAsUser() throws Exception {
        String url = "/api/users/1";

        mockMvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(null))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(userService, never()).userExists(any());
        verify(userService, never()).updateUser(any());
    }

    @Test
    void shouldReturnBadRequestStatusDuringUpdatingUserWhenPassedIdIsDifferentThanUserId() throws Exception {
        User user = UserGenerator.getRandomEmployee();

        String url = String.format("/api/users/%d", Long.valueOf(user.getId() + "1"));

        mockMvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(user))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(userService, never()).userExists(any());
        verify(userService, never()).updateUser(any());
    }

    @Test
    void shouldReturnNotFoundStatusDuringUpdatingUserWhenUserDoesNotExistInDatabase() throws Exception {
        User user = UserGenerator.getRandomEmployee();
        when(userService.userExists(user.getId())).thenReturn(false);

        String url = String.format("/api/users/%d", user.getId());

        mockMvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(user))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(userService).userExists(user.getId());
        verify(userService, never()).updateUser(user);
    }

    @Test
    void updateMethodShouldReturnBadRequestForInvalidUser() throws Exception {
        User invalidUser = User.builder().withId(1L).withEmail("aaa&aaa.com").build();
        when(userService.userExists(invalidUser.getId())).thenReturn(true);

        String url = String.format("/api/users/%d", invalidUser.getId());

        mockMvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(invalidUser))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(userService).userExists(invalidUser.getId());
        verify(userService, never()).updateUser(invalidUser);
    }

    @Test
    void shouldReturnUnsupportedMediaTypeStatusDuringUpdatingUserWithNotSupportedMediaType() throws Exception {
        User user = UserGenerator.getRandomEmployee();
        String url = String.format("/api/users/%d", user.getId());

        mockMvc.perform(put(url)
            .contentType(MediaType.APPLICATION_XML)
            .content(mapper.writeValueAsBytes(user))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnsupportedMediaType());

        verify(userService, never()).updateUser(user);
    }

    @Test
    void shouldReturnInternalServerErrorDuringUpdatingUserWhenSomethingWentWrongOnServer() throws Exception {
        User user = UserGenerator.getRandomEmployee();
        when(userService.userExists(user.getId())).thenReturn(true);
        when(userService.updateUser(user)).thenThrow(new ServiceOperationException());

        String url = String.format("/api/users/%d", user.getId());

        mockMvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(user))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(userService).userExists(user.getId());
        verify(userService).updateUser(user);
    }

    @Test
    void shouldReturnUser() throws Exception {
        User user = UserGenerator.getRandomEmployee();
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));

        String url = String.format("/api/users/%d", user.getId());

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(user)));

        verify(userService).getUserById(user.getId());
    }

    @Test
    void shouldReturnNotFoundStatusDuringGettingUserWhenUserWithSpecificIdDoesNotExist() throws Exception {
        Long id = 1L;
        when(userService.getUserById(id)).thenReturn(Optional.empty());

        String url = String.format("/api/users/%d", id);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(userService).getUserById(id);
    }

    @Test
    void shouldReturnUserAsJsonIfIsPriorToOtherAcceptedHeaders() throws Exception {
        User user = UserGenerator.getRandomEmployee();
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));

        String url = String.format("/api/users/%d", user.getId());

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_PDF))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(user)));

        verify(userService).getUserById(user.getId());
    }

    @Test
    void shouldReturnNotAcceptableStatusDuringGettingUserWithNotSupportedMediaType() throws Exception {
        User user = UserGenerator.getRandomEmployee();

        String url = String.format("/api/users/%d", user.getId());

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_XML))
            .andExpect(status().isNotAcceptable());

        verify(userService, never()).getUserById(user.getId());
    }

    @Test
    void shouldReturnInternalServerErrorDuringGettingUserWhenSomethingWentWrongOnServer() throws Exception {
        Long id = 1L;
        when(userService.getUserById(id)).thenThrow(new ServiceOperationException());

        String url = String.format("/api/users/%d", id);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(userService).getUserById(id);
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        List<User> users = Arrays.asList(UserGenerator.getRandomEmployee(), UserGenerator.getRandomEmployee());
        when(userService.getAllUsers()).thenReturn(users);

        String url = "/api/users";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(appModelMapper.mapToUserDTOList(users))));

        verify(userService).getAllUsers();
    }

    @Test
    void shouldReturnEmptyListOfUsersWhenThereAreNotUsersInTheDatabase() throws Exception {
        Collection<User> users = new ArrayList<>();
        when(userService.getAllUsers()).thenReturn(users);

        String url = "/api/users";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(users)));

        verify(userService).getAllUsers();
    }

    @Test
    void shouldReturnNotAcceptableStatusDuringGettingAllUsersWithNotSupportedMediaType() throws Exception {
        String url = "/api/users";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_XML))
            .andExpect(status().isNotAcceptable());

        verify(userService, never()).getAllUsers();
    }

    @Test
    void shouldReturnInternalServerErrorDuringGettingAllUsersWhenSomethingWentWrongOnServer() throws Exception {
        when(userService.getAllUsers()).thenThrow(new ServiceOperationException());

        String url = "/api/users";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(userService).getAllUsers();
    }

    @Test
    void shouldReturnAllActiveUsers() throws Exception {
        List<User> users = Arrays.asList(UserGenerator.getRandomEmployee(), UserGenerator.getRandomEmployee());
        when(userService.getAllActiveUsers()).thenReturn(users);

        String url = "/api/users/active";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(appModelMapper.mapToUserDTOList(users))));

        verify(userService).getAllActiveUsers();
    }

    @Test
    void shouldReturnEmptyListOfActiveUsersWhenThereAreNotActiveUsersInTheDatabase() throws Exception {
        Collection<User> users = new ArrayList<>();
        when(userService.getAllActiveUsers()).thenReturn(users);

        String url = "/api/users/active";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(users)));

        verify(userService).getAllActiveUsers();
    }

    @Test
    void shouldReturnNotAcceptableStatusDuringGettingAllActiveUsersWithNotSupportedMediaType() throws Exception {
        String url = "/api/users/active";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_XML))
            .andExpect(status().isNotAcceptable());

        verify(userService, never()).getAllActiveUsers();
    }

    @Test
    void shouldReturnInternalServerErrorDuringGettingAllActiveUsersWhenSomethingWentWrongOnServer() throws Exception {
        when(userService.getAllActiveUsers()).thenThrow(new ServiceOperationException());

        String url = "/api/users/active";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(userService).getAllActiveUsers();
    }

    @Test
    void shouldRemoveUser() throws Exception {
        User user = UserGenerator.getRandomEmployee();
        when(userService.userExists(user.getId())).thenReturn(true);
        doNothing().when(userService).deleteUser(user.getId());

        String url = String.format("/api/users/%d", user.getId());

        mockMvc.perform(delete(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(userService).userExists(user.getId());
        verify(userService).deleteUser(user.getId());
    }

    @Test
    void shouldReturnNotFoundStatusDuringRemovingUserWhenUserDoesNotExistInDatabase() throws Exception {
        when(userService.userExists(1L)).thenReturn(false);

        String url = "/api/users/1";

        mockMvc.perform(delete(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(userService).userExists(1L);
        verify(userService, never()).deleteUser(1L);
    }

    @Test
    void shouldReturnInternalServerErrorDuringRemovingUserWhenSomethingWentWrongOnServer() throws Exception {
        Long id = 1L;
        when(userService.userExists(id)).thenReturn(true);
        doThrow(ServiceOperationException.class).when(userService).deleteUser(id);

        String url = String.format("/api/users/%d", id);

        mockMvc.perform(delete(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(userService).userExists(id);
        verify(userService).deleteUser(id);
    }
}
