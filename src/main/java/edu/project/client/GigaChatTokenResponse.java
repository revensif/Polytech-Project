package edu.project.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GigaChatTokenResponse {

    private String accessToken;
    private long expiresAt;
}
