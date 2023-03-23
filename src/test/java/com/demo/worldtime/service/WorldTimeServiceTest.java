package com.demo.worldtime.service;

import com.demo.worldtime.dto.CurrentTimeResponse;
import com.demo.worldtime.dto.TimezoneResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class WorldTimeServiceTest {
 
    @MockBean
    private RestTemplate restTemplate;

    @Value("${worldtime.api.host}")
    private String baseUrl;
    @Autowired
    private WorldTimeService worldTimeService ;

    private String[] timezoneArr ;

    @BeforeEach
    private void setUp(){
        timezoneArr =new String[]{"Europe/Rome","Europe/Samara",
                "Europe/Saratov", "Europe/Paris", "America/New_York",
                "America/Menominee", "America/Los_Angeles"};
    }
    @Test
    public void testValidUSTimezone(){
        String timezone = "America/Menominee";

        Mockito.when(restTemplate.getForObject(baseUrl  + "/api/timezone/America"
                ,String[].class)).thenReturn(timezoneArr);
        Assertions.assertTrue(worldTimeService.isUsTimezone(timezone));
    }

    @Test
    public void testToGetUSTimezone() throws JsonProcessingException {
        String timezone = "America/Menominee";
        CurrentTimeResponse currentTimeResponse = readJsonFile();
        Mockito.when(restTemplate.exchange(Mockito.anyString(),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.any(HttpEntity.class),
                        Mockito.eq(CurrentTimeResponse.class)))
                .thenReturn(new ResponseEntity<>(currentTimeResponse, HttpStatus.ACCEPTED));
        TimezoneResponse timezoneResponse = worldTimeService.getUSTime(timezone);
        TimezoneResponse timezoneResponseExpected = TimezoneResponse.builder()
                        .timezone("America/Menominee").abbreviation("CST")
                        .datetime("2023-03-04T04:39:08.892915-06:00").build();
        Assertions.assertEquals(timezoneResponseExpected,timezoneResponse);
    }

    private CurrentTimeResponse readJsonFile(){
        File file = new File(
                this.getClass().getClassLoader().getResource("ustTimeSample.json").getFile()
        );
        CurrentTimeResponse currentTimeResponse ;
        ObjectMapper mapper = new ObjectMapper();
        try {
            currentTimeResponse = mapper.readValue(file, CurrentTimeResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return currentTimeResponse;
    }
}
