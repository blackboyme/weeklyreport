package com.caac.weeklyreport.biz.personalReport.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@HeadRowHeight(40)  //表头行高
@ContentRowHeight(40)  //内容行高
@ColumnWidth(25)
@ContentFontStyle(fontHeightInPoints = (short) 12)
@ApiModel(value = "PersonalReportExcelDTO", description = "PersonalReportExcelDTO")
public class PersonalReportExcelDTO {
    /**
     * 员工名称
     */
    @ExcelProperty("员工姓名")
    @TableField("user_name")
    private String userName;

    @ExcelProperty("所属部门")
    @TableField("dept_name")
    private String deptName;

    @ExcelProperty("所属团队")
    @TableField("team_name")
    private String teamName;

    @ExcelProperty("开始日期")
    @TableField("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime startDate;

    @ExcelProperty("结束日期")
    @TableField("end_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime endDate;

    @ExcelProperty("年份")
    @TableField("year")
    private int year;

    @ExcelProperty("周数")
    @TableField("week")
    private int week;

    @ExcelProperty("团队主线工作")
    @TableField("major")
    private String major;

    @ExcelProperty("特殊情况说明（团队主线工作）")
    @TableField("special_major")
    private String specialMajor;

    @ExcelProperty("示范区建设")
    @TableField("construction")
    private String construction;

    @ExcelProperty("特殊情况说明（示范区建设）")
    @TableField("special_construction")
    private String specialConstruction;

    @ExcelProperty("其他工作")
    @TableField("others")
    private String others;

    @ExcelProperty("特殊情况说明（其他工作）")
    @TableField("special_others")
    private String specialOthers;
}
