package edu.project.client;

import edu.project.configuration.ClientConfigurationProperties;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.UUID;

import static java.net.http.HttpClient.newHttpClient;
import static java.net.http.HttpRequest.newBuilder;

@Log4j2
@Component
@EnableConfigurationProperties(ClientConfigurationProperties.class)
public class GigaChatClient {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final long ONE_SECOND = 1000L;
    private static final String ACCEPT = "Accept";
    private static final String AUTHORIZATION = "Authorization";
    private final URI tokenUrl;
    private final URI responseUrl;
    private final String data;
    private GigaChatTokenResponse response;

    public GigaChatClient(ClientConfigurationProperties properties) {
        this.tokenUrl = URI.create(properties.gigaChat().baseUrlToken());
        this.responseUrl = URI.create(properties.gigaChat().baseUrlResponse());
        this.data = properties.gigaChat().data();
    }

    GigaChatTokenResponse getGigaChatToken() {
        try (HttpClient client = newHttpClient()) {
            HttpRequest request = buildHttpRequestForToken();
            String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            JSONObject jsonObject = new JSONObject(response);
            String accessToken = jsonObject.getString("access_token");
            long expiresAt = jsonObject.getLong("expires_at");
            return new GigaChatTokenResponse(accessToken, expiresAt);
        } catch (Exception exception) {
            log.error("Произошла ошибка при попытке получения токена: ", exception);
            return null;
        }
    }

    private HttpRequest buildHttpRequestForToken() {
        String uuid = UUID.randomUUID().toString();
        return newBuilder()
                .uri(tokenUrl)
                .POST(BodyPublishers.ofString("scope=GIGACHAT_API_PERS"))
                .header(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header("RqUID", uuid)
                .header(AUTHORIZATION, "Basic " + data)
                .build();
    }

    public String getResponseAfterSendingMessageToGigaChat(String message) {
        if ((response == null) || ((response.getExpiresAt() - System.currentTimeMillis() - ONE_SECOND) >= 0)) {
            response = getGigaChatToken();
            if (response == null) {
                return "Произошла ошибка при попытке извлечь токен";
            }
        }
        try (HttpClient client = newHttpClient()) {
            HttpRequest request = buildHttpRequestForResponse(message);
            String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray choicesArray = jsonObject.getJSONArray("choices");
            for (int i = 0; i < choicesArray.length(); i++) {
                JSONObject object = choicesArray.getJSONObject(i);
                if (object.has("message")) {
                    JSONObject messageObject = object.getJSONObject("message");
                    return messageObject.getString("content");
                }
            }
            return "GigaChat не смог прислать какое-то сообщение";
        } catch (Exception exception) {
            log.error("Произошла ошибка при попытке получения ответа от GigaChat: ", exception);
            return null;
        }
    }

    private HttpRequest buildHttpRequestForResponse(String message) {
        return newBuilder()
                .uri(responseUrl)
                .POST(BodyPublishers.ofString(constructBodyForRequest(message)))
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer " + response.getAccessToken())
                .build();
    }

    String constructBodyForRequest(String message) {
        String construct = """
                {
                  "model": "GigaChat",
                  "messages": [
                    {
                      "role": "user",
                      "content": "%s"
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
                """;
        return String.format(construct, message);
    }
}
