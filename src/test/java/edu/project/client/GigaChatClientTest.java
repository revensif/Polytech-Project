package edu.project.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GigaChatClientTest {

    private static final long EXPIRATION_TIME = 1790000;
    private static final int TOKEN_SIZE = 1233;

    @Autowired
    private GigaChatClient client;

    @Test
    void shouldGetGigaChatTokenResponse() {
        GigaChatTokenResponse response = client.getGigaChatToken();
        assertThat(response).isNotNull();
        assertThat(response.getExpiresAt()).isGreaterThan(System.currentTimeMillis() + EXPIRATION_TIME);
        assertThat(response.getAccessToken().length()).isEqualTo(TOKEN_SIZE);
    }

    @Test
    void shouldConstructBodyForRequest() {
        assertThat(client.constructBodyForRequest("test")).isEqualTo("""
                {
                  "model": "GigaChat",
                  "messages": [
                    {
                      "role": "user",
                      "content": "test"
                    }
                  ],
                  "temperature": 1,
                  "top_p": 0.1,
                  "n": 1,
                  "stream": false,
                  "max_tokens": 50,
                  "repetition_penalty": 1,
                  "update_interval": 0
                }
                """);
    }
}
