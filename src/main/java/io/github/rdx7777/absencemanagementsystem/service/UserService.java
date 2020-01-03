package io.github.rdx7777.absencemanagementsystem.service;

import io.github.rdx7777.absencemanagementsystem.model.User;
import io.github.rdx7777.absencemanagementsystem.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User addUser(User user) throws ServiceOperationException {
        if (user == null) {
            logger.error("Attempt to add null user.");
            throw new IllegalArgumentException("User cannot be null.");
        }
        try {
            Long userId = user.getId();
            if (userId != null && repository.existsById(userId)) {
                logger.error("Attempt to add user already existing in database.");
                throw new ServiceOperationException("User already exists in database.");
            }
            return repository.save(user);
        } catch (Exception e) {
            String message = "An error occurred during adding user to database.";
            logger.error(message);
            throw new ServiceOperationException(message, e);
        }
    }

    public User updateUser(User user) throws ServiceOperationException {
        if (user == null) {
            logger.error("Attempt to update user providing null user.");
            throw new IllegalArgumentException("User cannot be null.");
        }
        try {
            Long userId = user.getId();
            if (userId == null || !repository.existsById(userId)) {
                logger.error("Attempt to update not existing user.");
                throw new ServiceOperationException("Given user does not exist in database.");
            }
            return repository.save(user);
        } catch (Exception e) {
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
        } catch (Exception e) {
            String message = "An error occurred during getting user by id.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<User> getAllUsers() throws ServiceOperationException {
        try {
            return repository.findAll();
        } catch (Exception e) {
            String message = "An error occurred during getting all users.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<User> getAllActiveUsers() throws ServiceOperationException {
        Example<User> example = Example.of(new User.Builder().withIsActive(true).build());
        try {
            return repository.findAll(example);
        } catch (Exception e) {
            String message = "An error occurred during getting all active users.";
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
        } catch (Exception e) {
            String message = "An error occurred during checking if user exists.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public long usersCount() throws ServiceOperationException {
        try {
            return repository.count();
        } catch (Exception e) {
            String message = "An error occurred during getting number of users.";
            logger.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }
}
