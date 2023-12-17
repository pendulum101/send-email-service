package org.scormican.sendemailservice.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.scormican.sendemailservice.entities.Email;
import org.scormican.sendemailservice.mappers.EmailMapper;
import org.scormican.sendemailservice.model.EmailDTO;
import org.scormican.sendemailservice.repositories.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
class EmailControllerIT {

    @Autowired
    EmailController emailController;

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    EmailMapper emailMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    public final String CREATE_EMAIL = "test@mail.com";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .build();
    }

    @Test
    void testSaveNewEmail() throws Exception {
        List<EmailDTO> testEmails = getTestEmail();
        ResponseEntity responseEntity = emailController.handlePost(testEmails);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);
        List<String> emailAddrs = Arrays.asList(CREATE_EMAIL);
        Email email = emailRepository.findAllByEmailAddrIgnoreCaseIn(emailAddrs).get(0);
        assertThat(email.getId()).isEqualTo(savedUUID);
    }

    private List<EmailDTO> getTestEmail() {
        EmailDTO testEmail = EmailDTO.builder()
                        .emailAddr(CREATE_EMAIL)
                        .build();

        return Arrays.asList(testEmail);
    }
}