package org.scormican.sendemailservice.services;

import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendServiceImpl implements SendService {

    @Value("${sendgrid.api.key}")
    private String apiKey;
    private final SendGrid sendGrid;

    public SendServiceImpl() {
        sendGrid = new SendGrid(apiKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response api(Request request) throws IOException {
        return sendGrid.api(request);
    }
}