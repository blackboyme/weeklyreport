package com.caac.weeklyreport.entity.dto;

import com.caac.weeklyreport.entity.PersonalReport;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="周报填写返回类", description="周报填写返回类")
public class StatusPersonalReportDTO {
    PersonalReport currentWeekPersonalReport;
    PersonalReport lastWeekPersonalReport;
    String currentStatus;
    Boolean canOperate;
}
