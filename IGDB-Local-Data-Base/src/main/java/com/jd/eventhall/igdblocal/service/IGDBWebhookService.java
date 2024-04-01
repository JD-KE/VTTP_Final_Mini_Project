package com.jd.eventhall.igdblocal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class IGDBWebhookService {
    
    private RestTemplate template = new RestTemplate();

    @Value("${application.igdb.proxy.url}")
    private String baseUrl;

    @Value("${application.igdb.webhook.baseurl}")
    private String webhookBaseUrl;

    @Value("${application.igdb.proxy.api-key}")
    private String x_api_key;

    public void createGameWebhooks() {
        
        System.out.println("Making game endpoint webhooks first");

        System.out.println("Making Game Create Webhook");

        String gameWebhookUrl = UriComponentsBuilder.fromUriString(baseUrl)
            .pathSegment("games","webhooks").toUriString();

        String webhookCreateUrl = UriComponentsBuilder.fromUriString(webhookBaseUrl)
            .pathSegment("webhook", "game", "create").toUriString();

        MultiValueMap <String, String> form = new LinkedMultiValueMap<>();
        form.add("url", webhookCreateUrl);
        form.add("method", "create");
        form.add("secret", "testing1234");

        RequestEntity<MultiValueMap <String, String>> req = RequestEntity
            .post(gameWebhookUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("x-api-key", x_api_key)
            .body(form);

        ResponseEntity<String> resp = template.exchange(req, String.class);

        System.out.println(resp.getBody());

        try {
            Thread.sleep(500l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Making Game Update Webhook");

        String webhookUpdateUrl = UriComponentsBuilder.fromUriString(webhookBaseUrl)
            .pathSegment("webhook", "game", "update").toUriString();

        form.set("url", webhookUpdateUrl);
        form.set("method", "update");

        req = RequestEntity
            .post(gameWebhookUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("x-api-key", x_api_key)
            .body(form);

        resp = template.exchange(req, String.class);
        System.out.println(resp.getBody());

        try {
            Thread.sleep(500l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Making Game Delete Webhook");

        String webhookDeleteUrl = UriComponentsBuilder.fromUriString(webhookBaseUrl)
            .pathSegment("webhook", "game", "delete").toUriString();

        form.set("url", webhookDeleteUrl);
        form.set("method", "delete");

        req = RequestEntity
            .post(gameWebhookUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("x-api-key", x_api_key)
            .body(form);

        resp = template.exchange(req, String.class);
        System.out.println(resp.getBody());
    }

    public void createCoverWebhooks() {
        
        System.out.println("Making cover endpoint webhooks first");

        System.out.println("Making Cover Create Webhook");

        String coverWebhookUrl = UriComponentsBuilder.fromUriString(baseUrl)
            .pathSegment("covers","webhooks").toUriString();

        String webhookCreateUrl = UriComponentsBuilder.fromUriString(webhookBaseUrl)
            .pathSegment("webhook", "cover", "create").toUriString();

        MultiValueMap <String, String> form = new LinkedMultiValueMap<>();
        form.add("url", webhookCreateUrl);
        form.add("method", "create");
        form.add("secret", "testing1234");

        RequestEntity<MultiValueMap <String, String>> req = RequestEntity
            .post(coverWebhookUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("x-api-key", x_api_key)
            .body(form);

        ResponseEntity<String> resp = template.exchange(req, String.class);

        System.out.println(resp.getBody());

        try {
            Thread.sleep(500l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Making Cover Update Webhook");

        String webhookUpdateUrl = UriComponentsBuilder.fromUriString(webhookBaseUrl)
            .pathSegment("webhook", "cover", "update").toUriString();

        form.set("url", webhookUpdateUrl);
        form.set("method", "update");

        req = RequestEntity
            .post(coverWebhookUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("x-api-key", x_api_key)
            .body(form);

        resp = template.exchange(req, String.class);
        System.out.println(resp.getBody());

        try {
            Thread.sleep(500l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Making Cover Delete Webhook");

        String webhookDeleteUrl = UriComponentsBuilder.fromUriString(webhookBaseUrl)
            .pathSegment("webhook", "cover", "delete").toUriString();

        form.set("url", webhookDeleteUrl);
        form.set("method", "delete");

        req = RequestEntity
            .post(coverWebhookUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("x-api-key", x_api_key)
            .body(form);

        resp = template.exchange(req, String.class);
        System.out.println(resp.getBody());
    }

    public void createReleaseDateWebhooks() {
        
        System.out.println("Making release date endpoint webhooks first");

        System.out.println("Making Release Date Create Webhook");

        String releaseDateWebhookUrl = UriComponentsBuilder.fromUriString(baseUrl)
            .pathSegment("release_dates","webhooks").toUriString();

        String webhookCreateUrl = UriComponentsBuilder.fromUriString(webhookBaseUrl)
            .pathSegment("webhook", "release_date", "create").toUriString();

        MultiValueMap <String, String> form = new LinkedMultiValueMap<>();
        form.add("url", webhookCreateUrl);
        form.add("method", "create");
        form.add("secret", "testing1234");

        RequestEntity<MultiValueMap <String, String>> req = RequestEntity
            .post(releaseDateWebhookUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("x-api-key", x_api_key)
            .body(form);

        ResponseEntity<String> resp = template.exchange(req, String.class);

        System.out.println(resp.getBody());

        try {
            Thread.sleep(500l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Making Release Date Update Webhook");

        String webhookUpdateUrl = UriComponentsBuilder.fromUriString(webhookBaseUrl)
            .pathSegment("webhook", "release_date", "update").toUriString();

        form.set("url", webhookUpdateUrl);
        form.set("method", "update");

        req = RequestEntity
            .post(releaseDateWebhookUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("x-api-key", x_api_key)
            .body(form);

        resp = template.exchange(req, String.class);
        System.out.println(resp.getBody());

        try {
            Thread.sleep(500l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Making Release Date Delete Webhook");

        String webhookDeleteUrl = UriComponentsBuilder.fromUriString(webhookBaseUrl)
            .pathSegment("webhook", "release_date", "delete").toUriString();

        form.set("url", webhookDeleteUrl);
        form.set("method", "delete");

        req = RequestEntity
            .post(releaseDateWebhookUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("x-api-key", x_api_key)
            .body(form);

        resp = template.exchange(req, String.class);
        System.out.println(resp.getBody());
    }

    public void createPlatformWebhooks() {
        
        System.out.println("Making platform endpoint webhooks first");

        System.out.println("Making Platform Create Webhook");

        String platformWebhookUrl = UriComponentsBuilder.fromUriString(baseUrl)
            .pathSegment("platforms","webhooks").toUriString();

        String webhookCreateUrl = UriComponentsBuilder.fromUriString(webhookBaseUrl)
            .pathSegment("webhook", "platform", "create").toUriString();

        MultiValueMap <String, String> form = new LinkedMultiValueMap<>();
        form.add("url", webhookCreateUrl);
        form.add("method", "create");
        form.add("secret", "testing1234");

        RequestEntity<MultiValueMap <String, String>> req = RequestEntity
            .post(platformWebhookUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("x-api-key", x_api_key)
            .body(form);

        ResponseEntity<String> resp = template.exchange(req, String.class);

        System.out.println(resp.getBody());

        try {
            Thread.sleep(500l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Making Platform Update Webhook");

        String webhookUpdateUrl = UriComponentsBuilder.fromUriString(webhookBaseUrl)
            .pathSegment("webhook", "platform", "update").toUriString();

        form.set("url", webhookUpdateUrl);
        form.set("method", "update");

        req = RequestEntity
            .post(platformWebhookUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("x-api-key", x_api_key)
            .body(form);

        resp = template.exchange(req, String.class);
        System.out.println(resp.getBody());

        try {
            Thread.sleep(500l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Making Platform Delete Webhook");

        String webhookDeleteUrl = UriComponentsBuilder.fromUriString(webhookBaseUrl)
            .pathSegment("webhook", "platform", "delete").toUriString();

        form.set("url", webhookDeleteUrl);
        form.set("method", "delete");

        req = RequestEntity
            .post(platformWebhookUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("x-api-key", x_api_key)
            .body(form);

        resp = template.exchange(req, String.class);
        System.out.println(resp.getBody());
    }
}
