package org.scormican.sendemailservice.mappers;

import org.mapstruct.Mapper;
import org.scormican.sendemailservice.entities.Email;
import org.scormican.sendemailservice.model.EmailDTO;

@Mapper
public interface EmailMapper {
    Email emailDtoToEmail(EmailDTO dto);

    EmailDTO emailToEmailDto(Email email);
}