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
        Long userId = user.getId();
        if (userId != null && repository.existsById(userId)) {
            logger.error("Attempt to add user already existing in database.");
            throw new ServiceOperationException("User already exists in database.");
        }
        return repository.save(user);
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
        return repository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        if (id == null) {
            logger.error("Attempt to get user by id providing null id.");
            throw new IllegalArgumentException("Id cannot be null.");
        }
        return repository.findById(id);
    }

    public Collection<User> getAllUsers() {
        return repository.findAll();
    }

    public Collection<User> getAllActiveUsers() {
        Example<User> example = Example.of(new User.Builder().withIsActive(true).build());
        return repository.findAll(example);
    }

    public boolean userExists(Long id) {
        if (id == null) {
            logger.error("Attempt to check if user exists providing null id.");
            throw new IllegalArgumentException("Id cannot be null.");
        }
        return repository.existsById(id);
    }

    public long usersCount() {
        return repository.count();
    }
}
