package org.scormican.sendemailservice.repositories;

import org.scormican.sendemailservice.controller.EmailController;
import org.scormican.sendemailservice.mappers.EmailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailRepositoryIT {
    @Autowired
    EmailController emailController;

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    EmailMapper emailMapper;
}