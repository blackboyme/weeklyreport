package com.caac.weeklyreport.controller;

import com.caac.weeklyreport.common.ResultBean;
import com.caac.weeklyreport.common.ResultCode;
import com.caac.weeklyreport.entity.PersonalReport;
import com.caac.weeklyreport.entity.vo.PersonalReportVO;
import com.caac.weeklyreport.service.IPersonalReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
@Api(value = "个人周报管理", tags = "个人周报管理")
public class PersonalReportController {
    
    private final IPersonalReportService personalReportService;

    public PersonalReportController(IPersonalReportService personalReportService) {
        this.personalReportService = personalReportService;
    }

    @PostMapping
    public ResponseEntity<PersonalReport> createPersonalReport(@RequestBody PersonalReport personalReport) {
        return ResponseEntity.ok(personalReportService.createPersonalReport(personalReport));
    }
//    /**
//     * zjy  员工周报填写模块-onload校验
//     * 判断是否可以打开
//     * 无入参，后端通过header判断用户
//     * */
//    @ApiOperation(value = "员工周报填写前置检查", notes = "员工周报填写前置检查")
//    @GetMapping("/frontCheck/{userId}/{week}")
//    public ResultBean<?> frontPersonalReportCheck(@PathVariable String userId, @PathVariable int week) {
//       // 判断是否有暂存单
//        ResultBean<PersonalReport> reportCheck = personalReportService.frontPersonalReportCheck(userId, week);
//        return ResultBean.success(reportCheck);
//    }
//    /**
//     * zjy
//     * */
//    @ApiOperation(value = "员工周报填写-前一周数据获取", notes = "员工周报填写-前一周数据获取")
//    @GetMapping("/lastWeekData/{userId}/{week}")
//    public ResultBean<?> getLastWeekData(@PathVariable String userId,@PathVariable int week) {
//        //PersonalReport中没有字段区分是否是草稿
//        return ResultBean.success(personalReportService.getDraftByUserIdAndWeek(userId,week));
//    }


    @ApiOperation(value = "员工周报填写-获取当前状态和周报数据", notes = "员工周报填写-获取当前状态和周报数据")
    @GetMapping("/getCurrentStatusAndWeeklyReport")
    public ResultBean<?> getCurrentStatusAndWeeklyReport() {
        return ResultBean.success(personalReportService.getCurrentStatusAndWeeklyReport());
    }

    /**
     * zjy
     * */
    @ApiOperation(value = "团队领导查询所有团队成员周报", notes = "团队领导查询所有团队成员周报")
    @GetMapping("/approveList/{userId}/{week}")
    public ResultBean<?> getAllStatusPersonalReportForWeek(@PathVariable String userId, @PathVariable int week) {
        //PersonalReport中没有字段区分是否是草稿
        List<PersonalReport> allPersonalReportsForWeek = personalReportService.getAllPersonalReportsForWeek(userId, 2025, week);
        return ResultBean.success(allPersonalReportsForWeek);
    }


    @ApiOperation(value = "保存为草稿", notes = "保存为草稿")
    @PostMapping("/saveDraft")
    public ResultBean<?> savePersonalReportDraft(@RequestBody PersonalReportVO personalReport) {
        if (StringUtils.isEmpty(personalReport.getUserId()) || personalReport.getWeek() == null) {
            return ResultBean.fail(ResultCode.PARAM_IS_NULL);
        }
        return ResultBean.success(personalReportService.savePersonalReportDraft(personalReport));
    }

    @ApiOperation(value = "提交", notes = "提交")
    @PostMapping("/submit")
    public ResultBean<?> submitPersonalReport(@RequestBody PersonalReportVO personalReport) {
        if (StringUtils.isEmpty(personalReport.getUserId()) || personalReport.getWeek() == null) {
            return ResultBean.fail(ResultCode.PARAM_IS_NULL);
        }
        return ResultBean.success(personalReportService.submitPersonalReport(personalReport));
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
