package org.scormican.sendemailservice.repositories;

import org.scormican.sendemailservice.entities.Email;
import org.springframework.data.repository.CrudRepository;

public interface EmailRepository extends CrudRepository<Email, Long> {
    //TODO method that returns addrs where send >5 mins ago
}