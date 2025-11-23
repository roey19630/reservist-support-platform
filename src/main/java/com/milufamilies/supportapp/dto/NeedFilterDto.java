package com.milufamilies.supportapp.dto;

import com.milufamilies.supportapp.model.enums.*;

import lombok.Data;

@Data
public class NeedFilterDto {
    private HelpType helpType;
    private Region region;
    private DaysAvailable day;
    private TimeSlot time;
}
