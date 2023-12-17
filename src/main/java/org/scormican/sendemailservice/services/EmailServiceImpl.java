package org.scormican.sendemailservice.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.scormican.sendemailservice.entities.Email;
import org.scormican.sendemailservice.mappers.EmailMapper;
import org.scormican.sendemailservice.model.EmailDTO;
import org.scormican.sendemailservice.repositories.EmailRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailMapper emailMapper;
    private final EmailRepository emailRepository;

    @Override
    public List<EmailDTO> sendEmailsAndUpdateDB(List<EmailDTO> emailDTO) {

        //TODO Validate each email using predicate, 404 error if any email is invalid
        //TODO create/update their DB values (only update if >5mins since last email)
        //TODO save the new emails
        //TODO call backend service with refined list of Emails to send to
        //TODO return something to determine the response code 201 vs. 404 boolean?
        List<EmailDTO> createdEmails = new ArrayList<>();
        createdEmails.add(saveNewEmail(emailDTO.get(0)));
        return createdEmails;
    }

    private EmailDTO saveNewEmail(EmailDTO email) {
        return emailMapper.emailToEmailDto(emailRepository.save(emailMapper.emailDtoToEmail(email)));
    }

    private EmailDTO updateExistingEmail(String emailAddr) {
        List<String> emailAddrs = new ArrayList<>();
        emailAddrs.add(emailAddr);
        Optional<EmailDTO> update = getExistingEmailAddresses(emailAddrs).stream().findFirst();
        //TODO handle the update and error if there is no value returned
//        update.setSendDate(LocalDateTime.now());
        return null;
    }

    private List<EmailDTO> getExistingEmailAddresses(List<String> emailAddrs) {
        List<Email> emails = emailRepository.findAllByEmailAddrIgnoreCaseIn(emailAddrs);
        return emails.stream().map(emailMapper::emailToEmailDto).collect(Collectors.toList());
    }




    //TODO invalid email
//TODO connect to email service
//TODO send single email

//TODO unit testing

//TODO Store configuration for Breva

//TODO javadoc generation
//TODO README.MD
}