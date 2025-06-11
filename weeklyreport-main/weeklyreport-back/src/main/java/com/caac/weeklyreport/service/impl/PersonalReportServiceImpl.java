package com.caac.weeklyreport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.ResultBean;
import com.caac.weeklyreport.common.ResultCode;
import com.caac.weeklyreport.common.enums.CommonConstants;
import com.caac.weeklyreport.entity.*;
import com.caac.weeklyreport.entity.dto.StatusPersonalReportDTO;
import com.caac.weeklyreport.entity.vo.PersonalReportVO;
import com.caac.weeklyreport.exception.BusinessException;
import com.caac.weeklyreport.mapper.FlowHistoryMapper;
import com.caac.weeklyreport.mapper.FlowRecordMapper;
import com.caac.weeklyreport.mapper.PersonalReportMapper;
import com.caac.weeklyreport.mapper.UserMapper;
import com.caac.weeklyreport.service.IPersonalReportService;
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
 * 个人周报表 服务实现类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@Service
public class PersonalReportServiceImpl extends ServiceImpl<PersonalReportMapper, PersonalReport> implements IPersonalReportService {

    @Autowired
    private PersonalReportMapper personalReportMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FlowHistoryMapper flowHistoryMapper;

    @Autowired
    private FlowRecordMapper flowRecordMapper;

    @Override
    public PersonalReport createPersonalReport(PersonalReport personalReport) {
        personalReport.setPrId(UUID.randomUUID().toString());
        personalReport.setCreatedAt(LocalDateTime.now());
        personalReport.setUpdatedAt(LocalDateTime.now());
        personalReport.setIsDeleted("0");
        personalReportMapper.insert(personalReport);
        return getPersonalReportById(personalReport.getPrId());
    }

    @Override
    public PersonalReport getPersonalReportById(String id) {
        return personalReportMapper.selectById(id);
    }

    @Override
    public List<PersonalReport> getAllPersonalReports() {
        QueryWrapper<PersonalReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", "0");
        return personalReportMapper.selectList(queryWrapper);
    }
    // 增加年份year字段
    /**
     * zjy
     */

    @Override
    public List<PersonalReport> getAllPersonalReportsForWeek(String userId, int year, int week){
        UserInfo userInfo = UserContext.getCurrentUser();
        if(!userInfo.getUserId().equals(userId)){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }
        QueryWrapper<PersonalReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", "0")
                .eq("team_id", userInfo.getTeamId())
                .eq("week", week);
        return personalReportMapper.selectList(queryWrapper);

    }

    @Override
    public StatusPersonalReportDTO getCurrentStatusAndWeeklyReport() {
        StatusPersonalReportDTO statusPersonalReportDTO = new StatusPersonalReportDTO();
        UserInfo userInfo = UserContext.getCurrentUser();
        int currentWeek =  WeekDateUtils.getCurrentWeekNumber();
        PersonalReport currentPersonalReport = getDraftByUserIdAndWeek(userInfo.getUserId(), currentWeek,LocalDate.now().getYear());
        if(currentPersonalReport == null){
            statusPersonalReportDTO.setCurrentStatus(CommonConstants.CURRENT_STATUS_DRAFT);
        } else {
            FlowRecord flowRecord = flowRecordMapper.selectById(currentPersonalReport.getFlowId());
            if(flowRecord == null){
                throw new BusinessException(ResultCode.FLOW_IS_NULL);
            }
            statusPersonalReportDTO.setCurrentStatus(flowRecord.getCurrentStatus());
            statusPersonalReportDTO.setCurrentWeekPersonalReport(currentPersonalReport);
        }

        // 当前状态为已通过审批，不能修改
        if (CommonConstants.CURRENT_STATUS_PASS.equals(statusPersonalReportDTO.getCurrentStatus())
                || CommonConstants.CURRENT_STATUS_SUBMIT.equals(statusPersonalReportDTO.getCurrentStatus())) {
            statusPersonalReportDTO.setCanOperate(Boolean.FALSE);
        } else {
            statusPersonalReportDTO.setCanOperate(Boolean.TRUE);
        }

        PersonalReport lastWeekPersonalReport = null;
        if (currentWeek == 1) {
            int lastYearTotalWeek = WeekDateUtils.getTotalWeeksInYear(LocalDate.now().getYear()-1);
            lastWeekPersonalReport = getDraftByUserIdAndWeek(userInfo.getUserId(), lastYearTotalWeek, LocalDate.now().getYear()-1);
        } else {
            lastWeekPersonalReport = getDraftByUserIdAndWeek(userInfo.getUserId(), currentWeek-1, LocalDate.now().getYear());
        }
        statusPersonalReportDTO.setLastWeekPersonalReport(lastWeekPersonalReport);

        return statusPersonalReportDTO;
    }

    @Override
    public PersonalReport updatePersonalReport(PersonalReport personalReport) {
        personalReport.setUpdatedAt(LocalDateTime.now());
        personalReportMapper.updateById(personalReport);
        return getPersonalReportById(personalReport.getPrId());
    }

    @Override
    public void deletePersonalReport(String id) {
        PersonalReport personalReport = new PersonalReport();
        personalReport.setPrId(id);
        personalReport.setIsDeleted("1");
        personalReport.setUpdatedAt(LocalDateTime.now());
        personalReportMapper.updateById(personalReport);
    }

    @Override
    @Transactional
    public PersonalReport savePersonalReportDraft(PersonalReportVO personalReportVO) {
        // 不能修改其他人的周报
        UserInfo userInfo = UserContext.getCurrentUser();
        if(!userInfo.getUserId().equals(personalReportVO.getUserId())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }

        PersonalReport existingDraft = getDraftByUserIdAndWeek(personalReportVO.getUserId(), personalReportVO.getWeek(),LocalDate.now().getYear());

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

            BeanUtils.copyProperties(personalReportVO, existingDraft);
            existingDraft.setUpdatedAt(LocalDateTime.now());
            personalReportMapper.updateById(existingDraft);

            //创建新历史记录
            FlowHistory flowHistory = new FlowHistory();
            flowHistory.setHistoryId(KeyGeneratorUtil.generateUUID());
            flowHistory.setFlowId(existingDraft.getFlowId());
            flowHistory.setReportId(existingDraft.getPrId());
            flowHistory.setReportType(CommonConstants.REPORT_TYPE_PERSONAL);//1-个人周报,2-团队周报,3-部门周报
            flowHistory.setOperation(CommonConstants.OPERATION_DRAFT);//1-保存为草稿,2-提交,3-通过,4-退回
            flowHistory.setOperatorId(userInfo.getUserId());
            flowHistory.setOperatorName(userInfo.getUserName());
            flowHistory.setOperatorRole(userInfo.getRoleId());
            flowHistory.setCurrentStage(CommonConstants.CURRENT_STAGE_PERSONAL);
            flowHistory.setIsDeleted("0");

            flowHistoryMapper.insert(flowHistory);

            return getPersonalReportById(existingDraft.getPrId());
        } else {
            String flowId = KeyGeneratorUtil.generateUUID();
            String prId = KeyGeneratorUtil.generateUUID();

            PersonalReport personalReport = new PersonalReport();
            BeanUtils.copyProperties(personalReportVO, personalReport);
            // 创建新草稿
            personalReport.setPrId(prId);
            personalReport.setFlowId(flowId);
            personalReport.setUserName(userInfo.getUserName());
            personalReport.setTeamId(userInfo.getTeamId());
            personalReport.setTeamName(userInfo.getTeamName());
            personalReport.setDeptName(userInfo.getDepartName());
            personalReport.setStartDate(WeekDateUtils.getStartDateOfWeek(personalReport.getWeek()));
            personalReport.setEndDate(WeekDateUtils.getEndDateOfWeek(personalReport.getWeek()));
            personalReport.setCreatedAt(LocalDateTime.now());
            personalReport.setUpdatedAt(LocalDateTime.now());
            personalReport.setIsDeleted("0");
            personalReportMapper.insert(personalReport);

            //创建新流程
            FlowRecord flowRecord = new FlowRecord();
            flowRecord.setFlowId(flowId);
            flowRecord.setReportId(prId);
            flowRecord.setReportType(CommonConstants.REPORT_TYPE_PERSONAL); //1-个人周报,2-团队周报,3-部门周报
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
            flowHistory.setReportId(prId);
            flowHistory.setReportType(CommonConstants.REPORT_TYPE_PERSONAL);//1-个人周报,2-团队周报,3-部门周报
            flowHistory.setOperation(CommonConstants.OPERATION_DRAFT);//1-保存为草稿,2-提交,3-通过,4-退回
            flowHistory.setOperatorId(userInfo.getUserId());
            flowHistory.setOperatorName(userInfo.getUserName());
            flowHistory.setOperatorRole(userInfo.getRoleId());
            flowHistory.setCurrentStage(CommonConstants.CURRENT_STAGE_PERSONAL);
            flowHistory.setIsDeleted("0");
            flowHistoryMapper.insert(flowHistory);

            return personalReport;
        }
    }


    @Override
    @Transactional
    public PersonalReport submitPersonalReport(PersonalReportVO personalReportVO) {
        // 不能修改其他人的周报
        UserInfo userInfo = UserContext.getCurrentUser();
        if(!userInfo.getUserId().equals(personalReportVO.getUserId())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }

        User approver = getApprover(userInfo);

        PersonalReport existingDraft = getDraftByUserIdAndWeek(personalReportVO.getUserId(), personalReportVO.getWeek(),LocalDate.now().getYear());

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
            BeanUtils.copyProperties(personalReportVO, existingDraft);
            personalReportMapper.updateById(existingDraft);

            //创建新历史记录
            FlowHistory flowHistory = new FlowHistory();
            flowHistory.setHistoryId(KeyGeneratorUtil.generateUUID());
            flowHistory.setFlowId(existingDraft.getFlowId());
            flowHistory.setReportId(existingDraft.getPrId());
            flowHistory.setCurrentStage(CommonConstants.CURRENT_STAGE_PERSONAL); //1-员工提交,2-团队审核,3-部门审核
            flowHistory.setReportType(CommonConstants.REPORT_TYPE_PERSONAL);//1-个人周报,2-团队周报,3-部门周报
            flowHistory.setOperation(CommonConstants.OPERATION_SUBMIT);//1-保存为草稿,2-提交,3-通过,4-退回
            flowHistory.setOperatorId(userInfo.getUserId());
            flowHistory.setOperatorName(userInfo.getUserName());
            flowHistory.setOperatorRole(userInfo.getRoleId());
            flowHistory.setIsDeleted("0");
            flowHistoryMapper.insert(flowHistory);

            flowRecordMapper.updateStatus(existingDraft.getFlowId(),CommonConstants.CURRENT_STATUS_SUBMIT,
                    approver.getUserId(),approver.getUserName());

            return getPersonalReportById(existingDraft.getPrId());
        } else {
            String flowId = KeyGeneratorUtil.generateUUID();
            String prId = KeyGeneratorUtil.generateUUID();

            // 创建新草稿
            PersonalReport personalReport = new PersonalReport();
            BeanUtils.copyProperties(personalReportVO, personalReport);
            // 创建新草稿
            personalReport.setPrId(prId);
            personalReport.setFlowId(flowId);
            personalReport.setUserName(userInfo.getUserName());
            personalReport.setTeamId(userInfo.getTeamId());
            personalReport.setTeamName(userInfo.getTeamName());
            personalReport.setDeptName(userInfo.getDepartName());
            personalReport.setStartDate(WeekDateUtils.getStartDateOfWeek(personalReport.getWeek()));
            personalReport.setEndDate(WeekDateUtils.getEndDateOfWeek(personalReport.getWeek()));
            personalReport.setCreatedAt(LocalDateTime.now());
            personalReport.setUpdatedAt(LocalDateTime.now());
            personalReport.setIsDeleted("0");
            personalReportMapper.insert(personalReport);

            //创建新流程
            FlowRecord flowRecord = new FlowRecord();
            flowRecord.setFlowId(flowId);
            flowRecord.setReportId(prId);
            flowRecord.setReportType(CommonConstants.REPORT_TYPE_PERSONAL); //1-个人周报,2-团队周报,3-部门周报
            flowRecord.setCurrentStatus(CommonConstants.CURRENT_STATUS_SUBMIT); //1-草稿，2-待审核，3.-已审核，4-已退回
            flowRecord.setCurrentStage(CommonConstants.CURRENT_STAGE_PERSONAL); //1-员工提交,2-团队审核,3-部门审核
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
            flowHistory.setReportId(prId);
            flowHistory.setReportType(CommonConstants.REPORT_TYPE_PERSONAL);//1-个人周报,2-团队周报,3-部门周报
            flowHistory.setOperation(CommonConstants.OPERATION_SUBMIT);//1-保存为草稿,2-提交,3-通过,4-退回
            flowHistory.setOperatorId(userInfo.getUserId());
            flowHistory.setOperatorName(userInfo.getUserName());
            flowHistory.setOperatorRole(userInfo.getRoleId());
            flowHistory.setCurrentStage(CommonConstants.CURRENT_STAGE_PERSONAL);
            flowHistory.setIsDeleted("0");
            flowHistoryMapper.insert(flowHistory);

            return getPersonalReportById(prId);
        }
    }

    @Override
    public PersonalReport getDraftByUserIdAndWeek(String userId, int week,int year) {
        QueryWrapper<PersonalReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("week", week)
                   .eq("is_deleted", "0")
                   .apply("YEAR(created_at) = {0}", year);
        return personalReportMapper.selectOne(queryWrapper);
    }


    public User getApprover(UserInfo userInfo) {
        User submitter = null;
        if("1".equals(userInfo.getRoleType())){
            submitter = userMapper.getTeamApprover(userInfo.getTeamId(),"2");
        } else if("2".equals(userInfo.getRoleType())){
            submitter = userMapper.getDeptApprover("3");
        } else {
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }
        return submitter;
    }

    /**
     * zjy
     */
    @Override
    @Transactional
    public ResultBean<PersonalReport> frontPersonalReportCheck(String userId, int week){
        UserInfo currentUser = UserContext.getCurrentUser();
        if(!currentUser.getUserId().equals(userId)){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }
        PersonalReport existingDraft = getDraftByUserIdAndWeek(userId, week,LocalDate.now().getYear());
        if(existingDraft == null){
            return ResultBean.success(ResultCode.OPEN_WITHOUT_DATA);
        }
        FlowRecord flowRecord = flowRecordMapper.selectById(existingDraft.getFlowId());
        if("1".equals(flowRecord.getReportType()) && "0".equals(flowRecord.getCurrentStatus())){
            return ResultBean.success(ResultCode.OPEN_WITH_DATA,existingDraft);
        }
        return ResultBean.success(ResultCode.NO_OPEN);
    }
}