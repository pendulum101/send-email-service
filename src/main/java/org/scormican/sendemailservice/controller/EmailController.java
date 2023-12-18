package org.scormican.sendemailservice.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.scormican.sendemailservice.model.EmailDTO;
import org.scormican.sendemailservice.services.EmailService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class EmailController {
    public static final String API_PATH = "/api/v1/sendemail";
    public static final String ERROR_MSG = "You entered an incorrectly formatted email, Please try again";
    private final EmailService emailService;

    @PostMapping(API_PATH)
    public ResponseEntity handlePost(@RequestBody @Valid List<EmailDTO> addresses) {

        List<EmailDTO> emailDTOs = emailService.sendEmailsAndUpdateDB(addresses);

        ResponseEntity responseEntity;
        if (!emailDTOs.isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", API_PATH + "/" + emailDTOs.get(0).getId().toString());
            responseEntity = new ResponseEntity(headers, HttpStatus.CREATED);
        } else {
            responseEntity = new ResponseEntity("No new emails to send", HttpStatus.OK);
        }

        return responseEntity;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HandlerMethodValidationException.class, ConstraintViolationException.class})
    public ResponseEntity handleValidationExceptions() {
        return new ResponseEntity<>("You entered an incorrectly formatted email, Please try again", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BackendEmailServiceFailureException.class)
    public ResponseEntity handleBackendEmailServiceFailure() {
        return new ResponseEntity<>("Unable to send request to the email service", HttpStatus.NOT_FOUND);
    }
}