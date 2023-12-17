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

    DummyEmailServiceImpl dummyEmailServiceImpl;

    @BeforeEach
    void setUp() {
        dummyEmailServiceImpl = new DummyEmailServiceImpl();
    }

    @Test
    void testSendEmail() throws Exception {
        EmailDTO emailDTO = EmailDTO.builder()
            .emailAddr(DummyEmailServiceImpl.RECENT_EMAIL)
            .build();
        List<EmailDTO> emailDtoList = Arrays.asList(emailDTO);

        given(emailService.sendEmailsAndUpdateDB(any(List.class)))
            .willReturn(dummyEmailServiceImpl.sendEmailsAndUpdateDB(emailDtoList));

        MvcResult mvcResult = mockMvc.perform(post(EmailController.API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailDtoList)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andReturn();
    }

    @Test
    void testSendEmailInvalid() throws Exception {
        EmailDTO emailDTO = EmailDTO.builder()
            .emailAddr("")
            .build();
        List<EmailDTO> listdto = Arrays.asList(emailDTO);

        given(emailService.sendEmailsAndUpdateDB(any(List.class)))
            .willReturn(dummyEmailServiceImpl.sendEmailsAndUpdateDB(listdto));

        MvcResult mvcResult = mockMvc.perform(post(EmailController.API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listdto)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }
}