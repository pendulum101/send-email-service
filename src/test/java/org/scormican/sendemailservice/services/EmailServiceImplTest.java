package org.scormican.sendemailservice.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.sendgrid.Response;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.scormican.sendemailservice.controller.EmailController;
import org.scormican.sendemailservice.entities.Email;
import org.scormican.sendemailservice.mappers.EmailMapper;
import org.scormican.sendemailservice.model.EmailDTO;
import org.scormican.sendemailservice.repositories.EmailRepository;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(EmailServiceImpl.class)
@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    private static final String VALID_EMAIL = "a@mail.com";

    @MockBean
    EmailRepository emailRepository;

    @MockBean
    EmailController emailController;

    EmailService emailService;

    @MockBean
    EmailMapper emailMapper;

    @MockBean
    SendService sendService;

    Email email = new Email(UUID.randomUUID(), VALID_EMAIL, 0, LocalDateTime.now());

    @BeforeEach
    void setUp() {
        emailService = new EmailServiceImpl(emailMapper, emailRepository, sendService);
    }

    @Test
    void testSendEmailsAndUpdateDB() throws IOException {
        EmailDTO dto = EmailDTO.builder().emailAddr(VALID_EMAIL).build();

        given(sendService.api(any())).willReturn(new Response());
        given(emailMapper.emailDtoToEmail(any(EmailDTO.class)))
            .willReturn(email);
        given(emailMapper.emailToEmailDto(any(Email.class)))
            .willReturn(dto);
        given(emailRepository.saveAll(any()))
            .willReturn(List.of(email));
        List<EmailDTO> emailDTOS = emailService.sendEmailsAndUpdateDB(List.of(dto));
        assert (emailDTOS).contains(dto);
    }


}