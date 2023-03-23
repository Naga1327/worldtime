package com.demo.worldtime.controller;

import com.demo.worldtime.WorldTimeApplication;
import com.demo.worldtime.dto.CurrentTimeResponse;
import com.demo.worldtime.dto.TimezoneResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WorldTimeApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorldTimeControllerIntegrationTest {
    @LocalServerPort
    private int port;
    TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Test
    public void testRetrieveTime(){
        String url =createURLWithPort("/time");
        URI uri =UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("timezone", "America/Menominee")
                .build()
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<TimezoneResponse> responseEntity = testRestTemplate.exchange(uri, HttpMethod.GET,entity,TimezoneResponse.class);

        TimezoneResponse expectedResponseObj = readJsonFile();
        //Assertions.assertTrue(expectedResponseObj.equals(responseEntity.getBody()));
        TimezoneResponse timezoneResponse = responseEntity.getBody();
        //Assertions.assertTrue(expectedResponseObj.getAbbreviation().equals(timezoneResponse.getAbbreviation()));
        Assertions.assertTrue(expectedResponseObj.getTimezone().equals(timezoneResponse.getTimezone()));

    }

    @Test
    public void testInvalidTimezone(){
        String url =createURLWithPort("/time");
        URI uri =UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("timezone", "America")
                .build()
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<TimezoneResponse> responseEntity = testRestTemplate.exchange(uri, HttpMethod.GET,entity,TimezoneResponse.class);

        Assertions.assertTrue(responseEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST));

    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private TimezoneResponse readJsonFile(){
        File file = new File(
                this.getClass().getClassLoader().getResource("timeSampleResponse.json").getFile()
        );
        TimezoneResponse timezoneResponse ;
        ObjectMapper mapper = new ObjectMapper();
        try {
            timezoneResponse = mapper.readValue(file, TimezoneResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return timezoneResponse;
    }
}
