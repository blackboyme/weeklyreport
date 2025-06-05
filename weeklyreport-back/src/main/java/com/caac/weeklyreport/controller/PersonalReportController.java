package com.caac.weeklyreport.controller;

import com.caac.weeklyreport.entity.PersonalReport;
import com.caac.weeklyreport.service.IPersonalReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 个人周报表 前端控制器
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@RestController
@RequestMapping("/api/v1/personal-report")
public class PersonalReportController {
    
    private final IPersonalReportService personalReportService;

    public PersonalReportController(IPersonalReportService personalReportService) {
        this.personalReportService = personalReportService;
    }

    @PostMapping
    public ResponseEntity<PersonalReport> createPersonalReport(@RequestBody PersonalReport personalReport) {
        return ResponseEntity.ok(personalReportService.createPersonalReport(personalReport));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalReport> getPersonalReport(@PathVariable String id) {
        PersonalReport personalReport = personalReportService.getPersonalReportById(id);
        return personalReport != null ? ResponseEntity.ok(personalReport) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<PersonalReport>> getAllPersonalReports() {
        return ResponseEntity.ok(personalReportService.getAllPersonalReports());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonalReport> updatePersonalReport(@PathVariable String id, @RequestBody PersonalReport personalReport) {
        personalReport.setPrId(id);
        return ResponseEntity.ok(personalReportService.updatePersonalReport(personalReport));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonalReport(@PathVariable String id) {
        personalReportService.deletePersonalReport(id);
        return ResponseEntity.ok().build();
    }
}
