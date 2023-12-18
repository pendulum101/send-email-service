package org.scormican.sendemailservice.services;

import java.util.List;
import org.scormican.sendemailservice.controller.BackendEmailServiceFailureException;
import org.scormican.sendemailservice.model.EmailDTO;

public interface EmailService {

    List<EmailDTO> sendEmailsAndUpdateDB(List<EmailDTO> emailDTOList) throws BackendEmailServiceFailureException;
}