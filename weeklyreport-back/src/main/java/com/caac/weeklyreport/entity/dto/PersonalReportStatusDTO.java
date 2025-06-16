package com.caac.weeklyreport.entity.dto;

import com.caac.weeklyreport.entity.PersonalReport;
import lombok.Data;

@Data
public class PersonalReportStatusDTO extends PersonalReport {
    private String status;
    public String comment;
}
