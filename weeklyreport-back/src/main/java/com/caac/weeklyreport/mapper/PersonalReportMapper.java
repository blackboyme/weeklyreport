package com.caac.weeklyreport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caac.weeklyreport.entity.PersonalReport;
import com.caac.weeklyreport.entity.dto.PersonalReportExcelDTO;
import com.caac.weeklyreport.entity.dto.PersonalReportStatusDTO;
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
            "pr.*, fr.current_status as status " +
            "FROM personal_report pr " +
            "LEFT JOIN flow_record fr on " +
            "fr.flow_id = pr.flow_id " +
            "LEFT JOIN  user u on " +
            "u.user_id = pr.user_id " +
            "WHERE " +
            "YEAR(pr.created_at) = #{year} " +
            "AND pr.week = #{week} " +
            "AND pr.team_id = #{teamId} " +
            "AND fr.current_status = #{status}" +
            "ORDER BY pr.updated_at")
    List<PersonalReportStatusDTO> getPersonalReportByStatus(@Param("teamId")String teamId, @Param("week")int week,
                                                            @Param("year")int year, @Param("status")String status);


    @Select("SELECT " +
            "pr.*, fr.current_status as status " +
            "FROM personal_report pr " +
            "LEFT JOIN flow_record fr on " +
            "fr.flow_id = pr.flow_id " +
            "LEFT JOIN  user u on " +
            "u.user_id = pr.user_id " +
            "WHERE " +
            "YEAR(pr.created_at) = #{year} " +
            "AND fr.current_status != '1'" +
            "AND pr.week = #{week} " +
            "AND pr.team_id = #{teamId} " +
            "ORDER BY pr.updated_at")
    List<PersonalReportStatusDTO> getAllPersonalReportWithStatus(@Param("teamId")String teamId, @Param("week")int week,
                                                            @Param("year")int year);

    @Select("SELECT " +
            "pr.user_name as userName,pr.equip as equip,pr.system_rd as systemRd,pr.construction as construction," +
            "pr.others as others,pr.next_equip as nextEquip,pr.next_system as nextSystem,pr.next_construction as nextConstruction," +
            "pr.next_others as nextOthers,pr.special as special " +
            "FROM personal_report pr " +
            "LEFT JOIN flow_record fr on " +
            "fr.flow_id = pr.flow_id " +
            "LEFT JOIN  user u on " +
            "u.user_id = pr.user_id " +
            "WHERE " +
            "YEAR(pr.created_at) = #{year} " +
            "AND fr.current_status != '1'" +
            "AND pr.week = #{week} " +
            "AND pr.team_id = #{teamId} " +
            "ORDER BY pr.updated_at")
    List<PersonalReportExcelDTO> getExportPersonalReport(@Param("teamId")String teamId, @Param("week")int week,
                                                         @Param("year")int year);

}
