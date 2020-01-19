package io.github.rdx7777.absencemanagementsystem.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleUnexpectedException(Exception e, WebRequest request) {
        logger.error("Handling {} due to {}", e.getClass().getSimpleName(), e.getMessage());
        return createJsonResponse(e, request);
    }

    private ResponseEntity<Object> createJsonResponse(Exception e, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (e instanceof ResponseStatusException) {
            return new ResponseEntity<>(createExceptionBody(((ResponseStatusException) e).getStatus(),
                ((ResponseStatusException) e).getReason(),
                request.getDescription(false)),
                headers, ((ResponseStatusException) e).getStatus());
        }
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(createExceptionBody(status,
            "An unexpected error occurred.", request.getDescription(false)), headers, status);
    }

    private Map<String, Object> createExceptionBody(HttpStatus status, String message, String path) {
        Map<String, Object> exceptionBody = new LinkedHashMap<>();
        exceptionBody.put("timestamp", LocalDateTime.now());
        exceptionBody.put("status", status.value());
        exceptionBody.put("error", status.getReasonPhrase());
        exceptionBody.put("message", message);
        exceptionBody.put("path", path);
        return exceptionBody;
    }
}
