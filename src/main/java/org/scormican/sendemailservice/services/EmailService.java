package org.scormican.sendemailservice.services;

import java.util.List;
import org.scormican.sendemailservice.model.EmailDTO;

public interface EmailService {

    /**
     * Sends emails and updates the database with the given list of EmailDTO objects.
     *
     * @param emailDTOList The list of EmailDTO objects to send and update.
     * @return The list of EmailDTO objects sent and saved to DB.
     */
    List<EmailDTO> sendEmailsAndUpdateDB(List<EmailDTO> emailDTOList);

}