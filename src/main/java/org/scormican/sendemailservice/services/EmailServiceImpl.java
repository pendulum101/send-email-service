package org.scormican.sendemailservice.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.scormican.sendemailservice.model.EmailDTO;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    /**
     * @param email 
     * @return
     */
    @Override
    public EmailDTO saveNewEmail(EmailDTO email) {
        EmailDTO saveEmail = EmailDTO.builder()
            .email(email.getEmail())
            .sendDate(LocalDateTime.now())
            .build();
        return saveEmail;
    }

    /**
     * @param address 
     * @param emailDTO
     * @return
     */
    @Override
    public Optional<EmailDTO> updateEmail(String address, EmailDTO emailDTO) {
        EmailDTO update = getEmail(address);
        update.setSendDate(LocalDateTime.now());
        return Optional.of(update);
    }


    @Override
     public List<EmailDTO> findByEmailIn(List<String> emailList) {
        //TODO return subset of emails from DB
        List<EmailDTO> emailDtoList = new ArrayList<>();
        return emailDtoList;
    }

    public EmailDTO getEmail(String email) {
        //TODO retrieve the val from DB using address
        return EmailDTO.builder()
            .email(email)
            .sendDate(LocalDateTime.now())
            .build();
    }
}