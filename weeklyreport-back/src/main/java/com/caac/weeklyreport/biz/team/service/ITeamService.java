package com.caac.weeklyreport.biz.team.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.biz.team.entity.Team;

import java.util.List;

/**
 * <p>
 * 团队部门表 服务类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-22
 */
public interface ITeamService extends IService<Team> {
    List<Team> getAllTeams();
    Team getTeamByTeamId(String teamId);
}
