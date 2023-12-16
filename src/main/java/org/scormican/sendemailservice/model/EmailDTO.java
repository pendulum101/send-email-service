package org.scormican.sendemailservice.model;

import java.time.LocalDateTime;

public record EmailDTO(String email, LocalDateTime sendDate) {}