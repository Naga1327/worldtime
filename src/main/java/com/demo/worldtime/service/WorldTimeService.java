package com.demo.worldtime.service;

import com.demo.worldtime.dto.CurrentTimeResponse;
import com.demo.worldtime.dto.TimezoneResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class WorldTimeService {
    private final String baseUrl;
    private final RestTemplate restTemplate;

    private List<String> timezoneList;

    public WorldTimeService(@Value("${worldtime.api.host}") String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
        timezoneList = null;
    }

    public TimezoneResponse getUSTime(String timezone) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        String url = baseUrl + "/api/timezone/" + timezone;

        ResponseEntity<CurrentTimeResponse> response = restTemplate
                .exchange(url, HttpMethod.GET,
                        requestEntity, CurrentTimeResponse.class);
        System.out.println(response.getBody());

        CurrentTimeResponse currentTime = response.getBody();
        return TimezoneResponse.builder()
                .abbreviation(currentTime.getAbbreviation())
                .datetime(currentTime.getDatetime())
                .timezone(currentTime.getTimezone()).build();
    }

    public List<String> getTimezones() {
        if (timezoneList == null) {
            timezoneList = Arrays.asList(restTemplate
                    .getForObject(baseUrl + "/api/timezone/America", String[].class));
        }
        return timezoneList;
    }

    public boolean isUsTimezone(final String timezone) {
        List<String> timezoneList = getTimezones();
        return timezoneList.stream()
                .filter(timezoneArg -> timezone.equals(timezoneArg))
                .count() > 0;
    }
}
