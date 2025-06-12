package com.caac.weeklyreport.controller;

import com.caac.weeklyreport.common.ResultBean;
import com.caac.weeklyreport.common.ResultCode;
import com.caac.weeklyreport.entity.TeamReport;
import com.caac.weeklyreport.entity.vo.TeamReportVO;
import com.caac.weeklyreport.service.ITeamReportService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 团队周报表 前端控制器
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@RestController
@RequestMapping("/api/v1/team-report")
public class TeamReportController {
    
    private final ITeamReportService teamReportService;

    public TeamReportController(ITeamReportService teamReportService) {
        this.teamReportService = teamReportService;
    }

    @PostMapping
    public ResponseEntity<TeamReport> createTeamReport(@RequestBody TeamReport teamReport) {
        return ResponseEntity.ok(teamReportService.createTeamReport(teamReport));
    }

    @ApiOperation(value = "团队周报填写-获取当前状态和周报数据", notes = "团队周报填写-获取当前状态和周报数据")
    @GetMapping("/getCurrentStatusAndWeeklyReport")
    public ResultBean<?> getCurrentStatusAndWeeklyReport() {
        return ResultBean.success(teamReportService.getCurrentStatusAndWeeklyReport());
    }


    @ApiOperation(value = "团队周报保存为草稿", notes = "团队周报保存为草稿")
    @PostMapping("/saveDraft")
    public ResultBean<?> saveTeamReportDraft(@RequestBody TeamReportVO teamReport) {
        if (StringUtils.isEmpty(teamReport.getTeamId()) || teamReport.getWeek() == null) {
            return ResultBean.fail(ResultCode.PARAM_IS_NULL);
        }
        return ResultBean.success(teamReportService.saveTeamReportDraft(teamReport));
    }

    @ApiOperation(value = "团队周报提交", notes = "团队周报提交")
    @PostMapping("/submit")
    public ResultBean<?> submitPersonalReport(@RequestBody TeamReportVO teamReport) {
        if (StringUtils.isEmpty(teamReport.getTeamId()) || teamReport.getWeek() == null) {
            return ResultBean.fail(ResultCode.PARAM_IS_NULL);
        }
        return ResultBean.success(teamReportService.submitTeamReport(teamReport));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamReport> getTeamReport(@PathVariable String id) {
        TeamReport teamReport = teamReportService.getTeamReportById(id);
        return teamReport != null ? ResponseEntity.ok(teamReport) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<TeamReport>> getAllTeamReports() {
        return ResponseEntity.ok(teamReportService.getAllTeamReports());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamReport> updateTeamReport(@PathVariable String id, @RequestBody TeamReport teamReport) {
        teamReport.setTrId(id);
        return ResponseEntity.ok(teamReportService.updateTeamReport(teamReport));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeamReport(@PathVariable String id) {
        teamReportService.deleteTeamReport(id);
        return ResponseEntity.ok().build();
    }
}
