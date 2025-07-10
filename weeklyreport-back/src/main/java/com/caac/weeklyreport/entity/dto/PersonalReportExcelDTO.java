package com.caac.weeklyreport.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@HeadRowHeight(30)  //表头行高
@ContentRowHeight(40)  //内容行高
@ColumnWidth(25)
@ContentFontStyle(fontHeightInPoints = (short) 12)
@ApiModel(value = "PersonalReportExcelDTO", description = "PersonalReportExcelDTO")
public class PersonalReportExcelDTO {

    /**
     * 员工名称
     */
    @ExcelProperty("员工姓名")
    private String userName;

    /**
     * 本周装备研发
     */
    @ExcelProperty("本周装备研发")
    private String equip;

    /**
     * 本周系统开发
     */
    @ExcelProperty("本周系统开发")
    private String systemRd;

    /**
     * 本周示范区建设
     */
    @ExcelProperty("本周示范区建设")
    private String construction;

    /**
     * 本周其他工作
     */
    @ExcelProperty("本周其他工作")
    private String others;

    /**
     * 下周装备研发
     */
    @ExcelProperty("下周装备研发")
    private String nextEquip;

    /**
     * 下周系统开发
     */
    @ExcelProperty("下周系统开发")
    private String nextSystem;

    /**
     * 下周示范区建设
     */
    @ExcelProperty("下周示范区建设")
    private String nextConstruction;

    /**
     * 下周其他工作
     */
    @ExcelProperty("下周其他工作")
    private String nextOthers;

    /**
     * 特殊说明
     */
    @ExcelProperty("特殊说明")
    private String special;

}
