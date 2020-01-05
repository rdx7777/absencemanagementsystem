package io.github.rdx7777.absencemanagementsystem.controller;

import io.github.rdx7777.absencemanagementsystem.model.User;
import io.github.rdx7777.absencemanagementsystem.model.validation.UserValidator;
import io.github.rdx7777.absencemanagementsystem.service.ServiceOperationException;
import io.github.rdx7777.absencemanagementsystem.service.UserService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User user) throws ServiceOperationException {
        if (user == null) {
            logger.error("Attempt to add null user.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to add null user.");
        }
        if (user.getId() != null && service.userExists(user.getId())) {
            logger.error("Attempt to add user already existing in database.");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Attempt to add user already existing in database.");
        }
        List<String> validations = UserValidator.validate(user);
        if (validations.size() > 0) {
            logger.error("Attempt to add invalid user to database.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to add invalid user to database.");
        }
        User addedUser = service.addUser(user);
        logger.debug("New user added with id: {}.", addedUser.getId());
        URI location = URI.create(String.format("/api/users/%d", addedUser.getId()));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(location);
        return new ResponseEntity<>(addedUser, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody User user) throws ServiceOperationException {
        if (user == null) {
            logger.error("Attempt to update user providing null user.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to update user providing null user.");
        }
        if (!id.equals(user.getId())) {
            logger.error("Attempt to update user providing different user id.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to update user providing different user id.");
        }
        if (!service.userExists(id)) {
            logger.error("Attempt to update not existing user.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attempt to update not existing user.");
        }
        List<String> validations = UserValidator.validate(user);
        if (validations.size() > 0) {
            logger.error("Attempt to update invalid user.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to update invalid user.");
        }
        logger.debug("Updated user with id {}.", user.getId());
        return ResponseEntity.ok(service.updateUser(user));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
        Optional<User> user = service.getUserById(id);
        if (user.isEmpty()) {
            logger.error("Attempt to get user by id that does not exist in database.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attempt to get user by id that does not exist in database.");
        }
        return ResponseEntity.ok(user.get());
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        logger.info("Attempt to get all users.");
        return ResponseEntity.ok(service.getAllUsers());
    }

    @GetMapping(value = "/active")
    public ResponseEntity<?> getAllActiveUsers() {
        logger.info("Attempt to get all active users.");
        return ResponseEntity.ok(service.getAllActiveUsers());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        if (!service.userExists(id)) {
            logger.error("Attempt to delete not existing user.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attempt to delete not existing user.");
        }
        service.deleteUser(id);
        logger.debug("Deleted user with id {}.", id);
        return ResponseEntity.noContent().build();
    }
}
