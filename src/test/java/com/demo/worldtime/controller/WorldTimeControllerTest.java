package com.demo.worldtime.controller;

import com.demo.worldtime.dto.TimezoneResponse;
import com.demo.worldtime.service.WorldTimeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class WorldTimeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorldTimeService worldTimeService;

    @BeforeEach
    private void setUp(){
    }
    @Test
    public void getValidUSTime() throws Exception {
        try {
            String timezone = "America/New_York";
            Mockito.when(worldTimeService.isUsTimezone(timezone)).thenReturn(true);
            TimezoneResponse timezoneResponse = TimezoneResponse.builder()
                    .timezone(timezone).abbreviation("EST")
                    .datetime("2023-03-04T03:56:50.200211-05:00").build();
            Mockito.when(worldTimeService.getUSTime(timezone)).thenReturn(timezoneResponse);
            mockMvc.perform(MockMvcRequestBuilders.get("/time")
                            .param("timezone", timezone))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.abbreviation",Matchers.is("EST")))
                    .andExpect(jsonPath("$.datetime",Matchers.is("2023-03-04T03:56:50.200211-05:00")))
                    .andExpect(jsonPath("$.timezone",Matchers.is("America/New_York")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void invalidTimezone() throws Exception {
        String timezone = "Europe/Paris";
        Mockito.when(worldTimeService.isUsTimezone(timezone)).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.get("/time")
                        .param("timezone", timezone))
                .andExpect(status().isBadRequest());
    }
}
