package com.caac.weeklyreport.entity.dto;

import com.caac.weeklyreport.entity.TeamReport;

public class TeamReportStatusDTO extends TeamReport {
    private String status;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
