package org.scormican.sendemailservice.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.scormican.sendemailservice.model.EmailDTO;
import org.scormican.sendemailservice.services.EmailService;

public class DummyEmailServiceImpl implements EmailService {

    public static final String RECENT_EMAIL = "recent@mail.com";
    public static final String CAN_SEND_AGAIN_EMAIL = "canSendAgain@mail.com";
    private Map<UUID, EmailDTO> emailMap;

    public DummyEmailServiceImpl() {
        this.emailMap = new HashMap<>();

        EmailDTO email1 = EmailDTO.builder()
            .id(UUID.randomUUID())
            .emailAddr(RECENT_EMAIL)
            .version(1)
            .sendDate(LocalDateTime.now())
            .build();

        EmailDTO email2 = EmailDTO.builder()
            .id(UUID.randomUUID())
            .emailAddr(CAN_SEND_AGAIN_EMAIL)
            .version(1)
            .sendDate(LocalDateTime.now().minusMinutes(6))
            .build();

        emailMap.put(email1.getId(), email1);
        emailMap.put(email2.getId(), email2);
    }


    /**
     * @param emailDTO 
     * @return
     */
    @Override
    public List<EmailDTO> sendEmailsAndUpdateDB(List<EmailDTO> emailDTO) {
        return emailMap.values().stream().toList();
    }
}