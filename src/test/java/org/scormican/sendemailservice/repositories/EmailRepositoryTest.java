package org.scormican.sendemailservice.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.scormican.sendemailservice.entities.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class EmailRepositoryTest {

    @Autowired
    EmailRepository emailRepository;

    @Test
    void testSaveEmail() {
        Email email = emailRepository.save(Email.builder()
            .emailAddr("tet")
            .build());

        assertThat(email.getId()).isNotNull();
    }

    @Test
    void testSaveMultipleEmails() {
        Email email1 = Email.builder()
            .emailAddr("email1@mail.com")
            .build();
        Email email2 = Email.builder()
            .emailAddr("email2@mail.com")
            .build();

        List<Email> email = emailRepository.saveAll(List.of(email1, email2));

        assertThat(email.get(0).getId()).isNotNull();
        assertThat(email.get(1).getId()).isNotNull();
    }

    @Test
    void testFindAllByEmailAddrIgnoreCaseIn() {
        String emailAddr = "email1@mail.com";
        Email email = emailRepository.save(Email.builder()
            .emailAddr(emailAddr)
            .build());

        List<Email> returnedEmails = emailRepository.findAllByEmailAddrIgnoreCaseIn(List.of(emailAddr));
        assertThat(returnedEmails.get(0).getId()).isNotNull();
    }

    @Test
    void testFindAllByEmailAddrIgnoreCaseIn_noEmailFound() {
        String emailAddr = "email1@mail.com";

        List<Email> returnedEmails = emailRepository.findAllByEmailAddrIgnoreCaseIn(List.of(emailAddr));
        assertTrue(returnedEmails.isEmpty());
    }

}