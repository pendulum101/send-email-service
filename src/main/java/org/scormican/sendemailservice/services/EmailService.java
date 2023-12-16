package org.scormican.sendemailservice.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.scormican.sendemailservice.model.EmailDTO;

public interface EmailService {
    EmailDTO saveNewEmail(EmailDTO email);
    Optional<EmailDTO> updateEmail(String address, EmailDTO emailDTO);
    List<EmailDTO> getAllEmails();

}