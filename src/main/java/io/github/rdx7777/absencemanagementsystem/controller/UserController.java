package io.github.rdx7777.absencemanagementsystem.controller;

import io.github.rdx7777.absencemanagementsystem.model.AppModelMapper;
import io.github.rdx7777.absencemanagementsystem.model.User;
import io.github.rdx7777.absencemanagementsystem.model.UserDTO;
import io.github.rdx7777.absencemanagementsystem.model.validation.UserValidator;
import io.github.rdx7777.absencemanagementsystem.service.ServiceOperationException;
import io.github.rdx7777.absencemanagementsystem.service.UserService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService service;
    private final AppModelMapper appModelMapper;

    @Autowired
    public UserController(UserService service, AppModelMapper appModelMapper) {
        this.service = service;
        this.appModelMapper = appModelMapper;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addUser(@RequestBody (required = false) User user) throws ServiceOperationException {
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
        UserDTO addedUser = appModelMapper.mapToUserDTO(service.addUser(user));
        logger.debug("New user added with id: {}.", addedUser.getId());
        URI location = URI.create(String.format("/api/users/%d", addedUser.getId()));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(location);
        return new ResponseEntity<>(addedUser, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody (required = false) User user) throws ServiceOperationException {
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
        UserDTO updatedUser = appModelMapper.mapToUserDTO(service.updateUser(user));
        logger.debug("Updated user with id {}.", updatedUser.getId());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(updatedUser, httpHeaders, HttpStatus.OK);
    }

    /**
     * returns full user with password - it's only used to edit user details
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> getUser(@PathVariable("id") Long id) throws ServiceOperationException {
        Optional<User> user = service.getUserById(id);
        if (user.isEmpty()) {
            logger.error("Attempt to get user by id that does not exist in database.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attempt to get user by id that does not exist in database.");
        }
        return ResponseEntity.ok(user.get());
    }

    @GetMapping(value = "/show_user/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> getUserForShow(@PathVariable("id") Long id) throws ServiceOperationException {
        Optional<User> user = service.getUserById(id);
        if (user.isEmpty()) {
            logger.error("Attempt to get user by id that does not exist in database.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attempt to get user by id that does not exist in database.");
        }
        return ResponseEntity.ok(appModelMapper.mapToUserDTO(user.get()));
    }

    @GetMapping(produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> getAllUsers() throws ServiceOperationException {
        logger.info("Attempt to get all users.");
        return ResponseEntity.ok(appModelMapper.mapToUserDTOList((List<User>) service.getAllUsers()));
    }

    @GetMapping(params = {"offset", "limit"}, produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsersPaginated(@RequestParam(name = "offset") Long offset,
                                                  @RequestParam(name = "limit") Long limit) throws ServiceOperationException {
        if (offset == null) {
            logger.error("Attempt to get all paginated users providing null offset.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to get all paginated users providing null offset.");
        }
        if (limit == null) {
            logger.error("Attempt to get all paginated users providing null limit.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to get all paginated users providing null limit.");
        }
        logger.info("Attempt to get all paginated users with offset {} and limit {}.", offset, limit);
        return ResponseEntity.ok(appModelMapper.mapToUserDTOList((List<User>) service.getAllUsersPaginated(offset, limit)));
    }

    @GetMapping(value = "/active", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> getAllActiveUsers() throws ServiceOperationException {
        logger.info("Attempt to get all active users.");
        return ResponseEntity.ok(appModelMapper.mapToUserDTOList((List<User>) service.getAllActiveUsers()));
    }

    @GetMapping(value = "/headteachers", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('CS_SUPERVISOR') or hasRole('HEAD_TEACHER') or hasRole('HR_SUPERVISOR')")
    public ResponseEntity<?> getHeadTeachers() throws ServiceOperationException {
        logger.info("Attempt to get all Head Teachers.");
        return ResponseEntity.ok(appModelMapper.mapToUserDTOList((List<User>) service.getHeadTeachers()));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) throws ServiceOperationException {
        if (!service.userExists(id)) {
            logger.error("Attempt to delete not existing user.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attempt to delete not existing user.");
        }
        service.deleteUser(id);
        logger.debug("Deleted user with id {}.", id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(httpHeaders, HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/count", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> countUsers() throws ServiceOperationException {
        logger.info("Attempt to get number of all users.");
        return ResponseEntity.ok(service.usersCount());
    }
}
