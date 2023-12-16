package org.scormican.sendemailservice.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

@Builder
@Data
public class Email {
    @NotBlank
    @NotNull
    private String emailAddr;

    @UpdateTimestamp
    private LocalDateTime sendDate;
}