package org.scormican.sendemailservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sendgrid.Response;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.scormican.sendemailservice.entities.Email;
import org.scormican.sendemailservice.mappers.EmailMapper;
import org.scormican.sendemailservice.model.EmailDTO;
import org.scormican.sendemailservice.repositories.EmailRepository;
import org.scormican.sendemailservice.services.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    SendService sendService;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    public final String VALID_EMAIL = "test@mail.com";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .build();
    }

    @Test
    void testSaveNewEmail() throws Exception {
        when(sendService.api(any())).thenReturn(new Response());

        List<EmailDTO> testEmails = getTestEmail(VALID_EMAIL);
        ResponseEntity responseEntity = emailController.handlePost(testEmails);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);
        List<String> emailAddrs = List.of(VALID_EMAIL);
        Email email = emailRepository.findAllByEmailAddrIgnoreCaseIn(emailAddrs).get(0);
        assertThat(email.getId()).isEqualTo(savedUUID);
    }


    private List<EmailDTO> getTestEmail(String emailAddr) {
        EmailDTO testEmail = EmailDTO.builder()
            .emailAddr(emailAddr)
            .build();

        return List.of(testEmail);
    }
}