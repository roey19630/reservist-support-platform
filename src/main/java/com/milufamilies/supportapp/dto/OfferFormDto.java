package com.milufamilies.supportapp.dto;

import com.milufamilies.supportapp.model.enums.DaysAvailable;
import com.milufamilies.supportapp.model.enums.HelpType;
import com.milufamilies.supportapp.model.enums.Region;
import com.milufamilies.supportapp.model.enums.TimeSlot;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class OfferFormDto {

    @NotNull(message = "יש לבחור סוג עזרה")
    private HelpType helpType;

    @NotBlank(message = "יש להזין תיאור")
    @Size(max = 300, message = "התיאור חייב להיות עד 300 תווים")
    private String description;

    @NotNull(message = "יש לבחור לפחות אזור אחד")
    @Size(min = 1, message = "יש לבחור לפחות אזור אחד")
    private List<Region> regions;

    @NotNull(message = "יש לבחור לפחות יום אחד")
    @Size(min = 1, message = "יש לבחור לפחות יום אחד")
    private List<DaysAvailable> daysAvailable;

    @NotNull(message = "יש לבחור לפחות שעה אחת")
    @Size(min = 1, message = "יש לבחור לפחות שעה אחת")
    private List<TimeSlot> timeSlots;



}
