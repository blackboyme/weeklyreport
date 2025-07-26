package com.caac.weeklyreport.biz.dept.controller;


import com.caac.weeklyreport.biz.dept.entity.Dept;
import com.caac.weeklyreport.biz.dept.service.IDeptService;
import com.caac.weeklyreport.biz.dept.service.impl.DeptServiceImpl;
import com.caac.weeklyreport.biz.team.entity.Team;
import com.caac.weeklyreport.biz.team.service.ITeamService;
import com.caac.weeklyreport.biz.team.service.impl.TeamServiceImpl;
import com.caac.weeklyreport.common.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author hanrenjie
 * @since 2025-07-26
 */
@RestController
@RequestMapping("/dept")
@Api(value = "部门管理",tags="部门管理")
public class DeptController {
    private final IDeptService deptServiceImpl;

    public DeptController(DeptServiceImpl deptServiceImpl) {
        this.deptServiceImpl = deptServiceImpl;
    }

    @ApiOperation("获取所有部门")
    @GetMapping("/getAllDepts")
    public ResultBean<?> getAllDepts() {
        List<Dept> depts = deptServiceImpl.getAllDepts();
        return ResultBean.success(depts);
    }
}
