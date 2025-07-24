package com.caac.weeklyreport.biz.team.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.biz.team.entity.Team;
import com.caac.weeklyreport.biz.team.mapper.TeamMapper;
import com.caac.weeklyreport.biz.team.service.ITeamService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 团队部门表 服务实现类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-22
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements ITeamService {

    private final TeamMapper teamMapper;

    public TeamServiceImpl(TeamMapper teamMapper) {
        this.teamMapper = teamMapper;
    }

    @Override
    public List<Team> getAllTeams() {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", "0");
        return teamMapper.selectList(queryWrapper);
    }

    @Override
    public Team getTeamByTeamId(String teamId) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId)
                .eq("is_deleted", "0");
        return teamMapper.selectOne(queryWrapper);
    }
}
