package org.scormican.sendemailservice.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.scormican.sendemailservice.model.EmailDTO;
import org.scormican.sendemailservice.services.EmailService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class EmailController {
    public static final String API_PATH = "/api/v1/sendemail";

    private final EmailService emailService;

    @PostMapping(API_PATH)
    public ResponseEntity handlePost(@RequestBody @Valid List<EmailDTO> addresses) {
        List<EmailDTO> emailDTOs = emailService.sendEmailsAndUpdateDB(addresses);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", API_PATH + "/" + emailDTOs.get(0).getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    //TODO 404 bad request response "You entered an incorrectly formatted email, Please try again"
}