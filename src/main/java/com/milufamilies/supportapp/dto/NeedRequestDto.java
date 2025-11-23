package com.milufamilies.supportapp.dto;

import com.milufamilies.supportapp.model.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class NeedRequestDto {

    @NotNull(message = "יש לבחור סוג עזרה")
    private HelpType helpType;

    @NotNull(message = "יש לבחור לפחות אזור אחד")
    @Size(min = 1, message = "יש לבחור לפחות אזור אחד")
    private List<Region> regions;

    @NotNull(message = "יש לבחור לפחות יום אחד")
    @Size(min = 1, message = "יש לבחור לפחות יום אחד")
    private List<DaysAvailable> daysAvailable;

    @NotNull(message = "יש לבחור לפחות שעה אחת")
    @Size(min = 1, message = "יש לבחור לפחות שעה אחת")
    private List<TimeSlot> timeSlots;

    @NotBlank(message = "יש להזין תיאור")
    @Size(max = 500, message = "התיאור חייב להיות עד 500 תווים")
    private String description;


    private String preferredResponseTime;


}
