package com.caac.weeklyreport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.common.ResultCode;
import com.caac.weeklyreport.common.enums.CommonConstants;
import com.caac.weeklyreport.entity.*;
import com.caac.weeklyreport.entity.dto.PersonalReportStatusDTO;
import com.caac.weeklyreport.entity.dto.TeamReportWeekDTO;
import com.caac.weeklyreport.entity.vo.CancelVO;
import com.caac.weeklyreport.entity.vo.PassVO;
import com.caac.weeklyreport.entity.vo.TeamReportVO;
import com.caac.weeklyreport.exception.BusinessException;
import com.caac.weeklyreport.mapper.*;
import com.caac.weeklyreport.service.ITeamReportService;
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

    @Autowired
    private PersonalReportMapper personalReportMapper;

    public TeamReportServiceImpl(TeamReportMapper teamReportMapper) {
        this.teamReportMapper = teamReportMapper;
    }

    /**
     * zjy
     */
    @Transactional
    @Override
    public TeamReport saveTeamReportDraft(TeamReportVO teamReportVO){
        // 不能修改其他人的周报
        UserInfo userInfo = UserContext.getCurrentUser();
        if(!userInfo.getTeamId().equals(teamReportVO.getTeamId())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }

        TeamReport existingDraft = getTeamDraftByUserIdAndWeek(teamReportVO.getTeamId(), teamReportVO.getWeek(),LocalDate.now().getYear());

        if (existingDraft != null) {
            FlowRecord flowRecord = flowRecordMapper.selectById(existingDraft.getFlowId());
            if (flowRecord == null) {
                throw new BusinessException(ResultCode.FLOW_IS_NULL);
            }

            // 当前状态为已通过审批，不能修改
            if (CommonConstants.CURRENT_STATUS_PASS.equals(flowRecord.getCurrentStatus())) {
                throw new BusinessException(ResultCode.FLOW_ACCESS_DENY_PASS);
            } else if (CommonConstants.CURRENT_STATUS_SUBMIT.equals(flowRecord.getCurrentStatus())) {
                throw new BusinessException(ResultCode.FLOW_ACCESS_DENY_SUBMIT);
            }

            BeanUtils.copyProperties(teamReportVO, existingDraft);
            existingDraft.setUpdatedAt(LocalDateTime.now());
            teamReportMapper.updateById(existingDraft);

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
            flowHistory.setCurrentStage(CommonConstants.CURRENT_STAGE_TEAM);
            flowHistory.setIsDeleted("0");

            flowHistoryMapper.insert(flowHistory);

            return getTeamReportById(existingDraft.getTrId());
        } else {
            String flowId = KeyGeneratorUtil.generateUUID();
            String trId = KeyGeneratorUtil.generateUUID();

            TeamReport teamReport = new TeamReport();
            BeanUtils.copyProperties(teamReportVO, teamReport);
            // 创建新草稿
            teamReport.setTrId(trId);
            teamReport.setFlowId(flowId);
            teamReport.setUserName(userInfo.getUserName());
            teamReport.setTeamId(userInfo.getTeamId());
            teamReport.setTeamName(userInfo.getTeamName());
            teamReport.setDeptName(userInfo.getDepartName());
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
            flowRecord.setCurrentStage(CommonConstants.CURRENT_STAGE_TEAM); //1-员工提交,2-团队审核,3-部门审核
            flowRecord.setSubmitterId(userInfo.getUserId());
            flowRecord.setSubmitterName(userInfo.getUserName());
            flowRecord.setIsDeleted("0");
            flowRecordMapper.insert(flowRecord);

            //创建新历史记录
            FlowHistory flowHistory = new FlowHistory();
            flowHistory.setHistoryId(KeyGeneratorUtil.generateUUID());
            flowHistory.setFlowId(flowId);
            flowHistory.setReportId(trId);
            flowHistory.setReportType(CommonConstants.REPORT_TYPE_TEAM);//1-个人周报,2-团队周报,3-部门周报
            flowHistory.setOperation(CommonConstants.OPERATION_DRAFT);//1-保存为草稿,2-提交,3-通过,4-退回
            flowHistory.setOperatorId(userInfo.getUserId());
            flowHistory.setOperatorName(userInfo.getUserName());
            flowHistory.setOperatorRole(userInfo.getRoleId());
            flowHistory.setCurrentStage(CommonConstants.CURRENT_STAGE_TEAM);
            flowHistory.setIsDeleted("0");
            flowHistoryMapper.insert(flowHistory);

            return teamReport;
        }

    }
    @Override
    public TeamReport submitTeamReport(TeamReportVO teamReportVO){
        // 不能修改其他人的周报
        UserInfo userInfo = UserContext.getCurrentUser();
        if(!userInfo.getTeamId().equals(teamReportVO.getTeamId())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }

        User approver = getApprover(userInfo);

        TeamReport existingDraft = getTeamDraftByUserIdAndWeek(teamReportVO.getTeamId(), teamReportVO.getWeek(),LocalDate.now().getYear());

        if (existingDraft != null) {
            FlowRecord flowRecord = flowRecordMapper.selectById(existingDraft.getFlowId());
            if (flowRecord == null) {
                throw new BusinessException(ResultCode.FLOW_IS_NULL);
            }

            // 当前状态为已通过审批，不能修改
            if (CommonConstants.CURRENT_STATUS_PASS.equals(flowRecord.getCurrentStatus())) {
                throw new BusinessException(ResultCode.FLOW_ACCESS_DENY_PASS);
            } else if (CommonConstants.CURRENT_STATUS_SUBMIT.equals(flowRecord.getCurrentStatus())) {
                throw new BusinessException(ResultCode.FLOW_ACCESS_DENY_SUBMIT);
            }

            // 更新草稿
            BeanUtils.copyProperties(teamReportVO, existingDraft);
            teamReportMapper.updateById(existingDraft);

            //创建新历史记录
            FlowHistory flowHistory = new FlowHistory();
            flowHistory.setHistoryId(KeyGeneratorUtil.generateUUID());
            flowHistory.setFlowId(existingDraft.getFlowId());
            flowHistory.setReportId(existingDraft.getTrId());
            flowHistory.setCurrentStage(CommonConstants.CURRENT_STAGE_TEAM); //1-员工提交,2-团队审核,3-部门审核
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
            TeamReport teamReport = new TeamReport();
            BeanUtils.copyProperties(teamReportVO, teamReport);
            // 创建新草稿
            teamReport.setTrId(trId);
            teamReport.setFlowId(flowId);
            teamReport.setUserName(userInfo.getUserName());
            teamReport.setTeamId(userInfo.getTeamId());
            teamReport.setTeamName(userInfo.getTeamName());
            teamReport.setDeptName(userInfo.getDepartName());
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
            flowHistory.setCurrentStage(CommonConstants.CURRENT_STAGE_TEAM);
            flowHistory.setIsDeleted("0");
            flowHistoryMapper.insert(flowHistory);

            return getTeamReportById(trId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean passTeamReport(PassVO passVO) {
        UserInfo userInfo = UserContext.getCurrentUser();
        if(!userInfo.getRoleType().equals("2")) {
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }
        TeamReport teamReport = teamReportMapper.selectById(passVO.getTrId());
        if(teamReport == null){
            throw new BusinessException(ResultCode.PARAM_IS_ERROR);
        }
        FlowRecord flowRecord = flowRecordMapper.selectById(teamReport.getFlowId());
        if(flowRecord == null){
            throw new BusinessException(ResultCode.FLOW_IS_NULL);
        }
        if(!flowRecord.getCurrentStatus().equals(CommonConstants.CURRENT_STATUS_SUBMIT)){
            throw new BusinessException(ResultCode.FLOW_ACCESS_PASS);
        }

        flowRecord.setCurrentStage(CommonConstants.CURRENT_STAGE_TEAM);
        flowRecord.setCurrentStatus(CommonConstants.CURRENT_STATUS_PASS);
        flowRecord.setComment(passVO.getComment());
        flowRecord.setUpdatedAt(LocalDateTime.now());
        int count = flowRecordMapper.updateById(flowRecord);

        //创建新历史记录
        FlowHistory flowHistory = new FlowHistory();
        flowHistory.setHistoryId(KeyGeneratorUtil.generateUUID());
        flowHistory.setFlowId(teamReport.getFlowId());
        flowHistory.setReportId(teamReport.getTrId());
        flowHistory.setReportType(CommonConstants.REPORT_TYPE_TEAM);//1-个人周报,2-团队周报,3-部门周报
        flowHistory.setOperation(CommonConstants.OPERATION_PASS);//1-保存为草稿,2-提交,3-通过,4-退回
        flowHistory.setOperatorId(userInfo.getUserId());
        flowHistory.setOperatorName(userInfo.getUserName());
        flowHistory.setOperatorRole(userInfo.getRoleId());
        flowHistory.setCurrentStage(CommonConstants.CURRENT_STAGE_TEAM);
        flowHistory.setIsDeleted("0");

        flowHistoryMapper.insert(flowHistory);

        return count != 0;
    }

    @Override
    public Boolean cancelTeamReport(CancelVO cancelVO) {
        UserInfo userInfo = UserContext.getCurrentUser();
        if(!userInfo.getRoleType().equals("2")) {
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }
        TeamReport teamReport = teamReportMapper.selectById(cancelVO.getTrId());
        if(teamReport == null){
            throw new BusinessException(ResultCode.PARAM_IS_ERROR);
        }
        FlowRecord flowRecord = flowRecordMapper.selectById(teamReport.getFlowId());
        if(flowRecord == null){
            throw new BusinessException(ResultCode.FLOW_IS_NULL);
        }
        if(!flowRecord.getCurrentStatus().equals(CommonConstants.CURRENT_STATUS_SUBMIT)){
            throw new BusinessException(ResultCode.FLOW_ACCESS_PASS);
        }

        flowRecord.setCurrentStage(CommonConstants.CURRENT_STAGE_TEAM);
        flowRecord.setCurrentStatus(CommonConstants.CURRENT_STATUS_CANCEL);
        flowRecord.setComment(cancelVO.getComment());
        flowRecord.setUpdatedAt(LocalDateTime.now());
        int count = flowRecordMapper.updateById(flowRecord);

        //创建新历史记录
        FlowHistory flowHistory = new FlowHistory();
        flowHistory.setHistoryId(KeyGeneratorUtil.generateUUID());
        flowHistory.setFlowId(teamReport.getFlowId());
        flowHistory.setReportId(teamReport.getTrId());
        flowHistory.setReportType(CommonConstants.REPORT_TYPE_TEAM);//1-个人周报,2-团队周报,3-部门周报
        flowHistory.setOperation(CommonConstants.OPERATION_CANCEL);//1-保存为草稿,2-提交,3-通过,4-退回
        flowHistory.setOperatorId(userInfo.getUserId());
        flowHistory.setOperatorName(userInfo.getUserName());
        flowHistory.setOperatorRole(userInfo.getRoleId());
        flowHistory.setCurrentStage(CommonConstants.CURRENT_STAGE_PERSONAL);
        flowHistory.setComment(cancelVO.getComment());
        flowHistory.setIsDeleted("0");

        flowHistoryMapper.insert(flowHistory);

        return count != 0;
    }

    @Override
    public TeamReportWeekDTO getCurrentStatusAndWeeklyReport() {
        TeamReportWeekDTO teamReportWeekDTO = new TeamReportWeekDTO();
        UserInfo userInfo = UserContext.getCurrentUser();
        int currentWeek =  WeekDateUtils.getCurrentWeekNumber();
        TeamReport currentTeamReport = getTeamDraftByUserIdAndWeek(userInfo.getTeamId(), currentWeek, LocalDate.now().getYear());
        // 没有暂存团队周报
        if(currentTeamReport == null){
            teamReportWeekDTO.setCurrentStatus(CommonConstants.CURRENT_STATUS_DRAFT);
            teamReportWeekDTO.setCurrentWeekTeamReport(currentTeamReport);
        } else {// 有暂存或以提交的团队周报
            FlowRecord flowRecord = flowRecordMapper.selectById(currentTeamReport.getFlowId());
            if(flowRecord == null){
                throw new BusinessException(ResultCode.FLOW_IS_NULL);
            }
            teamReportWeekDTO.setCurrentStatus(flowRecord.getCurrentStatus());
            teamReportWeekDTO.setCurrentWeekTeamReport(currentTeamReport);
        }

        // 当前状态为已通过审批、已提交，前端不允许用户打开
        if (CommonConstants.CURRENT_STATUS_PASS.equals(teamReportWeekDTO.getCurrentStatus())
                || CommonConstants.CURRENT_STATUS_SUBMIT.equals(teamReportWeekDTO.getCurrentStatus())) {
            teamReportWeekDTO.setCanOperate(Boolean.FALSE);
        } else {
            teamReportWeekDTO.setCanOperate(Boolean.TRUE);
        }
        // 封装拼接数据
        List<PersonalReportStatusDTO>  personalReports = personalReportMapper.getPersonalReportByStatus(userInfo.getTeamId(),
                    WeekDateUtils.getCurrentWeekNumber(),LocalDate.now().getYear(),"3");
        currentTeamReport = new TeamReport();
        StringBuilder equip =  new StringBuilder();
        StringBuilder systemRd = new StringBuilder();
        StringBuilder construction = new StringBuilder();
        StringBuilder others = new StringBuilder();
        StringBuilder nextEquip = new StringBuilder();
        StringBuilder nextSystem = new StringBuilder();
        StringBuilder nextConstruction = new StringBuilder();
        StringBuilder nextOthers = new StringBuilder();

        for (int i = 0; i < personalReports.size(); i++) {
            if(personalReports.size() > 1 && i != personalReports.size() - 1 && i != 0){
                equip.append(personalReports.get(i).getEquip()).append(" ").append("("+personalReports.get(i).getUserName()+")").append("/n");
                systemRd.append(personalReports.get(i).getSystemRd()).append(" ").append("("+personalReports.get(i).getUserName()+")").append("/n");
                construction.append(personalReports.get(i).getConstruction()).append(" ").append("("+personalReports.get(i).getUserName()+")").append("/n");
                others.append(personalReports.get(i).getOthers()).append(" ").append("("+personalReports.get(i).getUserName()+")").append("/n");
                nextEquip.append(personalReports.get(i).getNextEquip()).append(" ").append("("+personalReports.get(i).getUserName()+")").append("/n");
                nextSystem.append(personalReports.get(i).getNextSystem()).append(" ").append("("+personalReports.get(i).getUserName()+")").append("/n");
                nextConstruction.append(personalReports.get(i).getNextConstruction()).append(" ").append("("+personalReports.get(i).getUserName()+")").append("/n");
                nextOthers.append(personalReports.get(i).getNextOthers()).append(" ").append("("+personalReports.get(i).getUserName()+")").append("/n");
            } else if(i == 0) {
                equip.append(personalReports.get(i).getEquip()).append(" ").append("("+personalReports.get(i).getUserName()+")").append("/n");
                systemRd.append(personalReports.get(i).getSystemRd()).append(" ").append("("+personalReports.get(i).getUserName()+")").append("/n");
                construction.append(personalReports.get(i).getConstruction()).append(" ").append("("+personalReports.get(i).getUserName()+")").append("/n");
                others.append(personalReports.get(i).getOthers()).append(" ").append("("+personalReports.get(i).getUserName()+")").append("/n");
                nextEquip.append(personalReports.get(i).getNextEquip()).append(" ").append("("+personalReports.get(i).getUserName()+")").append("/n");
                nextSystem.append(personalReports.get(i).getNextSystem()).append(" ").append("("+personalReports.get(i).getUserName()+")").append("/n");
                nextConstruction.append(personalReports.get(i).getNextConstruction()).append(" ").append("("+personalReports.get(i).getUserName()+")").append("/n");
                nextOthers.append(personalReports.get(i).getNextOthers()).append(" ").append("("+personalReports.get(i).getUserName()+")").append("/n");
            } else {
                equip.append(personalReports.get(i).getEquip()).append(" ").append("("+personalReports.get(i).getUserName()+")");
                systemRd.append(personalReports.get(i).getSystemRd()).append(" ").append("("+personalReports.get(i).getUserName()+")");
                construction.append(personalReports.get(i).getConstruction()).append(" ").append("("+personalReports.get(i).getUserName()+")");
                others.append(personalReports.get(i).getOthers()).append(" ").append("("+personalReports.get(i).getUserName()+")");
                nextEquip.append(personalReports.get(i).getNextEquip()).append(" ").append("("+personalReports.get(i).getUserName()+")");
                nextSystem.append(personalReports.get(i).getNextSystem()).append(" ").append("("+personalReports.get(i).getUserName()+")");
                nextConstruction.append(personalReports.get(i).getNextConstruction()).append(" ").append("("+personalReports.get(i).getUserName()+")");
                nextOthers.append(personalReports.get(i).getNextOthers()).append(" ").append("("+personalReports.get(i).getUserName()+")");
            }
        }
        TeamReport lastWeekTeamReport = new TeamReport();
        lastWeekTeamReport.setEquip(equip.toString());
        lastWeekTeamReport.setSystemRd(systemRd.toString());
        lastWeekTeamReport.setConstruction(construction.toString());
        lastWeekTeamReport.setOthers(others.toString());
        lastWeekTeamReport.setNextEquip(nextEquip.toString());
        lastWeekTeamReport.setNextSystem(nextSystem.toString());
        lastWeekTeamReport.setNextConstruction(nextConstruction.toString());
        lastWeekTeamReport.setNextOthers(nextOthers.toString());
        teamReportWeekDTO.setLastWeekTeamReport(lastWeekTeamReport);

//        TeamReport lastWeekTeamReport = null;
//        if (currentWeek == 1) {
//            int lastYearTotalWeek = WeekDateUtils.getTotalWeeksInYear(LocalDate.now().getYear()-1);
//            lastWeekTeamReport = getTeamDraftByUserIdAndWeek(userInfo.getUserId(), lastYearTotalWeek, LocalDate.now().getYear()-1);
//        } else {
//            lastWeekTeamReport = getTeamDraftByUserIdAndWeek(userInfo.getUserId(), currentWeek-1, LocalDate.now().getYear());
//        }
//        teamReportWeekDTO.setLastWeekTeamReport(lastWeekTeamReport);

        return teamReportWeekDTO;
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
    public TeamReport getTeamDraftByUserIdAndWeek(String teamId, int week,int year)  {
        QueryWrapper<TeamReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId)
                .eq("week", week)
                .eq("is_deleted", "0")
                .apply("YEAR(created_at) = {0}", year);
        return teamReportMapper.selectOne(queryWrapper);
    }

    public User getApprover(UserInfo userInfo) {
        if("1".equals(userInfo.getRoleType())||"2".equals(userInfo.getRoleType())){
            return userMapper.getTeamApprover(userInfo.getTeamId(),"2");
        } else {
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }
    }
}
