package io.github.rdx7777.absencemanagementsystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.github.rdx7777.absencemanagementsystem.generators.UserGenerator;
import io.github.rdx7777.absencemanagementsystem.model.User;
import io.github.rdx7777.absencemanagementsystem.repository.UserRepository;

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
class UserServiceTest {

    @Mock
    UserRepository repository;

    @InjectMocks
    UserService userService;

    @Test
    void shouldAddUser() throws ServiceOperationException {
        // given
        User userToAdd = UserGenerator.getRandomEmployee();
        User addedUser = UserGenerator.getRandomEmployee();
        when(repository.existsById(userToAdd.getId())).thenReturn(false);
        when(repository.save(userToAdd)).thenReturn(addedUser);

        // when
        User result = userService.addUser(userToAdd);

        // then
        assertEquals(addedUser, result);
        verify(repository).existsById(userToAdd.getId());
        verify(repository).save(userToAdd);
    }

    @Test
    void shouldAddUserWithNullId() throws ServiceOperationException {
        // given
        User userToAdd = UserGenerator.getRandomEmployeeWithNullId();
        User addedUser = UserGenerator.getRandomEmployee();
        when(repository.save(userToAdd)).thenReturn(addedUser);

        // when
        User result = userService.addUser(userToAdd);

        // then
        assertEquals(addedUser, result);
        verify(repository, never()).existsById(userToAdd.getId());
        verify(repository).save(userToAdd);
    }

    @Test
    void addUserMethodShouldThrowIllegalArgumentExceptionForNullUser() {
        assertThrows(IllegalArgumentException.class, () -> userService.addUser(null));
        verify(repository, never()).existsById(any());
        verify(repository, never()).save(any());
    }

    @Test
    void addUserMethodShouldThrowExceptionForUserExistingInDatabase() {
        // given
        User user = UserGenerator.getRandomEmployee();
        when(repository.existsById(user.getId())).thenReturn(true);

        // then
        assertThrows(ServiceOperationException.class, () -> userService.addUser(user));
        verify(repository).existsById(user.getId());
        verify(repository, never()).save(user);
    }

    @Test
    void shouldUpdateGivenUserInDatabase() throws ServiceOperationException {
        // given
        User userToUpdate = UserGenerator.getRandomEmployee();
        User updatedUser = UserGenerator.getRandomEmployee();
        when(repository.existsById(userToUpdate.getId())).thenReturn(true);
        when(repository.save(userToUpdate)).thenReturn(updatedUser);

        // when
        User result = userService.updateUser(userToUpdate);

        // then
        assertEquals(updatedUser, result);
        verify(repository).existsById(userToUpdate.getId());
        verify(repository).save(userToUpdate);
    }

    @Test
    void updateUserMethodShouldThrowIllegalArgumentExceptionForNullUser() {
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(null));
        verify(repository, never()).existsById(any());
        verify(repository, never()).save(any());
    }

    @Test
    void updateUserMethodShouldThrowExceptionForNullUserId() {
        // given
        User user = UserGenerator.getRandomEmployeeWithNullId();

        // then
        assertThrows(ServiceOperationException.class, () -> userService.updateUser(user));
        verify(repository, never()).existsById(any());
        verify(repository, never()).save(any());
    }

    @Test
    void updateUserMethodShouldThrowExceptionWhenUserDoesNotExistInDatabase() {
        // given
        User user = UserGenerator.getRandomEmployee();
        when(repository.existsById(user.getId())).thenReturn(false);

        // then
        assertThrows(ServiceOperationException.class, () -> userService.updateUser(user));
        verify(repository).existsById(user.getId());
        verify(repository, never()).save(user);
    }

    @Test
    void shouldReturnUserByGivenId() {
        // given
        User user = UserGenerator.getRandomEmployee();
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        // when
        Optional<User> result = userService.getUserById(1L);

        // then
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(repository).findById(1L);
    }

    @Test
    void getUserByIdMethodShouldThrowIllegalArgumentExceptionForNullUserId() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(null));
        verify(repository, never()).findById(1L);
    }

    @Test
    void shouldReturnAllUsers() {
        // given
        List<User> users = List.of(UserGenerator.getRandomEmployee(), UserGenerator.getRandomEmployee());
        when(repository.findAll()).thenReturn(users);

        // when
        Collection<User> result = userService.getAllUsers();

        // then
        assertEquals(users, result);
        verify(repository).findAll();
    }

    @Test
    void shouldReturnAllActiveUsers() {
        // given
        List<User> users = List.of(UserGenerator.getRandomEmployee(), UserGenerator.getRandomEmployee());
        Example example = Example.of(new User.Builder().withIsActive(true).build());
        when(repository.findAll(example)).thenReturn(users);

        // when
        Collection<User> result = userService.getAllActiveUsers();

        // then
        assertEquals(users, result);
        verify(repository).findAll(example);
    }

    @Test
    void shouldReturnTrueWhenUserExistsInDatabase() {
        // given
        when(repository.existsById(1L)).thenReturn(true);

        // when
        boolean result = userService.userExists(1L);

        // then
        assertTrue(result);
        verify(repository).existsById(1L);
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotExistInDatabase() {
        // given
        when(repository.existsById(1L)).thenReturn(false);

        // when
        boolean result = userService.userExists(1L);

        // then
        assertFalse(result);
        verify(repository).existsById(1L);
    }

    @Test
    void userExistsMethodShouldThrowIllegalArgumentExceptionForNullUserId() {
        assertThrows(IllegalArgumentException.class, () -> userService.userExists(null));
    }

    @Test
    void shouldReturnNumberOfUsers() {
        // given
        when(repository.count()).thenReturn(10L);

        // when
        long result = userService.usersCount();

        // then
        assertEquals(10L, result);
        verify(repository).count();
    }
}
