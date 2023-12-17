package org.scormican.sendemailservice.controller;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
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

    @BeforeEach
    void setUp() {
        fakeEmailServiceImpl = new FakeEmailServiceImpl();
    }

    @Test
    void testSendEmail() throws Exception {
        EmailDTO emailDTO = EmailDTO.builder()
            .emailAddr(FakeEmailServiceImpl.RECENT_EMAIL)
            .build();
        List<EmailDTO> emailDtoList = Arrays.asList(emailDTO);

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
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    void testSendEmailInvalidFormat() throws Exception {
        EmailDTO emailDTO = EmailDTO.builder()
            .emailAddr("not_an_email@ ")
            .build();
        List<EmailDTO> listdto = Arrays.asList(emailDTO);

        given(emailService.sendEmailsAndUpdateDB(any(List.class)))
            .willReturn(fakeEmailServiceImpl.sendEmailsAndUpdateDB(listdto));

        MvcResult mvcResult = mockMvc.perform(post(EmailController.API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listdto)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }
}