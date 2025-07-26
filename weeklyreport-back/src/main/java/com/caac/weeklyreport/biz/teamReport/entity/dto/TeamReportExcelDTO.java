package com.caac.weeklyreport.biz.teamReport.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@HeadRowHeight(40)  //表头行高
@ContentRowHeight(40)  //内容行高
@ColumnWidth(25)
@ContentFontStyle(fontHeightInPoints = (short) 12)
@ApiModel(value = "TeamReportExcelDTO", description = "TeamReportExcelDTO")
public class TeamReportExcelDTO {
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
    private LocalDateTime startDate;

    @ExcelProperty("结束日期")
    @TableField("end_date")
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
