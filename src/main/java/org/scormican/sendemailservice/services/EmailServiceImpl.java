package org.scormican.sendemailservice.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.scormican.sendemailservice.controller.BackendEmailServiceFailureException;
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
    private final SendService sendService;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EmailDTO> sendEmailsAndUpdateDB(List<EmailDTO> emailDTOList) {
        List<EmailDTO> existingEmailAddresses = getExistingEmailAddresses(emailDTOList);
        List<EmailDTO> newEmailsToAddAndSend = removeExistingEmails(emailDTOList, existingEmailAddresses);

        List<EmailDTO> existingEmailsToUpdateAndSend = existingEmailAddresses.stream()
            .filter(dto -> dto.getSendDate().isBefore(LocalDateTime.now().minusMinutes(COOL_OFF_MINS)))
            .toList();

        List<EmailDTO> emailsToSave = Stream.concat(newEmailsToAddAndSend.stream(),
            existingEmailsToUpdateAndSend.stream()).toList();
        List<EmailDTO> savedEmails = saveEmails(emailsToSave);
        sendEmails(savedEmails);
        return savedEmails;
    }

    private void sendEmails(List<EmailDTO> combinedList) {
        for (EmailDTO dto : combinedList) {
            Mail mail = getMail(dto);
            Request request = new Request();

            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sendService.api(request);
                System.out.println(response.getStatusCode());
                System.out.println(response.getBody());
                System.out.println(response.getHeaders());
            } catch (IOException ex) {
                log.error("Failed to send request to the email service", ex);
                throw new BackendEmailServiceFailureException();
            }
        }
    }

    private Mail getMail(EmailDTO dto) {
        com.sendgrid.helpers.mail.objects.Email from = new com.sendgrid.helpers.mail.objects.Email(
            "noreply@test.com");
        String subject = "Email Sending Assignment";
        com.sendgrid.helpers.mail.objects.Email to = new com.sendgrid.helpers.mail.objects.Email(
            dto.getEmailAddr());
        Content content = new Content("text/plain",
            "You have just received an email from Sean as part of the Email Sending Assessment development");
        return new Mail(from, subject, to, content);
    }

    private List<EmailDTO> removeExistingEmails(List<EmailDTO> emailDTOList, List<EmailDTO> existingEmailAddresses) {
        List<String> emailsToRemove = existingEmailAddresses.stream().map(e -> e.getEmailAddr()).toList();
        return emailDTOList.stream()
            .filter(dto -> !emailsToRemove.contains(dto.getEmailAddr())).toList();
    }

    private List<EmailDTO> saveEmails(List<EmailDTO> emailDTO) {
        List<Email> emailEntities = emailDTO.stream().map(emailMapper::emailDtoToEmail).toList();
        for (Email e : emailEntities) {
            e.setSendDate(LocalDateTime.now());
        }
        return emailRepository.saveAll(emailEntities).stream().map(emailMapper::emailToEmailDto).toList();
    }

    private List<EmailDTO> getExistingEmailAddresses(List<EmailDTO> emailDTOList) {
        List<String> emailAddrs = emailDTOList.stream().map(EmailDTO::getEmailAddr).collect(Collectors.toList());
        List<Email> emails = emailRepository.findAllByEmailAddrIgnoreCaseIn(emailAddrs);
        return emails.stream().map(emailMapper::emailToEmailDto).collect(Collectors.toList());
    }
}