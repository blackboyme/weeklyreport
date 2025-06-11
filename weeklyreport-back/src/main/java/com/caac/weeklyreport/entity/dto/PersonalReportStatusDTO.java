package com.caac.weeklyreport.entity.dto;

import com.caac.weeklyreport.entity.PersonalReport;
import lombok.Data;


public class PersonalReportStatusDTO extends PersonalReport {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;
}
