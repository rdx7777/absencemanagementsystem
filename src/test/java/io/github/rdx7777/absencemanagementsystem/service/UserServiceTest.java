package io.github.rdx7777.absencemanagementsystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.github.rdx7777.absencemanagementsystem.generators.UserGenerator;
import io.github.rdx7777.absencemanagementsystem.model.User;
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
    void addUserMethodShouldThrowExceptionWhenAnErrorOccursDuringAddingUserToDatabase() {
        // given
        User user = UserGenerator.getRandomEmployee();
        when(repository.existsById(user.getId())).thenReturn(false);
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).save(user);

        // then
        assertThrows(ServiceOperationException.class, () -> userService.addUser(user));
        verify(repository).existsById(user.getId());
        verify(repository).save(user);
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
    void updateUserMethodShouldThrowExceptionWhenAnErrorOccursDuringUpdatingUserInDatabase() {
        // given
        User user = UserGenerator.getRandomEmployee();
        when(repository.existsById(user.getId())).thenReturn(true);
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).save(user);

        // then
        assertThrows(ServiceOperationException.class, () -> userService.updateUser(user));
        verify(repository).existsById(user.getId());
        verify(repository).save(user);
    }

    @Test
    void shouldReturnUserByGivenId() throws ServiceOperationException {
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
    void getUserByIdMethodShouldThrowExceptionWhenAnErrorOccursDuringGettingUserById() {
        // given
        doThrow(new NoSuchElementException()).when(repository).findById(1L);

        // then
        assertThrows(ServiceOperationException.class, () -> userService.getUserById(1L));
        verify(repository).findById(1L);
    }

    @Test
    void shouldReturnUserByGivenEmail() throws ServiceOperationException {
        // given
        User user = UserGenerator.getRandomEmployee();
        when(repository.findUserByEmail("user@users.com")).thenReturn(Optional.of(user));

        // when
        Optional<User> result = userService.getUserByEmail("user@users.com");

        // then
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(repository).findUserByEmail("user@users.com");
    }

    @Test
    void getUserByEmailMethodShouldThrowIllegalArgumentExceptionForNullUserEmail() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserByEmail(null));
        verify(repository, never()).findUserByEmail("user@users.com");
    }

    @Test
    void getUserByEmailMethodShouldThrowExceptionWhenAnErrorOccursDuringGettingUserByEmail() {
        // given
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).findUserByEmail("user@users.com");

        // then
        assertThrows(ServiceOperationException.class, () -> userService.getUserByEmail("user@users.com"));
        verify(repository).findUserByEmail("user@users.com");
    }

    @Test
    void shouldReturnAllUsers() throws ServiceOperationException {
        // given
        List<User> users = List.of(UserGenerator.getRandomEmployee(), UserGenerator.getRandomEmployee());
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        when(repository.findAll(sort)).thenReturn(users);

        // when
        Collection<User> result = userService.getAllUsers();

        // then
        assertEquals(users, result);
        verify(repository).findAll(sort);
    }

    @Test
    void getAllUsersMethodShouldThrowExceptionWhenAnErrorOccursDuringGettingAllUsers() {
        // given
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).findAll(sort);

        // then
        assertThrows(ServiceOperationException.class, () -> userService.getAllUsers());
        verify(repository).findAll(sort);
    }

    @Test
    void shouldReturnAllActiveUsers() throws ServiceOperationException {
        // given
        List<User> users = List.of(UserGenerator.getRandomEmployee(), UserGenerator.getRandomEmployee());
        Example<User> example = Example.of(new User.Builder().withIsActive(true).build());
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        when(repository.findAll(example, sort)).thenReturn(users);

        // when
        Collection<User> result = userService.getAllActiveUsers();

        // then
        assertEquals(users, result);
        verify(repository).findAll(example, sort);
    }

    @Test
    void getAllActiveUsersMethodShouldThrowExceptionWhenAnErrorOccursDuringGettingAllActiveUsers() {
        // given
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).findAll((Example<User>) any(), (Sort) any());

        // then
        assertThrows(ServiceOperationException.class, () -> userService.getAllActiveUsers());
        verify(repository).findAll((Example<User>) any(), (Sort) any());
    }

    @Test
    void shouldDeleteUser() throws ServiceOperationException {
        // given
        doNothing().when(repository).deleteById(1L);

        // when
        userService.deleteUser(1L);

        // then
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteUserMethodShouldThrowIllegalArgumentExceptionForNullUserId() {
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(null));
    }

    @Test
    void deleteUserMethodShouldThrowExceptionWhenAnErrorOccursDuringDeletingUser() {
        // given
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).deleteById(1L);

        // then
        assertThrows(ServiceOperationException.class, () -> userService.deleteUser(1L));
        verify(repository).deleteById(1L);
    }

    @Test
    void shouldReturnTrueWhenUserExistsInDatabase() throws ServiceOperationException {
        // given
        when(repository.existsById(1L)).thenReturn(true);

        // when
        boolean result = userService.userExists(1L);

        // then
        assertTrue(result);
        verify(repository).existsById(1L);
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotExistInDatabase() throws ServiceOperationException {
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
    void userExistsMethodShouldThrowExceptionWhenAnErrorOccursDuringCheckingUserExists() {
        // given
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).existsById(1L);

        // then
        assertThrows(ServiceOperationException.class, () -> userService.userExists(1L));
        verify(repository).existsById(1L);
    }

    @Test
    void shouldReturnNumberOfUsers() throws ServiceOperationException {
        // given
        when(repository.count()).thenReturn(10L);

        // when
        long result = userService.usersCount();

        // then
        assertEquals(10L, result);
        verify(repository).count();
    }

    @Test
    void usersCountMethodShouldThrowExceptionWhenAnErrorOccursDuringGettingNumberOfUsers() {
        // given
        doThrow(new NonTransientDataAccessException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(repository).count();

        // then
        assertThrows(ServiceOperationException.class, () -> userService.usersCount());
        verify(repository).count();
    }
}
