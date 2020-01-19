package io.github.rdx7777.absencemanagementsystem.controller;

import static org.junit.Assert.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

class GlobalExceptionHandlerTest {

    GlobalExceptionHandler handler;
    WebRequest request;

    @BeforeEach
    void setup() {
        handler = new GlobalExceptionHandler();
        request = Mockito.mock(WebRequest.class);
    }

    @ParameterizedTest
    @MethodSource("statusExceptionArguments")
    void shouldReturnJsonResponseWithCorrectStatusAndBodyWhenResponseStatusExceptionIsThrown(ResponseStatusException exception) {
        ResponseEntity<Object> response = handler.handleUnexpectedException(exception, request);
        String stringBody = response.getBody().toString();
        assertEquals(exception.getReason(), extractMessageFromResponseBody(stringBody));
        assertEquals(exception.getStatus(), response.getStatusCode());
    }

    private static Stream<Arguments> statusExceptionArguments() {
        return Stream.of(
            Arguments.of(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to add null user.")),
            Arguments.of(new ResponseStatusException(HttpStatus.CONFLICT, "Attempt to add user already existing in database.")),
            Arguments.of(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to add invalid user to database.")),
            Arguments.of(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to update user providing null user.")),
            Arguments.of(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to update user providing different user id.")),
            Arguments.of(new ResponseStatusException(HttpStatus.NOT_FOUND, "Attempt to update not existing user.")),
            Arguments.of(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to update invalid user.")),
            Arguments.of(new ResponseStatusException(HttpStatus.NOT_FOUND, "Attempt to get user by id that does not exist in database.")),
            Arguments.of(new ResponseStatusException(HttpStatus.NOT_FOUND, "Attempt to delete not existing user."))
        );
    }

    @Test
    void shouldReturnJsonResponseWithCorrectStatusAndBodyWhenUnexpectedErrorOccurs() {
        ResponseEntity<Object> response = handler.handleUnexpectedException(new NullPointerException(), request);
        String stringBody = response.getBody().toString();
        assertEquals("An unexpected error occurred.", extractMessageFromResponseBody(stringBody));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private String extractMessageFromResponseBody(String body) {
        int startIndex = body.indexOf("message=");
        int endIndex = body.indexOf(", path=");
        return body.substring(startIndex, endIndex).replaceFirst("message=", "");
    }

}