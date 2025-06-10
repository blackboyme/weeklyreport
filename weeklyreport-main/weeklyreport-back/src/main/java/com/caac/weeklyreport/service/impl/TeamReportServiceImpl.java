package com.caac.weeklyreport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.common.ResultCode;
import com.caac.weeklyreport.common.enums.CommonConstants;
import com.caac.weeklyreport.entity.*;
import com.caac.weeklyreport.exception.BusinessException;
import com.caac.weeklyreport.mapper.FlowHistoryMapper;
import com.caac.weeklyreport.mapper.FlowRecordMapper;
import com.caac.weeklyreport.mapper.TeamReportMapper;
import com.caac.weeklyreport.mapper.UserMapper;
import com.caac.weeklyreport.service.ITeamReportService;
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
 * 团队周报表 服务实现类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@Service
public class TeamReportServiceImpl extends ServiceImpl<TeamReportMapper, TeamReport> implements ITeamReportService {

    @Autowired
    private final TeamReportMapper teamReportMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FlowHistoryMapper flowHistoryMapper;

    @Autowired
    private FlowRecordMapper flowRecordMapper;

    public TeamReportServiceImpl(TeamReportMapper teamReportMapper) {
        this.teamReportMapper = teamReportMapper;
    }

    /**
     * zjy
     * */
    @Transactional
    @Override
    public TeamReport saveTeamReportDraft(TeamReport teamReport){
        // 不能修改其他人的周报
        UserInfo userInfo = UserContext.getCurrentUser();
        if(!userInfo.getUserId().equals(teamReport.getUserId())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }

        TeamReport existingDraft = getTeamDraftByUserIdAndWeek(teamReport.getUserId(), teamReport.getWeek());

        if (existingDraft != null) {
            // 更新
            teamReport.setTrId(existingDraft.getTrId());
            teamReport.setStartDate(WeekDateUtils.getStartDateOfWeek(teamReport.getWeek()));
            teamReport.setEndDate(WeekDateUtils.getEndDateOfWeek(teamReport.getWeek()));
            teamReport.setUpdatedAt(LocalDateTime.now());
            teamReport.setIsDeleted("0");
            teamReportMapper.updateById(teamReport);

            //创建新历史记录
            FlowHistory flowHistory = new FlowHistory();
            flowHistory.setHistoryId(KeyGeneratorUtil.generateUUID());
            flowHistory.setFlowId(existingDraft.getFlowId());
            flowHistory.setReportId(existingDraft.getTrId());
            flowHistory.setReportType(CommonConstants.REPORT_TYPE_TEAM);//1-个人周报,2-团队周报,3-部门周报
            flowHistory.setOperation(CommonConstants.OPERATION_DRAFT);//1-保存为草稿,2-提交,3-通过,4-退回
            flowHistory.setOperatorId(userInfo.getUserId());
            flowHistory.setOperatorName(userInfo.getUserName());
            flowHistory.setOperatorRole(userInfo.getRoleId());
            flowHistory.setCurrentStage(CommonConstants.CURRENT_STAGE_PERSONAL);
            flowHistory.setIsDeleted("0");

            flowHistoryMapper.insert(flowHistory);

            return getTeamReportById(existingDraft.getTrId());
        } else {
            String flowId = KeyGeneratorUtil.generateUUID();
            String trId = KeyGeneratorUtil.generateUUID();

            // 创建新草稿
            teamReport.setTrId(trId);
            teamReport.setFlowId(flowId);
            teamReport.setStartDate(WeekDateUtils.getStartDateOfWeek(teamReport.getWeek()));
            teamReport.setEndDate(WeekDateUtils.getEndDateOfWeek(teamReport.getWeek()));
            teamReport.setCreatedAt(LocalDateTime.now());
            teamReport.setUpdatedAt(LocalDateTime.now());
            teamReport.setIsDeleted("0");
            teamReportMapper.insert(teamReport);

            //创建新流程
            FlowRecord flowRecord = new FlowRecord();
            flowRecord.setFlowId(flowId);
            flowRecord.setReportId(trId);
            flowRecord.setReportType(CommonConstants.REPORT_TYPE_TEAM); //1-个人周报,2-团队周报,3-部门周报
            flowRecord.setCurrentStatus(CommonConstants.CURRENT_STATUS_DRAFT); //1-草稿，2-待审核，3.-已审核，4-已退回
            flowRecord.setCurrentStage(CommonConstants.CURRENT_STAGE_PERSONAL); //1-员工提交,2-团队审核,3-部门审核
            flowRecord.setSubmitterId(userInfo.getUserId());
            flowRecord.setSubmitterName(userInfo.getUserName());
            flowRecord.setIsDeleted("0");
            flowRecordMapper.insert(flowRecord);

            //创建新历史记录
            FlowHistory flowHistory = new FlowHistory();
            flowHistory.setHistoryId(KeyGeneratorUtil.generateUUID());
            flowHistory.setFlowId(flowId);
            flowHistory.setReportId(trId);
            flowHistory.setReportType(CommonConstants.REPORT_TYPE_PERSONAL);//1-个人周报,2-团队周报,3-部门周报
            flowHistory.setOperation(CommonConstants.OPERATION_DRAFT);//1-保存为草稿,2-提交,3-通过,4-退回
            flowHistory.setOperatorId(userInfo.getUserId());
            flowHistory.setOperatorName(userInfo.getUserName());
            flowHistory.setOperatorRole(userInfo.getRoleId());
            flowHistory.setCurrentStage(CommonConstants.CURRENT_STAGE_PERSONAL);
            flowHistory.setIsDeleted("0");
            flowHistoryMapper.insert(flowHistory);

            return teamReport;
        }

    }
    @Override
    public TeamReport submitTeamReport(TeamReport teamReport){
        // 不能修改其他人的周报
        UserInfo userInfo = UserContext.getCurrentUser();
        if(!userInfo.getUserId().equals(teamReport.getUserId())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }

        User approver = getApprover();

        TeamReport existingDraft = getTeamDraftByUserIdAndWeek(teamReport.getUserId(), teamReport.getWeek());

        if (existingDraft != null) {
            // 更新草稿
            teamReport.setTrId(existingDraft.getTrId());
            teamReport.setUpdatedAt(LocalDateTime.now());
            teamReport.setIsDeleted("0");
            teamReportMapper.updateById(teamReport);

            //创建新历史记录
            FlowHistory flowHistory = new FlowHistory();
            flowHistory.setFlowId(KeyGeneratorUtil.generateUUID());
            flowHistory.setReportId(existingDraft.getTrId());
            flowHistory.setReportType(CommonConstants.REPORT_TYPE_TEAM);//1-个人周报,2-团队周报,3-部门周报
            flowHistory.setOperation(CommonConstants.OPERATION_SUBMIT);//1-保存为草稿,2-提交,3-通过,4-退回
            flowHistory.setOperatorId(userInfo.getUserId());
            flowHistory.setOperatorName(userInfo.getUserName());
            flowHistory.setOperatorRole(userInfo.getRoleId());
            flowHistory.setIsDeleted("0");
            flowHistoryMapper.insert(flowHistory);

            flowRecordMapper.updateStatus(existingDraft.getFlowId(),CommonConstants.CURRENT_STATUS_SUBMIT,
                    approver.getUserId(),approver.getUserName());

            return getTeamReportById(existingDraft.getTrId());
        } else {
            String flowId = KeyGeneratorUtil.generateUUID();
            String trId = KeyGeneratorUtil.generateUUID();

            // 创建新草稿
            teamReport.setTrId(trId);
            teamReport.setFlowId(flowId);
            teamReport.setStartDate(WeekDateUtils.getStartDateOfWeek(teamReport.getWeek()));
            teamReport.setEndDate(WeekDateUtils.getEndDateOfWeek(teamReport.getWeek()));
            teamReport.setIsDeleted("0");
            teamReportMapper.insert(teamReport);

            //创建新流程
            FlowRecord flowRecord = new FlowRecord();
            flowRecord.setFlowId(flowId);
            flowRecord.setReportId(trId);
            flowRecord.setReportType(CommonConstants.REPORT_TYPE_TEAM); //1-个人周报,2-团队周报,3-部门周报
            flowRecord.setCurrentStatus(CommonConstants.CURRENT_STATUS_SUBMIT); //1-草稿，2-待审核，3.-已审核，4-已退回
            flowRecord.setCurrentStage(CommonConstants.CURRENT_STAGE_TEAM); //1-员工提交,2-团队审核,3-部门审核
            flowRecord.setSubmitterId(userInfo.getUserId());
            flowRecord.setSubmitterName(userInfo.getUserName());
            flowRecord.setApproverId(approver.getUserId());
            flowRecord.setApproverName(approver.getUserName());
            flowRecord.setIsDeleted("0");
            flowRecordMapper.insert(flowRecord);

            //创建新历史记录
            FlowHistory flowHistory = new FlowHistory();
            flowHistory.setHistoryId(KeyGeneratorUtil.generateUUID());
            flowHistory.setFlowId(flowId);
            flowHistory.setReportId(trId);
            flowHistory.setReportType(CommonConstants.REPORT_TYPE_TEAM);//1-个人周报,2-团队周报,3-部门周报
            flowHistory.setOperation(CommonConstants.OPERATION_SUBMIT);//1-保存为草稿,2-提交,3-通过,4-退回
            flowHistory.setOperatorId(userInfo.getUserId());
            flowHistory.setOperatorName(userInfo.getUserName());
            flowHistory.setOperatorRole(userInfo.getRoleId());
            flowHistory.setCurrentStage(CommonConstants.CURRENT_STAGE_TEAM);//1-员工提交, 2-团队审核, 3-部门审核
            flowHistory.setIsDeleted("0");
            flowHistoryMapper.insert(flowHistory);

            return teamReport;
        }
    }
    @Override
    public TeamReport createTeamReport(TeamReport teamReport) {
        teamReport.setTrId(UUID.randomUUID().toString());
        teamReport.setCreatedAt(LocalDateTime.now());
        teamReport.setUpdatedAt(LocalDateTime.now());
        teamReport.setIsDeleted("0");
        teamReportMapper.insert(teamReport);
        return getTeamReportById(teamReport.getTrId());
    }

    @Override
    public TeamReport getTeamReportById(String id) {
        return teamReportMapper.selectById(id);
    }

    @Override
    public List<TeamReport> getAllTeamReports() {
        QueryWrapper<TeamReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", "0");
        return teamReportMapper.selectList(queryWrapper);
    }

    @Override
    public TeamReport updateTeamReport(TeamReport teamReport) {
        teamReport.setUpdatedAt(LocalDateTime.now());
        teamReportMapper.updateById(teamReport);
        return getTeamReportById(teamReport.getTrId());
    }

    @Override
    public void deleteTeamReport(String id) {
        TeamReport teamReport = new TeamReport();
        teamReport.setTrId(id);
        teamReport.setIsDeleted("1");
        teamReport.setUpdatedAt(LocalDateTime.now());
        teamReportMapper.updateById(teamReport);
    }
    @Override
    public TeamReport getTeamDraftByUserIdAndWeek(String userId, int week) {
        QueryWrapper<TeamReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("week", week)
                .eq("is_deleted", "0");
        return teamReportMapper.selectOne(queryWrapper);
    }
    public User getApprover() {
        return userMapper.getDeptApprover("3");
    }
}
