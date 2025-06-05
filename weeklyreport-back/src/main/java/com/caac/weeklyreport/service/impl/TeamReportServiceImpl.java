package com.caac.weeklyreport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.entity.TeamReport;
import com.caac.weeklyreport.mapper.TeamReportMapper;
import com.caac.weeklyreport.service.ITeamReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 团队周报表 服务实现类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@Service
public class TeamReportServiceImpl extends ServiceImpl<TeamReportMapper, TeamReport> implements ITeamReportService {

    private final TeamReportMapper teamReportMapper;

    public TeamReportServiceImpl(TeamReportMapper teamReportMapper) {
        this.teamReportMapper = teamReportMapper;
    }

    @Override
    public TeamReport createTeamReport(TeamReport teamReport) {
        teamReport.setTrId(UUID.randomUUID().toString());
        teamReport.setCreatedAt(LocalDateTime.now());
        teamReport.setUpdatedAt(LocalDateTime.now());
        teamReport.setIsDeleted("0");
        teamReportMapper.insert(teamReport);
        return getTeamReportById(teamReport.getTrId());
    }

    @Override
    public TeamReport getTeamReportById(String id) {
        return teamReportMapper.selectById(id);
    }

    @Override
    public List<TeamReport> getAllTeamReports() {
        QueryWrapper<TeamReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", "0");
        return teamReportMapper.selectList(queryWrapper);
    }

    @Override
    public TeamReport updateTeamReport(TeamReport teamReport) {
        teamReport.setUpdatedAt(LocalDateTime.now());
        teamReportMapper.updateById(teamReport);
        return getTeamReportById(teamReport.getTrId());
    }

    @Override
    public void deleteTeamReport(String id) {
        TeamReport teamReport = new TeamReport();
        teamReport.setTrId(id);
        teamReport.setIsDeleted("1");
        teamReport.setUpdatedAt(LocalDateTime.now());
        teamReportMapper.updateById(teamReport);
    }
}
