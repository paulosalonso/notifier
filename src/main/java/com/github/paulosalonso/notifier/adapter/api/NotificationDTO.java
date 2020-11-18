package com.github.paulosalonso.notifier.adapter.api;

import com.github.paulosalonso.notifier.domain.NotificationType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NotificationDTO {
    @NotNull
    private NotificationType type;

    private String sender;

    @Singular
    @NotEmpty
    private List<String> recipients;

    private String subject;

    @NotBlank
    private String message;

    @Singular
    private Map<String, String> additionalProperties;
}
