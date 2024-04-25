package edu.project.client;

import edu.project.configuration.ClientConfigurationProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GigaChatClientTest {

    @Autowired
    private GigaChatClient client;

    @Autowired
    private ClientConfigurationProperties properties;

    @Test
    void shouldGetGigaChatTokenResponse() {
        GigaChatTokenResponse response = client.getGigaChatToken();
        assertThat(response).isNotNull();
        assertThat(response.getExpiresAt()).isGreaterThan(System.currentTimeMillis() + 1790000);
        assertThat(response.getAccessToken().length()).isEqualTo(1233);
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
