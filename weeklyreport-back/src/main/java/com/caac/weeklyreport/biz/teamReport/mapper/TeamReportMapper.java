package com.caac.weeklyreport.biz.teamReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caac.weeklyreport.biz.teamReport.entity.TeamReport;
import com.caac.weeklyreport.biz.teamReport.entity.dto.TeamReportExcelDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 团队周报表 Mapper 接口
 * </p>
 *
 * @author hanrenjie
 * @since 2025-07-24
 */
public interface TeamReportMapper extends BaseMapper<TeamReport> {
    @Select("SELECT " +
            "user_name,dept_name,team_name, start_date, end_date, year, week, summary " +
            "FROM team_report " +
            "WHERE " +
            "team_id = #{teamId} " +
            "AND year = #{year} " +
            "AND week >= #{startWeek} " +
            "AND week <= #{endWeek} " +
            "ORDER BY week")
    List<TeamReportExcelDTO> getExportTeamReport(@Param("teamId")String teamId,
                                                 @Param("startWeek")int startWeek,
                                                 @Param("endWeek")int endWeek,
                                                 @Param("year")int year);
}
