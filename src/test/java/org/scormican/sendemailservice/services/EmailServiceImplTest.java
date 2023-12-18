package org.scormican.sendemailservice.services;

import org.scormican.sendemailservice.mappers.EmailMapper;
import org.scormican.sendemailservice.repositories.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(EmailServiceImpl.class)
class EmailServiceImplTest {
    //TODO ServiceImplTest

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    EmailMapper emailMapper;

    EmailService emailService;

    void sendEmailsAndUpdateDB() {

    }

}