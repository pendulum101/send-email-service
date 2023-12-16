package org.scormican.sendemailservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmailDTO {
    @NotBlank
    @NotNull
    private String email;

    private LocalDateTime sendDate;

}