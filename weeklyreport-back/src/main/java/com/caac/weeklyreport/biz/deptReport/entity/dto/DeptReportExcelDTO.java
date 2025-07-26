package com.caac.weeklyreport.biz.deptReport.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@HeadRowHeight(40)  //表头行高
@ContentRowHeight(40)  //内容行高
@ColumnWidth(25)
@ContentFontStyle(fontHeightInPoints = (short) 12)
@ApiModel(value = "DeptReportExcelDTO", description = "DeptReportExcelDTO")
public class DeptReportExcelDTO {

    @ExcelProperty("所属部门")
    @TableField("dept_name")
    private String deptName;

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

    @ExcelProperty("团队重点工作汇总")
    @TableField("summary")
    private String summary;
}
