package org.scormican.sendemailservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.scormican.sendemailservice.model.EmailDTO;
import org.scormican.sendemailservice.services.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class EmailController {
    public static final String API_PATH = "/api/v1/sendemail";

    private final EmailService emailService;

    public ResponseEntity handlePost(@Validated @RequestBody EmailDTO email) {
        //TODO parse multiple emails and create/update their DB values
        //TODO save the new emails
        //TODO call backend service with refined list of Emails to send to

        EmailDTO savedEmail = emailService.saveNewEmail(email);

        return new ResponseEntity(HttpStatus.CREATED);
    }
}