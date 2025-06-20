package com.caac.weeklyreport.entity.dto;

import com.caac.weeklyreport.entity.DeptReport;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="部门周报填写返回类", description="部门周报填写返回类")
public class DeptReportWeekDTO {
    DeptReport currentWeekDeptReport;
    DeptReport lastWeekDeptReport;
    String currentStatus;
    Boolean canOperate;
}
