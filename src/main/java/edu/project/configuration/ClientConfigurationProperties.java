package edu.project.configuration;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "client", ignoreUnknownFields = false)
public record ClientConfigurationProperties(@NotNull GigaChat gigaChat) {

    public record GigaChat(
            @NotEmpty
            String baseUrlToken,
            @NotEmpty
            String baseUrlResponse,
            @NotEmpty
            String clientSecret,
            @NotEmpty
            String data
    ) {
    }
}
