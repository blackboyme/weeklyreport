package com.caac.weeklyreport.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PersonalReportVO {


    /**
     * 员工ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 周数
     */
    private Integer week;

    /**
     * 本周装备研发
     */
    private String equip;

    /**
     * 本周系统开发
     */
    private String systemRd;

    /**
     * 本周示范区建设
     */
    private String construction;

    /**
     * 本周其他工作
     */
    private String others;

    /**
     * 下周装备研发
     */
    private String nextEquip;

    /**
     * 下周系统开发
     */
    private String nextSystem;

    /**
     * 下周示范区建设
     */
    private String nextConstruction;

    /**
     * 下周其他工作
     */
    private String nextOthers;
}
