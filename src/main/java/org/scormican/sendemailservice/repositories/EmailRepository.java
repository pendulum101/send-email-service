package org.scormican.sendemailservice.repositories;

import java.util.List;
import java.util.UUID;
import org.scormican.sendemailservice.entities.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, UUID> {

    /**
     * Retrieves all Email objects from the DB that have email addresses
     * matching the email addresses in the provided list.
     *
     * @param emailAddrs the list of email addresses to search for
     * @return a List of Email objects matching email addresses in the list,
     *         or an empty List if no matches are found
     */
    List<Email> findAllByEmailAddrIgnoreCaseIn(List<String> emailAddrs);
}