package org.scormican.sendemailservice.repositories;

import java.util.List;
import java.util.UUID;
import org.scormican.sendemailservice.entities.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, UUID> {
      List<Email> findAllByEmailAddrIgnoreCaseIn(List<String> emailAddrs);
}