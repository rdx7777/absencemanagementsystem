package io.github.rdx7777.absencemanagementsystem.controller;

import io.github.rdx7777.absencemanagementsystem.model.User;
import io.github.rdx7777.absencemanagementsystem.service.ServiceOperationException;
import io.github.rdx7777.absencemanagementsystem.service.UserService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/")
public class SecurityController {

    private final Logger logger = LoggerFactory.getLogger(SecurityController.class);

    private UserService service;

    public SecurityController(UserService service) {
        this.service = service;
    }

    /*@GetMapping
    @ResponseBody
    public Long getLoggedUserId(Authentication authentication) throws ServiceOperationException {
        String email = authentication.getName();
        logger.info("Logged user email: " + email);
        Optional<User> loggedUser = service.getUserByEmail(email);
        if (loggedUser.isEmpty()) {
            logger.error("Logged user does not exist in user database.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged user does not exist in user database.");
        } else {
            Long loggedUserId = loggedUser.get().getId();
            return loggedUserId;
        }
    }*/

    @GetMapping
    @ResponseBody
    public User getLoggedUser(Authentication authentication) throws ServiceOperationException {
        String email = authentication.getName();
        logger.info("Logged user email: " + email);
        Optional<User> loggedUser = service.getUserByEmail(email);
        if (loggedUser.isEmpty()) {
            logger.error("Logged user does not exist in user database.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged user does not exist in user database.");
        } else {
            return loggedUser.get();
        }
    }
}

/*
        Object details = authentication.getDetails();
        Object credentials = authentication.getCredentials();
        Object principal = authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        System.out.println("Details: " + details);
        System.out.println("Credentials: " + credentials);
        System.out.println("Principal: " + principal);
        authorities.forEach(System.out::println);
*/
