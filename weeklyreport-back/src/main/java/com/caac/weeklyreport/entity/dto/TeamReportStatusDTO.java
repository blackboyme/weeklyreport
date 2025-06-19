package com.caac.weeklyreport.entity.dto;

import com.caac.weeklyreport.entity.TeamReport;
import lombok.Data;

@Data
public class TeamReportStatusDTO extends TeamReport {
    private String status;
    public String comment;
}
