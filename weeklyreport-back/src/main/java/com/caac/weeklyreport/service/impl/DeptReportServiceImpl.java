package com.caac.weeklyreport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.common.ResultCode;
import com.caac.weeklyreport.common.enums.CommonConstants;
import com.caac.weeklyreport.entity.DeptReport;
import com.caac.weeklyreport.entity.FlowHistory;
import com.caac.weeklyreport.entity.FlowRecord;
import com.caac.weeklyreport.entity.UserInfo;
import com.caac.weeklyreport.exception.BusinessException;
import com.caac.weeklyreport.mapper.DeptReportMapper;
import com.caac.weeklyreport.mapper.FlowHistoryMapper;
import com.caac.weeklyreport.mapper.FlowRecordMapper;
import com.caac.weeklyreport.service.IDeptReportService;
import com.caac.weeklyreport.util.KeyGeneratorUtil;
import com.caac.weeklyreport.util.UserContext;
import com.caac.weeklyreport.util.WeekDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public DeptReport saveDeptReportDraft(DeptReport deptReport) {
        // 不能修改其他人的周报
        UserInfo userInfo = UserContext.getCurrentUser();
        if(!userInfo.getUserId().equals(deptReport.getUserId())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }

        DeptReport existingDraft = getDeptDraftByUserIdAndWeek(deptReport.getUserId(), deptReport.getWeek());

        if (existingDraft != null) {
            // 更新
            deptReport.setDrId(existingDraft.getDrId());
            deptReport.setStartDate(WeekDateUtils.getStartDateOfWeek(deptReport.getWeek()));
            deptReport.setEndDate(WeekDateUtils.getEndDateOfWeek(deptReport.getWeek()));
            deptReport.setUpdatedAt(LocalDateTime.now());
            deptReport.setIsDeleted("0");
            deptReportMapper.updateById(deptReport);

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

            // 创建新草稿
            deptReport.setDrId(drId);
            deptReport.setFlowId(flowId);
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
            flowHistory.setReportType(CommonConstants.REPORT_TYPE_PERSONAL);//1-个人周报,2-团队周报,3-部门周报
            flowHistory.setOperation(CommonConstants.OPERATION_DRAFT);//1-保存为草稿,2-提交,3-通过,4-退回
            flowHistory.setOperatorId(userInfo.getUserId());
            flowHistory.setOperatorName(userInfo.getUserName());
            flowHistory.setOperatorRole(userInfo.getRoleId());
            flowHistory.setCurrentStage(CommonConstants.CURRENT_STAGE_PERSONAL);
            flowHistory.setIsDeleted("0");
            flowHistoryMapper.insert(flowHistory);

            return deptReport;
        }
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
    public DeptReport getDeptDraftByUserIdAndWeek(String userId, int week) {
        QueryWrapper<DeptReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("week", week)
                .eq("is_deleted", "0");
        return deptReportMapper.selectOne(queryWrapper);
    }
}
