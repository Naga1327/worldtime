package com.demo.worldtime.controller;

import com.demo.worldtime.dto.TimezoneResponse;
import com.demo.worldtime.service.WorldTimeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WorldTimeController {
    private final WorldTimeService worldTimeService;
    @GetMapping("/time")
    public ResponseEntity<TimezoneResponse> getUSTime(@RequestParam String timezone) throws JsonProcessingException {
        if(worldTimeService.isUsTimezone(timezone)){
            TimezoneResponse timezoneResponse = worldTimeService.getUSTime(timezone);
            return ResponseEntity.ok(timezoneResponse);
            //return new ResponseEntity<>(timezoneResponse,HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().build();
    }
}
