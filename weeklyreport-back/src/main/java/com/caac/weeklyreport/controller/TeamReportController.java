package com.caac.weeklyreport.controller;

import com.caac.weeklyreport.entity.TeamReport;
import com.caac.weeklyreport.service.ITeamReportService;
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
