package org.scormican.sendemailservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.scormican.sendemailservice.model.EmailDTO;
import org.scormican.sendemailservice.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@WebMvcTest(EmailController.class)
class EmailControllerTest {

    private static final String TEST_EMAIL_ADDR = "test@mail.com";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EmailService emailService;

    FakeEmailServiceImpl fakeEmailServiceImpl;

    @Autowired
    private LocalValidatorFactoryBean validator;

    public final String VALID_EMAIL = "test@mail.com";
    public final String INVALID_EMAIL = "test";

    @BeforeEach
    void setUp() {
        fakeEmailServiceImpl = new FakeEmailServiceImpl();
    }

    @Test
    void testSendEmail() throws Exception {
        EmailDTO emailDTO = EmailDTO.builder()
            .emailAddr(FakeEmailServiceImpl.RECENT_EMAIL)
            .build();
        List<EmailDTO> emailDtoList = Collections.singletonList(emailDTO);

        given(emailService.sendEmailsAndUpdateDB(any(List.class)))
            .willReturn(fakeEmailServiceImpl.sendEmailsAndUpdateDB(emailDtoList));

        MvcResult mvcResult = mockMvc.perform(post(EmailController.API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailDtoList)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andReturn();
    }

    @Test
    void testSendEmailBlank() throws Exception {
        EmailDTO emailDTO = EmailDTO.builder()
            .emailAddr("")
            .build();
        EmailDTO emailDTO2 = EmailDTO.builder()
            .emailAddr(TEST_EMAIL_ADDR)
            .build();
        List<EmailDTO> listdto = Arrays.asList(emailDTO, emailDTO2);

        given(emailService.sendEmailsAndUpdateDB(any(List.class)))
            .willReturn(fakeEmailServiceImpl.sendEmailsAndUpdateDB(listdto));

        MvcResult mvcResult = mockMvc.perform(post(EmailController.API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listdto)))
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void testSendEmailInvalidFormat() throws Exception {
        EmailDTO emailDTO = EmailDTO.builder()
            .emailAddr("not_an_email@ ")
            .build();
        List<EmailDTO> listdto = Collections.singletonList(emailDTO);

        given(emailService.sendEmailsAndUpdateDB(any(List.class)))
            .willReturn(fakeEmailServiceImpl.sendEmailsAndUpdateDB(listdto));

        MvcResult mvcResult = mockMvc.perform(post(EmailController.API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listdto)))
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void testThrowNotFoundHTTPException() throws Exception {
        List<EmailDTO> listdto = new ArrayList<>();

        given(emailService.sendEmailsAndUpdateDB(any(List.class)))
            .willThrow(HandlerMethodValidationException.class);

        mockMvc.perform(post(EmailController.API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listdto)))
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    public void testValidationValid() {
        EmailDTO dto = EmailDTO.builder()
            .emailAddr(VALID_EMAIL).build();

        Set<ConstraintViolation<EmailDTO>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    public void testValidationInvalid() {
        EmailDTO dto = EmailDTO.builder()
            .emailAddr(INVALID_EMAIL).build();

        Set<ConstraintViolation<EmailDTO>> violations = validator.validate(dto);
        List<ConstraintViolation<EmailDTO>> list = violations.stream().toList();
        assertThat(list.get(0).getInvalidValue()).isEqualTo(INVALID_EMAIL);
    }
}