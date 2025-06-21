package com.caac.weeklyreport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.common.ResultCode;
import com.caac.weeklyreport.common.enums.CommonConstants;
import com.caac.weeklyreport.entity.*;
import com.caac.weeklyreport.entity.dto.DeptReportWeekDTO;
import com.caac.weeklyreport.entity.dto.PersonalReportStatusDTO;
import com.caac.weeklyreport.entity.dto.TeamReportStatusDTO;
import com.caac.weeklyreport.entity.dto.TeamReportWeekDTO;
import com.caac.weeklyreport.entity.vo.DeptReportVO;
import com.caac.weeklyreport.exception.BusinessException;
import com.caac.weeklyreport.mapper.DeptReportMapper;
import com.caac.weeklyreport.mapper.FlowHistoryMapper;
import com.caac.weeklyreport.mapper.FlowRecordMapper;
import com.caac.weeklyreport.service.IDeptReportService;
import com.caac.weeklyreport.util.KeyGeneratorUtil;
import com.caac.weeklyreport.util.UserContext;
import com.caac.weeklyreport.util.WeekDateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 部门周报表 服务实现类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@Service
public class DeptReportServiceImpl extends ServiceImpl<DeptReportMapper, DeptReport> implements IDeptReportService {

    private final DeptReportMapper deptReportMapper;

    @Autowired
    private FlowHistoryMapper flowHistoryMapper;

    @Autowired
    private FlowRecordMapper flowRecordMapper;

    public DeptReportServiceImpl(DeptReportMapper deptReportMapper) {
        this.deptReportMapper = deptReportMapper;
    }

    @Override
    public DeptReport createDeptReport(DeptReport deptReport) {
        deptReport.setDrId(UUID.randomUUID().toString());
        deptReport.setCreatedAt(LocalDateTime.now());
        deptReport.setUpdatedAt(LocalDateTime.now());
        deptReport.setIsDeleted("0");
        deptReportMapper.insert(deptReport);
        return getDeptReportById(deptReport.getDrId());
    }

    @Override
    @Transactional
    public DeptReport saveDeptReportDraft(DeptReportVO deptReportVO) {
        // 不能修改其他人的周报
        UserInfo userInfo = UserContext.getCurrentUser();
        //userInfo中缺少部门ID deptId
//        if(!userInfo.get().equals(deptReportVO.getDeptId())){
//            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
//        }

        DeptReport existingDraft = getDeptDraftByUserIdAndWeek(deptReportVO.getDeptId(), deptReportVO.getWeek(), LocalDate.now().getYear());

        if (existingDraft != null) {
            FlowRecord flowRecord = flowRecordMapper.selectById(existingDraft.getFlowId());
            if (flowRecord == null) {
                throw new BusinessException(ResultCode.FLOW_IS_NULL);
            }

            // 当前状态为已通过审批，不能修改
//            if (CommonConstants.CURRENT_STATUS_PASS.equals(flowRecord.getCurrentStatus())) {
//                throw new BusinessException(ResultCode.FLOW_ACCESS_DENY_PASS);
//            } else if (CommonConstants.CURRENT_STATUS_SUBMIT.equals(flowRecord.getCurrentStatus())) {
//                throw new BusinessException(ResultCode.FLOW_ACCESS_DENY_SUBMIT);
//            }

            BeanUtils.copyProperties(deptReportVO, existingDraft);
            existingDraft.setUpdatedAt(LocalDateTime.now());
            deptReportMapper.updateById(existingDraft);

            //创建新历史记录
            FlowHistory flowHistory = new FlowHistory();
            flowHistory.setHistoryId(KeyGeneratorUtil.generateUUID());
            flowHistory.setFlowId(existingDraft.getFlowId());
            flowHistory.setReportId(existingDraft.getDrId());
            flowHistory.setReportType(CommonConstants.REPORT_TYPE_DEPT);//1-个人周报,2-团队周报,3-部门周报
            flowHistory.setOperation(CommonConstants.OPERATION_DRAFT);//1-保存为草稿,2-提交,3-通过,4-退回
            flowHistory.setOperatorId(userInfo.getUserId());
            flowHistory.setOperatorName(userInfo.getUserName());
            flowHistory.setOperatorRole(userInfo.getRoleId());
            flowHistory.setCurrentStage(CommonConstants.CURRENT_STAGE_DEPT);
            flowHistory.setIsDeleted("0");

            flowHistoryMapper.insert(flowHistory);

            return getDeptReportById(existingDraft.getDrId());
        } else {
            String flowId = KeyGeneratorUtil.generateUUID();
            String drId = KeyGeneratorUtil.generateUUID();

            DeptReport deptReport = new DeptReport();
            BeanUtils.copyProperties(deptReportVO, deptReport);
            // 创建新草稿
            deptReport.setDrId(drId);
            deptReport.setUserId(userInfo.getUserId());
            deptReport.setFlowId(flowId);
            deptReport.setUserName(userInfo.getUserName());
            deptReport.setTeamId(userInfo.getTeamId());
            deptReport.setTeamName(userInfo.getTeamName());
            deptReport.setDeptId(userInfo.getDeptId());
            deptReport.setDeptName(userInfo.getDeptName());
            deptReport.setStartDate(WeekDateUtils.getStartDateOfWeek(deptReport.getWeek()));
            deptReport.setEndDate(WeekDateUtils.getEndDateOfWeek(deptReport.getWeek()));
            deptReport.setCreatedAt(LocalDateTime.now());
            deptReport.setUpdatedAt(LocalDateTime.now());
            deptReport.setIsDeleted("0");
            deptReportMapper.insert(deptReport);

            //创建新流程
            FlowRecord flowRecord = new FlowRecord();
            flowRecord.setFlowId(flowId);
            flowRecord.setReportId(drId);
            flowRecord.setReportType(CommonConstants.REPORT_TYPE_DEPT); //1-个人周报,2-团队周报,3-部门周报
            flowRecord.setCurrentStatus(CommonConstants.CURRENT_STATUS_DRAFT); //1-草稿，2-待审核，3.-已审核，4-已退回
            flowRecord.setCurrentStage(CommonConstants.CURRENT_STAGE_DEPT); //1-员工提交,2-团队审核,3-部门审核
            flowRecord.setSubmitterId(userInfo.getUserId());
            flowRecord.setSubmitterName(userInfo.getUserName());
            flowRecord.setIsDeleted("0");
            flowRecordMapper.insert(flowRecord);

            //创建新历史记录
            FlowHistory flowHistory = new FlowHistory();
            flowHistory.setHistoryId(KeyGeneratorUtil.generateUUID());
            flowHistory.setFlowId(flowId);
            flowHistory.setReportId(drId);
            flowHistory.setReportType(CommonConstants.REPORT_TYPE_DEPT);//1-个人周报,2-团队周报,3-部门周报
            flowHistory.setOperation(CommonConstants.OPERATION_DRAFT);//1-保存为草稿,2-提交,3-通过,4-退回
            flowHistory.setOperatorId(userInfo.getUserId());
            flowHistory.setOperatorName(userInfo.getUserName());
            flowHistory.setOperatorRole(userInfo.getRoleId());
            flowHistory.setCurrentStage(CommonConstants.CURRENT_STAGE_DEPT);
            flowHistory.setIsDeleted("0");
            flowHistoryMapper.insert(flowHistory);

            return deptReport;
        }
    }
    @Override
    public DeptReportWeekDTO getCurrentStatusAndDeptReport() {
        DeptReportWeekDTO deptReportWeekDTO = new DeptReportWeekDTO();
        UserInfo userInfo = UserContext.getCurrentUser();
        int currentWeek =  WeekDateUtils.getCurrentWeekNumber();
        DeptReport currentDeptReport = getDeptDraftByUserIdAndWeek(userInfo.getDeptId(), currentWeek, LocalDate.now().getYear());
        // 没有暂存团队周报
        if(currentDeptReport == null){
            deptReportWeekDTO.setCurrentStatus(CommonConstants.CURRENT_STATUS_DRAFT);
            deptReportWeekDTO.setCurrentWeekDeptReport(currentDeptReport);
        } else {// 有暂存或以提交的团队周报
            FlowRecord flowRecord = flowRecordMapper.selectById(currentDeptReport.getFlowId());
            if(flowRecord == null){
                throw new BusinessException(ResultCode.FLOW_IS_NULL);
            }
            deptReportWeekDTO.setCurrentStatus(flowRecord.getCurrentStatus());
            deptReportWeekDTO.setCurrentWeekDeptReport(currentDeptReport);
        }

        // 当前状态为已通过审批、已提交，前端不允许用户打开
        if (CommonConstants.CURRENT_STATUS_PASS.equals(deptReportWeekDTO.getCurrentStatus())
                || CommonConstants.CURRENT_STATUS_SUBMIT.equals(deptReportWeekDTO.getCurrentStatus())) {
            deptReportWeekDTO.setCanOperate(Boolean.FALSE);
        } else {
            deptReportWeekDTO.setCanOperate(Boolean.TRUE);
        }
        // 封装拼接数据
        List<TeamReportStatusDTO>  teamReports = deptReportMapper.getTeamReportByStatus(userInfo.getDeptId(),
                WeekDateUtils.getCurrentWeekNumber(),LocalDate.now().getYear(),"3");
        StringBuilder equip =  new StringBuilder();
        StringBuilder systemRd = new StringBuilder();
        StringBuilder construction = new StringBuilder();
        StringBuilder others = new StringBuilder();
        StringBuilder nextEquip = new StringBuilder();
        StringBuilder nextSystem = new StringBuilder();
        StringBuilder nextConstruction = new StringBuilder();
        StringBuilder nextOthers = new StringBuilder();

        if(!teamReports.isEmpty()) {
            for (int i = 0; i < teamReports.size(); i++) {
                TeamReport report = teamReports.get(i);
                String userNamePart = " (" + report.getUserName() + ")";
                boolean isLast = (teamReports.size() == 1 || i == teamReports.size() - 1);
                String separator = isLast ? "" : System.lineSeparator();

                appendIfNotEmpty(equip, report.getEquip(), userNamePart, separator);
                appendIfNotEmpty(systemRd, report.getSystemRd(), userNamePart, separator);
                appendIfNotEmpty(construction, report.getConstruction(), userNamePart, separator);
                appendIfNotEmpty(others, report.getOthers(), userNamePart, separator);
                appendIfNotEmpty(nextEquip, report.getNextEquip(), userNamePart, separator);
                appendIfNotEmpty(nextSystem, report.getNextSystem(), userNamePart, separator);
                appendIfNotEmpty(nextConstruction, report.getNextConstruction(), userNamePart, separator);
                appendIfNotEmpty(nextOthers, report.getNextOthers(), userNamePart, separator);
            }

            DeptReport lastWeekDeptReport = new DeptReport();
            lastWeekDeptReport.setEquip(equip.toString());
            lastWeekDeptReport.setSystemRd(systemRd.toString());
            lastWeekDeptReport.setConstruction(construction.toString());
            lastWeekDeptReport.setOthers(others.toString());
            lastWeekDeptReport.setNextEquip(nextEquip.toString());
            lastWeekDeptReport.setNextSystem(nextSystem.toString());
            lastWeekDeptReport.setNextConstruction(nextConstruction.toString());
            lastWeekDeptReport.setNextOthers(nextOthers.toString());
            deptReportWeekDTO.setLastWeekDeptReport(lastWeekDeptReport);
        }


//        TeamReport lastWeekTeamReport = null;
//        if (currentWeek == 1) {
//            int lastYearTotalWeek = WeekDateUtils.getTotalWeeksInYear(LocalDate.now().getYear()-1);
//            lastWeekTeamReport = getTeamDraftByUserIdAndWeek(userInfo.getUserId(), lastYearTotalWeek, LocalDate.now().getYear()-1);
//        } else {
//            lastWeekTeamReport = getTeamDraftByUserIdAndWeek(userInfo.getUserId(), currentWeek-1, LocalDate.now().getYear());
//        }
//        teamReportWeekDTO.setLastWeekTeamReport(lastWeekTeamReport);

        return deptReportWeekDTO;
    }

    // 辅助方法
    private void appendIfNotEmpty(StringBuilder builder, String value, String suffix, String separator) {
        if (value != null && !value.trim().isEmpty()) {
            builder.append(value).append(suffix).append(separator);
        }
    }


    @Override
    public DeptReport getWeeklyReportByTime(int year, int week) {
        UserInfo userInfo = UserContext.getCurrentUser();
        if(!"3".equals(userInfo.getRoleType())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }
        DeptReport deptReport = getDeptDraftByUserIdAndWeek(userInfo.getUserId(), week,year);
        if(deptReport == null){
            throw new BusinessException(ResultCode.REPORT_IS_NULL);
        }
        return deptReport;
    }

    @Override
    public DeptReport getDeptReportById(String id) {
        return deptReportMapper.selectById(id);
    }

    @Override
    public List<DeptReport> getAllDeptReports() {
        QueryWrapper<DeptReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", "0");
        return deptReportMapper.selectList(queryWrapper);
    }

    @Override
    public DeptReport updateDeptReport(DeptReport deptReport) {
        deptReport.setUpdatedAt(LocalDateTime.now());
        deptReportMapper.updateById(deptReport);
        return getDeptReportById(deptReport.getDrId());
    }

    @Override
    public void deleteDeptReport(String id) {
        DeptReport deptReport = new DeptReport();
        deptReport.setDrId(id);
        deptReport.setIsDeleted("1");
        deptReport.setUpdatedAt(LocalDateTime.now());
        deptReportMapper.updateById(deptReport);
    }

    @Override
    public DeptReport getDeptDraftByUserIdAndWeek(String deptId, int week,int year) {
        QueryWrapper<DeptReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id", deptId)
                .eq("week", week)
                .eq("is_deleted", "0")
                .apply("YEAR(created_at) = {0}", year);
        return deptReportMapper.selectOne(queryWrapper);
    }
}
