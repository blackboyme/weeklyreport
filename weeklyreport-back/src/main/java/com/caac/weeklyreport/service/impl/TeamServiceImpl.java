package com.caac.weeklyreport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.caac.weeklyreport.entity.Team;
import com.caac.weeklyreport.entity.User;
import com.caac.weeklyreport.mapper.TeamMapper;
import com.caac.weeklyreport.service.ITeamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
}
