package org.scormican.sendemailservice.services;

import com.sendgrid.Request;
import com.sendgrid.Response;
import java.io.IOException;

public interface SendService {

    /**
     * Makes an API call to the Email Sending Service to send an email and returns the response.
     *
     * @param request the request object containing the email details
     * @return the response object from the API call
     * @throws IOException if an I/O error occurs during the API call
     */
    Response api(Request request) throws IOException;
}