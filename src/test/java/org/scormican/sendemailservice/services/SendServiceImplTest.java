package org.scormican.sendemailservice.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SendServiceImplTest {

        @Mock
        SendGrid sendGrid;

        @InjectMocks
        SendServiceImpl sendService;

        @Test
        public void testApiMethod() throws IOException {
            Request mockRequest = new Request();
            mockRequest.setMethod(Method.POST);
            Response mockResponse = new Response();
            when(sendGrid.api(mockRequest)).thenReturn(mockResponse);

            Response actualResponse = sendService.api(mockRequest);

            assertEquals(mockResponse, actualResponse);
        }
    }