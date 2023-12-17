package org.scormican.sendemailservice.services;

import java.util.List;
import org.scormican.sendemailservice.model.EmailDTO;

public interface EmailService {
//    EmailDTO saveNewEmail(EmailDTO emailDTO);
//    EmailDTO updateExistingEmail(String emailAddr);
//    List<EmailDTO> getExistingEmailAddresses(List<String> emailAddrs);

    List<EmailDTO> sendEmailsAndUpdateDB(List<EmailDTO> emailDTO);
}