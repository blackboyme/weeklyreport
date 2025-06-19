package com.caac.weeklyreport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caac.weeklyreport.entity.TeamReport;
import com.caac.weeklyreport.entity.dto.PersonalReportStatusDTO;
import com.caac.weeklyreport.entity.dto.TeamReportStatusDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 团队周报表 Mapper 接口
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@Mapper
public interface TeamReportMapper extends BaseMapper<TeamReport> {

    @Select("SELECT " +
            "tr.*, fr.current_status as status " +
            "FROM team_report tr " +
            "LEFT JOIN flow_record fr on " +
            "fr.flow_id = tr.flow_id " +
            "LEFT JOIN  user u on " +
            "u.user_id = tr.user_id " +
            "WHERE " +
            "YEAR(tr.created_at) = #{year} " +
            "AND tr.week = #{week} " +
            "AND tr.team_id = #{teamId} " +
            "AND fr.current_status = #{status}" +
            "ORDER BY tr.updated_at")
    List<TeamReportStatusDTO> getTeamReportByStatus(@Param("teamId")String teamId, @Param("week")int week,
                                                        @Param("year")int year, @Param("status")String status);


    @Select("SELECT " +
            "tr.*, fr.current_status as status " +
            "FROM team_report tr " +
            "LEFT JOIN flow_record fr on " +
            "fr.flow_id = tr.flow_id " +
            "LEFT JOIN  user u on " +
            "u.user_id = tr.user_id " +
            "WHERE " +
            "YEAR(tr.created_at) = #{year} " +
            "AND fr.current_status != '1'" +
            "AND tr.week = #{week} " +
            "AND tr.team_id = #{teamId} " +
            "ORDER BY tr.updated_at")
    List<TeamReportStatusDTO> getAllTeamReportWithStatus(@Param("teamId")String teamId, @Param("week")int week,
                                                                 @Param("year")int year);

}
