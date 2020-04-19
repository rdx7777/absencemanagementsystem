package io.github.rdx7777.absencemanagementsystem.service;

import io.github.rdx7777.absencemanagementsystem.model.Position;
import io.github.rdx7777.absencemanagementsystem.model.User;
import io.github.rdx7777.absencemanagementsystem.repository.UserRepository;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User addUser(User user) throws ServiceOperationException {
        if (user == null) {
            logger.error("Attempt to add null user.");
            throw new IllegalArgumentException("User cannot be null.");
        }
        Long userId = user.getId();
        if (userId != null && repository.existsById(userId)) {
            logger.error("Attempt to add user already existing in database.");
            throw new ServiceOperationException("User already exists in database.");
        }
        try {
            return repository.save(user);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during adding user.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public User updateUser(User user) throws ServiceOperationException {
        if (user == null) {
            logger.error("Attempt to update user providing null user.");
            throw new IllegalArgumentException("User cannot be null.");
        }
        Long userId = user.getId();
        if (userId == null || !repository.existsById(userId)) {
            logger.error("Attempt to update not existing user.");
            throw new ServiceOperationException("Given user does not exist in database.");
        }
        try {
            return repository.save(user);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during updating user.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Optional<User> getUserById(Long id) throws ServiceOperationException {
        if (id == null) {
            logger.error("Attempt to get user by id providing null id.");
            throw new IllegalArgumentException("Id cannot be null.");
        }
        try {
            return repository.findById(id);
        } catch (NoSuchElementException e) {
            String message = "An error occurred during getting user by id.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Optional<User> getUserByEmail(String email) throws ServiceOperationException {
        if (email == null) {
            logger.error("Attempt to get user by email providing null email.");
            throw new IllegalArgumentException("Email cannot be null.");
        }
        try {
            return repository.findUserByEmail(email);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting user by email.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<User> getAllUsers() throws ServiceOperationException {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        try {
            return repository.findAll(sort);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting all users.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<User> getAllActiveUsers() throws ServiceOperationException {
        Example<User> example = Example.of(new User.Builder().withIsActive(true).build());
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        try {
            return repository.findAll(example, sort);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting all active users.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<User> getHeadTeachers() throws ServiceOperationException {
        Example<User> example = Example.of(new User.Builder().withIsActive(true).withPosition(Position.HeadTeacher).build());
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        try {
            return repository.findAll(example, sort);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting Head Teachers.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Optional<User> getCoverSupervisor() throws ServiceOperationException {
        Example<User> example = Example.of(new User.Builder().withIsActive(true).withPosition(Position.CoverSupervisor).build());
        try {
            return repository.findOne(example);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting Cover Supervisor.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Optional<User> getHRSupervisor() throws ServiceOperationException {
        Example<User> example = Example.of(new User.Builder().withIsActive(true).withPosition(Position.HumanResourcesSupervisor).build());
        try {
            return repository.findOne(example);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting Human Resources Supervisor.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public void deleteUser(Long id) throws ServiceOperationException {
        if (id == null) {
            logger.error("Attempt to delete user providing null id.");
            throw new IllegalArgumentException("Id cannot be null.");
        }
        try {
            repository.deleteById(id);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during deleting user.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public boolean userExists(Long id) throws ServiceOperationException {
        if (id == null) {
            logger.error("Attempt to check if user exists providing null id.");
            throw new IllegalArgumentException("Id cannot be null.");
        }
        try {
            return repository.existsById(id);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during checking if user exists.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public long usersCount() throws ServiceOperationException {
        try {
            return repository.count();
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting number of users.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }
}
