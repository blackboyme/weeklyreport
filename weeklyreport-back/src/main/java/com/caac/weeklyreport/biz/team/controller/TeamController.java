package com.caac.weeklyreport.biz.team.controller;


import com.caac.weeklyreport.common.ResultBean;
import com.caac.weeklyreport.biz.team.entity.Team;
import com.caac.weeklyreport.biz.team.service.ITeamService;
import com.caac.weeklyreport.biz.team.service.impl.TeamServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 团队部门表 前端控制器
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-22
 */
@RestController
@RequestMapping("/api/v1/team")
public class TeamController {

    private final ITeamService teamServiceImpl;

    public TeamController(TeamServiceImpl teamServiceImpl) {
        this.teamServiceImpl = teamServiceImpl;
    }

    @GetMapping("/getAllTeams")
    public ResultBean<?> getAllTeams() {
        List<Team> teams = teamServiceImpl.getAllTeams();
        return ResultBean.success(teams);
    }
}
