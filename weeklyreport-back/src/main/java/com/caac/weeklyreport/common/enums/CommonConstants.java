package com.caac.weeklyreport.common.enums;

public class CommonConstants {

    // 周报类型：1-个人周报,2-团队周报,3-部门周报
    public static final String REPORT_TYPE_PERSONAL = "1";
    public static final String REPORT_TYPE_TEAM = "2";
    public static final String REPORT_TYPE_DEPT = "3";

    // 操作类型：1-保存为草稿,2-提交,3-通过,4-退回
    public static final String OPERATION_DRAFT = "1";
    public static final String OPERATION_SUBMIT = "2";
    public static final String OPERATION_PASS = "3";
    public static final String OPERATION_CANCEL = "4";

    // 当前状态：1-草稿，2-待审核，3.-已审核，4-已退回
    public static final String CURRENT_STATUS_DRAFT = "1";
    public static final String CURRENT_STATUS_SUBMIT = "2";
    public static final String CURRENT_STATUS_PASS = "3";
    public static final String CURRENT_STATUS_CANCEL = "4";

    // 当前环节：1-员工提交, 2-团队审核, 3-部门审核
    public static final String CURRENT_STAGE_PERSONAL = "1";
    public static final String CURRENT_STAGE_TEAM = "2";
    public static final String CURRENT_STAGE_DEPT = "3";
}
