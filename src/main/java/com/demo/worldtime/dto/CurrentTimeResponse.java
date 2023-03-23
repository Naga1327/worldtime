package com.demo.worldtime.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentTimeResponse {
    private String abbreviation;
    private String client_ip;
    private String datetime;
    @JsonProperty("day_of_week")
    private int dayOfWeek;
    @JsonProperty("day_of_year")
    private int dayOfYear;
    private boolean dst;
    private String dst_from;
    private int dst_offset;
    private String dst_until;
    private int raw_offset;
    private String timezone;
    private int unixtime;
    private String utc_datetime;
    private String utc_offset;
    private int week_number;

}
