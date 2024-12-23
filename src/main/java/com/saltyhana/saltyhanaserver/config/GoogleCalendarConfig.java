package com.saltyhana.saltyhanaserver.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Collections;

@Configuration
public class GoogleCalendarConfig {

  @Value("${google.calendar.client-id}")
  private String clientId;

  @Value("${google.calendar.client-secret}")
  private String clientSecret;

  @Value("${google.calendar.auth-uri}")
  private String authUri;

  @Value("${google.calendar.token-uri}")
  private String tokenUri;

  @Value("${google.calendar.redirect-uris}")
  private String redirectUri;

  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

  @Bean
  public GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow() throws IOException {
    GoogleClientSecrets clientSecrets = new GoogleClientSecrets()
            .setInstalled(new GoogleClientSecrets.Details()
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setAuthUri(authUri)
                    .setTokenUri(tokenUri)
                    .setRedirectUris(Collections.singletonList(redirectUri)));

    return new GoogleAuthorizationCodeFlow.Builder(
            new NetHttpTransport(),
            JSON_FACTORY,
            clientSecrets,
            Collections.singletonList("https://www.googleapis.com/auth/calendar"))
            .setAccessType("offline")
            .build();
  }

  @Bean
  public Credential googleCredential(GoogleAuthorizationCodeFlow flow) {
    return new Credential.Builder(flow.getMethod())
            .setJsonFactory(JSON_FACTORY)
            .setTransport(new NetHttpTransport())
            .setClientAuthentication(flow.getClientAuthentication())
            .setTokenServerUrl(new GenericUrl(flow.getTokenServerEncodedUrl()))
            .build();
  }
}
