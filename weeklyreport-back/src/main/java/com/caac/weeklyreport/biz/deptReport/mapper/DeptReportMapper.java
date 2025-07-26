package com.caac.weeklyreport.biz.deptReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caac.weeklyreport.biz.deptReport.entity.DeptReport;
import com.caac.weeklyreport.biz.deptReport.entity.dto.DeptReportExcelDTO;
import com.caac.weeklyreport.biz.teamReport.entity.dto.TeamReportExcelDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 部门周报表 Mapper 接口
 * </p>
 *
 * @author hanrenjie
 * @since 2025-07-24
 */
public interface DeptReportMapper extends BaseMapper<DeptReport> {
    @Select("SELECT " +
            "dept_name,start_date, end_date, year, week, summary " +
            "FROM team_report " +
            "WHERE " +
            "dept_id = #{deptId} " +
            "AND year = #{year} " +
            "AND week >= #{startWeek} " +
            "AND week <= #{endWeek} " +
            "ORDER BY week")
    List<DeptReportExcelDTO> getExportDeptReport(@Param("deptId")String deptId,
                                                 @Param("startWeek")int startWeek,
                                                 @Param("endWeek")int endWeek,
                                                 @Param("year")int year);
}
