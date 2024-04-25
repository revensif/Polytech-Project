package edu.project.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GigaChatTokenResponse {

    private String accessToken;
    private long expiresAt;
}
