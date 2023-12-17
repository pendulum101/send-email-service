package org.scormican.sendemailservice.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    public static final int COOL_OFF_MINS = 5;
    private final EmailMapper emailMapper;
    private final EmailRepository emailRepository;

    @Override
    public List<EmailDTO> sendEmailsAndUpdateDB(List<EmailDTO> emailDTOList) {
        if (emailDTOList.isEmpty()) {
            return null;
            //TODO 404 or 400 error if any email is invalid
        }
        List<EmailDTO> existingEmailAddresses = getExistingEmailAddresses(emailDTOList);
        List<EmailDTO> newEmailsToAddAndSend = removeExistingEmails(emailDTOList, existingEmailAddresses);

        List<EmailDTO> existingEmailsToUpdateAndSend = existingEmailAddresses.stream()
            .filter(dto -> dto.getSendDate().isBefore(LocalDateTime.now().minusMinutes(COOL_OFF_MINS)))
            .toList();

        //TODO handle case where there is no update to make currently responds with a 500
        List<EmailDTO> combinedList = Stream.concat(saveNewEmail(newEmailsToAddAndSend).stream(),
            updateExistingEmail(existingEmailsToUpdateAndSend).stream()).toList();

        sendEmails(combinedList);
        //TODO return something to determine the response code 201 vs. 404 boolean?
        return combinedList;
    }

    private void sendEmails(List<EmailDTO> combinedList) {
        //TODO call backend service with refined list of Emails to send to

    }

    private List<EmailDTO> removeExistingEmails(List<EmailDTO> emailDTOList, List<EmailDTO> existingEmailAddresses) {
        List<String> emailsToRemove = existingEmailAddresses.stream().map(e -> e.getEmailAddr()).toList();
        return emailDTOList.stream()
            .filter(dto -> !emailsToRemove.contains(dto.getEmailAddr())).toList();
    }

    private List<EmailDTO> saveNewEmail(List<EmailDTO> emailDTO) {
        List<Email> emailEntities = emailDTO.stream().map(emailMapper::emailDtoToEmail).toList();
        return emailRepository.saveAll(emailEntities).stream().map(emailMapper::emailToEmailDto).toList();
    }

    private List<EmailDTO> updateExistingEmail(List<EmailDTO> emailDTO) {
        List<Email> emailEntities = emailDTO.stream().map(emailMapper::emailDtoToEmail).toList();
        for(Email e : emailEntities) {
            e.setSendDate(LocalDateTime.now());
        }
        return emailRepository.saveAll(emailEntities).stream().map(emailMapper::emailToEmailDto).toList();
    }

    private List<EmailDTO> getExistingEmailAddresses(List<EmailDTO> emailDTOList) {
        List<String> emailAddrs = emailDTOList.stream().map(EmailDTO::getEmailAddr).collect(Collectors.toList());
        List<Email> emails = emailRepository.findAllByEmailAddrIgnoreCaseIn(emailAddrs);
        return emails.stream().map(emailMapper::emailToEmailDto).collect(Collectors.toList());
    }

//TODO connect to email service
//TODO send single email

//TODO unit testing

//TODO Store configuration for Breva

//TODO javadoc generation
//TODO README.MD
}