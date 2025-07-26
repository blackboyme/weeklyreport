package com.caac.weeklyreport.biz.personalReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caac.weeklyreport.biz.personalReport.entity.PersonalReport;
import com.caac.weeklyreport.biz.personalReport.entity.dto.PersonalReportExcelDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 个人周报表 Mapper 接口
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@Mapper
public interface PersonalReportMapper extends BaseMapper<PersonalReport> {

    @Select("SELECT " +
            "user_name,dept_name,team_name, start_date, end_date, year, week, major,special_major," +
            "construction,special_construction,others,special_others " +
            "FROM personal_report " +
            "WHERE " +
            "team_id = #{teamId} " +
            "AND year = #{year} " +
            "AND week = #{week} " +
            "ORDER BY updated_at")
    List<PersonalReportExcelDTO> getExportPersonalReport(@Param("teamId")String teamId, @Param("week")int week,
                                                         @Param("year")int year);
}
