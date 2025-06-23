package com.caac.weeklyreport.controller;


import com.caac.weeklyreport.common.ResultBean;
import com.caac.weeklyreport.entity.Team;
import com.caac.weeklyreport.entity.User;
import com.caac.weeklyreport.service.ITeamService;
import com.caac.weeklyreport.service.impl.TeamServiceImpl;
import org.springframework.http.ResponseEntity;
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
