package org.scormican.sendemailservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmailDTO {
    private UUID id;
    private Integer version;

    @NotBlank
    @NotNull
    private String emailAddr;

    private LocalDateTime sendDate;

}