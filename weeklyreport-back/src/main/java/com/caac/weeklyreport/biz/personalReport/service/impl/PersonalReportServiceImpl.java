package com.caac.weeklyreport.biz.personalReport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.biz.personalReport.entity.PersonalReport;
import com.caac.weeklyreport.biz.personalReport.entity.dto.PersonalReportWeekDTO;
import com.caac.weeklyreport.biz.personalReport.entity.vo.PersonalReportVO;
import com.caac.weeklyreport.biz.personalReport.mapper.PersonalReportMapper;
import com.caac.weeklyreport.biz.personalReport.service.IPersonalReportService;
import com.caac.weeklyreport.biz.team.service.ITeamService;
import com.caac.weeklyreport.biz.user.entity.UserInfo;
import com.caac.weeklyreport.common.ResultCode;
import com.caac.weeklyreport.common.enums.CommonConstants;
import com.caac.weeklyreport.exception.BusinessException;
import com.caac.weeklyreport.util.KeyGeneratorUtil;
import com.caac.weeklyreport.util.UserContext;
import com.caac.weeklyreport.util.WeekDateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private ITeamService  teamService;

    @Override
    @Transactional
    public PersonalReport savePersonalReportDraft(PersonalReportVO personalReportVO) {
        // 不能修改其他人的周报
        UserInfo userInfo = UserContext.getCurrentUser();
        if(!userInfo.getUserId().equals(personalReportVO.getUserId())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }

        PersonalReport existingDraft = getDraftByUserIdAndWeek(personalReportVO.getUserId(), personalReportVO.getWeek(), LocalDate.now().getYear());

        if (existingDraft != null) {
            if (CommonConstants.CURRENT_STATUS_SUBMIT.equals(existingDraft.getCurrentStatus())) {
                throw new BusinessException(ResultCode.FLOW_ACCESS_DENY_SUBMIT);
            }

            BeanUtils.copyProperties(personalReportVO, existingDraft);
            existingDraft.setUpdatedAt(LocalDateTime.now());
            personalReportMapper.updateById(existingDraft);

            return getPersonalReportById(existingDraft.getPrId());
        } else {
            String prId = KeyGeneratorUtil.generateUUID();
            PersonalReport personalReport = new PersonalReport();
            BeanUtils.copyProperties(personalReportVO, personalReport);
            // 创建新草稿
            personalReport.setPrId(prId);
            personalReport.setUserName(userInfo.getUserName());
            if("1".equals(userInfo.getRoleType()) || "2".equals(userInfo.getRoleType())) {
                personalReport.setTeamId(userInfo.getTeamId());
                personalReport.setTeamName(userInfo.getTeamName());
            }
            personalReport.setDeptId(userInfo.getDeptId());
            personalReport.setDeptName(userInfo.getDeptName());
            personalReport.setStartDate(WeekDateUtils.getStartDateOfWeek(personalReport.getWeek()));
            personalReport.setEndDate(WeekDateUtils.getEndDateOfWeek(personalReport.getWeek()));
            personalReport.setCurrentStatus(CommonConstants.CURRENT_STATUS_DRAFT);
            personalReport.setCreatedAt(LocalDateTime.now());
            personalReport.setUpdatedAt(LocalDateTime.now());
            personalReport.setIsDeleted("0");
            personalReportMapper.insert(personalReport);

            return getPersonalReportById(prId);
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

        PersonalReport existingDraft = getDraftByUserIdAndWeek(personalReportVO.getUserId(), personalReportVO.getWeek(),LocalDate.now().getYear());

        if (existingDraft != null) {
            if (CommonConstants.CURRENT_STATUS_SUBMIT.equals(existingDraft.getCurrentStatus())) {
                throw new BusinessException(ResultCode.FLOW_ACCESS_DENY_SUBMIT);
            }

            BeanUtils.copyProperties(personalReportVO, existingDraft);
            existingDraft.setUpdatedAt(LocalDateTime.now());
            existingDraft.setCurrentStatus(CommonConstants.CURRENT_STATUS_SUBMIT);
            personalReportMapper.updateById(existingDraft);

            return getPersonalReportById(existingDraft.getPrId());
        } else {
            String prId = KeyGeneratorUtil.generateUUID();
            PersonalReport personalReport = new PersonalReport();
            BeanUtils.copyProperties(personalReportVO, personalReport);
            // 创建新草稿
            personalReport.setPrId(prId);
            personalReport.setUserName(userInfo.getUserName());
            if("1".equals(userInfo.getRoleType()) || "2".equals(userInfo.getRoleType())) {
                personalReport.setTeamId(userInfo.getTeamId());
                personalReport.setTeamName(userInfo.getTeamName());
            }
            personalReport.setDeptId(userInfo.getDeptId());
            personalReport.setDeptName(userInfo.getDeptName());
            personalReport.setStartDate(WeekDateUtils.getStartDateOfWeek(personalReport.getWeek()));
            personalReport.setEndDate(WeekDateUtils.getEndDateOfWeek(personalReport.getWeek()));
            personalReport.setCurrentStatus(CommonConstants.CURRENT_STATUS_SUBMIT);
            personalReport.setCreatedAt(LocalDateTime.now());
            personalReport.setUpdatedAt(LocalDateTime.now());
            personalReport.setIsDeleted("0");
            personalReportMapper.insert(personalReport);

            return getPersonalReportById(prId);
        }
    }

    @Override
    public PersonalReportWeekDTO getCurrentStatusAndWeeklyReport() {
        PersonalReportWeekDTO personalReportWeekDTO = new PersonalReportWeekDTO();
        UserInfo userInfo = UserContext.getCurrentUser();
        int currentWeek =  WeekDateUtils.getCurrentWeekNumber();
        PersonalReport currentPersonalReport = getDraftByUserIdAndWeek(userInfo.getUserId(), currentWeek,LocalDate.now().getYear());
        if(currentPersonalReport == null){
            personalReportWeekDTO.setCurrentStatus(CommonConstants.CURRENT_STATUS_DRAFT);
        } else {
            personalReportWeekDTO.setCurrentStatus(currentPersonalReport.getCurrentStatus());
        }
        personalReportWeekDTO.setCurrentWeekPersonalReport(currentPersonalReport);

        // 当前状态为：已提交不能修改
        if (CommonConstants.CURRENT_STATUS_SUBMIT.equals(personalReportWeekDTO.getCurrentStatus())) {
            personalReportWeekDTO.setCanOperate(Boolean.FALSE);
        } else {
            personalReportWeekDTO.setCanOperate(Boolean.TRUE);
        }

        PersonalReport lastWeekPersonalReport = null;
        if (currentWeek == 1) {
            int lastYearTotalWeek = WeekDateUtils.getTotalWeeksInYear(LocalDate.now().getYear()-1);
            lastWeekPersonalReport = getDraftByUserIdAndWeek(userInfo.getUserId(), lastYearTotalWeek, LocalDate.now().getYear()-1);
        } else {
            lastWeekPersonalReport = getDraftByUserIdAndWeek(userInfo.getUserId(), currentWeek-1, LocalDate.now().getYear());
        }
        personalReportWeekDTO.setLastWeekPersonalReport(lastWeekPersonalReport);
        personalReportWeekDTO.setTeamTitle(userInfo.getTeamTitle());
        return personalReportWeekDTO;
    }

    @Override
    public PersonalReport getWeeklyReportByTime(int year, int week) {
        UserInfo userInfo = UserContext.getCurrentUser();
        if(!"1".equals(userInfo.getRoleType())&&!"2".equals(userInfo.getRoleType())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }
        PersonalReport personalReport = getDraftByUserIdAndWeek(userInfo.getUserId(), week,year);
        if(personalReport == null){
            throw new BusinessException(ResultCode.REPORT_IS_NULL);
        }
        return personalReport;
    }

    public PersonalReport getPersonalReportById(String id) {
        return personalReportMapper.selectById(id);
    }
//
//    @Override
//    public List<PersonalReport> getAllPersonalReports() {
//        QueryWrapper<PersonalReport> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("is_deleted", "0");
//        return personalReportMapper.selectList(queryWrapper);
//    }
//    // 增加年份year字段
//    /**
//     * zjy
//     */
//
//    @Override
//    public List<PersonalReport> getAllPersonalReportsForWeek(String userId, int year, int week){
//        UserInfo userInfo = UserContext.getCurrentUser();
//        if(!userInfo.getUserId().equals(userId)){
//            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
//        }
//        QueryWrapper<PersonalReport> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("is_deleted", "0")
//                .eq("team_id", userInfo.getTeamId())
//                .eq("week", week);
//        return personalReportMapper.selectList(queryWrapper);
//
//    }
//
//    @Override
//    public List<PersonalReportStatusDTO> getAllPersonalReportByStatus(String status) {
//        UserInfo userInfo = UserContext.getCurrentUser();
//        User approver = getApprover(userInfo);
//        if(!approver.getUserId().equals(userInfo.getUserId())){
//            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
//        }
//        List<PersonalReportStatusDTO> personalReports = null;
//        if(StringUtils.isEmpty(status)){
//            personalReports = personalReportMapper.getAllPersonalReportWithStatus(userInfo.getTeamId(),
//                    WeekDateUtils.getCurrentWeekNumber(),LocalDate.now().getYear());
//        } else {
//            personalReports = personalReportMapper.getPersonalReportByStatus(userInfo.getTeamId(),
//                    WeekDateUtils.getCurrentWeekNumber(),LocalDate.now().getYear(),status);
//        }
//        return personalReports;
//    }
//
//
//

//
//    @Override
//    public PersonalReportStatusDTO getLeaderWeeklyReportByTime(int year, int week) {
//        UserInfo userInfo = UserContext.getCurrentUser();
//        if(!"3".equals(userInfo.getRoleType())){
//            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
//        }
//        PersonalReport personalReport = getDraftByUserIdAndWeek(userInfo.getUserId(), week,year);
//        if(personalReport == null){
//            throw new BusinessException(ResultCode.REPORT_IS_NULL);
//        }
//        FlowRecord flowRecord = flowRecordMapper.selectById(personalReport.getFlowId());
//        if(flowRecord == null){
//            throw new BusinessException(ResultCode.FLOW_IS_NULL);
//        }
//        PersonalReportStatusDTO personalReportStatusDTO = new PersonalReportStatusDTO();
//        BeanUtils.copyProperties(personalReport, personalReportStatusDTO);
//        personalReportStatusDTO.setStatus(flowRecord.getCurrentStatus());
//        personalReportStatusDTO.setComment(flowRecord.getComment());
//        return personalReportStatusDTO;
//    }
//
//    @Override
//    public void exportPersonalReportExcel(String teamId,int week,int year,HttpServletResponse response) {
//        List<PersonalReportExcelDTO>  resultList  = personalReportMapper.getExportPersonalReport(teamId,
//                week,year);
//
//        try {
//            //HttpServletResponse消息头参数设置
//            response.setCharacterEncoding("UTF-8");
//            response.setHeader("Content-Transfer-Encoding", "binary");
//            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
//            response.setHeader("Pragma", "public");
//            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
//            String fileName = "导出团队第"+week+"周报列表"+ ".xlsx";
//            fileName = new String(fileName.getBytes(), "ISO-8859-1");
//            response.setHeader("Content-Disposition", "attachment;filename=" + fileName );
//            EasyExcel.write(response.getOutputStream(), PersonalReportExcelDTO.class)
//                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 启用自适应
//                    .autoCloseStream(Boolean.FALSE)
//                    .sheet("团队周报")
//                    .doWrite(resultList);
//        } catch (Exception e) {
//            LogBacks.error(e.getMessage());
//        }
//    }
//
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Boolean passPersonalReport(PassVO passVO) {
//        UserInfo userInfo = UserContext.getCurrentUser();
//        if(!userInfo.getRoleType().equals("2")) {
//            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
//        }
//        PersonalReport personalReport = personalReportMapper.selectById(passVO.getPrId());
//        if(personalReport == null){
//            throw new BusinessException(ResultCode.PARAM_IS_ERROR);
//        }
//        FlowRecord flowRecord = flowRecordMapper.selectById(personalReport.getFlowId());
//        if(flowRecord == null){
//            throw new BusinessException(ResultCode.FLOW_IS_NULL);
//        }
//        if(!flowRecord.getCurrentStatus().equals(CommonConstants.CURRENT_STATUS_SUBMIT)){
//            throw new BusinessException(ResultCode.FLOW_ACCESS_PASS);
//        }
//
//        flowRecord.setCurrentStage(CommonConstants.CURRENT_STAGE_TEAM);
//        flowRecord.setCurrentStatus(CommonConstants.CURRENT_STATUS_PASS);
//        flowRecord.setComment(passVO.getComment());
//        flowRecord.setUpdatedAt(LocalDateTime.now());
//        int count = flowRecordMapper.updateById(flowRecord);
//
//        //创建新历史记录
//        FlowHistory flowHistory = new FlowHistory();
//        flowHistory.setHistoryId(KeyGeneratorUtil.generateUUID());
//        flowHistory.setFlowId(personalReport.getFlowId());
//        flowHistory.setReportId(personalReport.getPrId());
//        flowHistory.setReportType(CommonConstants.REPORT_TYPE_PERSONAL);//1-个人周报,2-团队周报,3-部门周报
//        flowHistory.setOperation(CommonConstants.OPERATION_PASS);//1-保存为草稿,2-提交,3-通过,4-退回
//        flowHistory.setOperatorId(userInfo.getUserId());
//        flowHistory.setOperatorName(userInfo.getUserName());
//        flowHistory.setOperatorRole(userInfo.getRoleId());
//        flowHistory.setCurrentStage(CommonConstants.CURRENT_STAGE_TEAM);
//        flowHistory.setIsDeleted("0");
//
//        flowHistoryMapper.insert(flowHistory);
//
//        return count != 0;
//    }
//
//    @Override
//    public Boolean cancelPersonalReport(CancelVO cancelVO) {
//        UserInfo userInfo = UserContext.getCurrentUser();
//        if(!userInfo.getRoleType().equals("2")) {
//            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
//        }
//        PersonalReport personalReport = personalReportMapper.selectById(cancelVO.getPrId());
//        if(personalReport == null){
//            throw new BusinessException(ResultCode.PARAM_IS_ERROR);
//        }
//        FlowRecord flowRecord = flowRecordMapper.selectById(personalReport.getFlowId());
//        if(flowRecord == null){
//            throw new BusinessException(ResultCode.FLOW_IS_NULL);
//        }
//        if(!flowRecord.getCurrentStatus().equals(CommonConstants.CURRENT_STATUS_SUBMIT)){
//            throw new BusinessException(ResultCode.FLOW_ACCESS_PASS);
//        }
//
//        flowRecord.setCurrentStage(CommonConstants.CURRENT_STAGE_PERSONAL);
//        flowRecord.setCurrentStatus(CommonConstants.CURRENT_STATUS_CANCEL);
//        flowRecord.setComment(cancelVO.getComment());
//        flowRecord.setUpdatedAt(LocalDateTime.now());
//        int count = flowRecordMapper.updateById(flowRecord);
//
//        //创建新历史记录
//        FlowHistory flowHistory = new FlowHistory();
//        flowHistory.setHistoryId(KeyGeneratorUtil.generateUUID());
//        flowHistory.setFlowId(personalReport.getFlowId());
//        flowHistory.setReportId(personalReport.getPrId());
//        flowHistory.setReportType(CommonConstants.REPORT_TYPE_PERSONAL);//1-个人周报,2-团队周报,3-部门周报
//        flowHistory.setOperation(CommonConstants.OPERATION_CANCEL);//1-保存为草稿,2-提交,3-通过,4-退回
//        flowHistory.setOperatorId(userInfo.getUserId());
//        flowHistory.setOperatorName(userInfo.getUserName());
//        flowHistory.setOperatorRole(userInfo.getRoleId());
//        flowHistory.setCurrentStage(CommonConstants.CURRENT_STAGE_PERSONAL);
//        flowHistory.setComment(cancelVO.getComment());
//        flowHistory.setIsDeleted("0");
//
//        flowHistoryMapper.insert(flowHistory);
//
//        return count != 0;
//    }
//
//    @Override
//    public PersonalReport updatePersonalReport(PersonalReport personalReport) {
//        personalReport.setUpdatedAt(LocalDateTime.now());
//        personalReportMapper.updateById(personalReport);
//        return getPersonalReportById(personalReport.getPrId());
//    }
//
//    @Override
//    public void deletePersonalReport(String id) {
//        PersonalReport personalReport = new PersonalReport();
//        personalReport.setPrId(id);
//        personalReport.setIsDeleted("1");
//        personalReport.setUpdatedAt(LocalDateTime.now());
//        personalReportMapper.updateById(personalReport);
//    }
//
//
//
    public PersonalReport getDraftByUserIdAndWeek(String userId, int week,int year) {
        QueryWrapper<PersonalReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("week", week)
                   .eq("is_deleted", "0")
                   .apply("YEAR(created_at) = {0}", year);
        return personalReportMapper.selectOne(queryWrapper);
    }
//
//
////    public List<PersonalReport> getAllPersonalReportByTeamIdAndWeekAndYear(String teamId, int week,int year) {
////        QueryWrapper<PersonalReport> queryWrapper = new QueryWrapper<>();
////        queryWrapper.eq("team_id", teamId)
////                .eq("week", week)
////                .eq("is_deleted", "0")
////                .apply("YEAR(created_at) = {0}", year);
////        return personalReportMapper.selectList(queryWrapper);
////    }
//
//
//    public User getApprover(UserInfo userInfo) {
//        if("1".equals(userInfo.getRoleType())||"2".equals(userInfo.getRoleType())){
//            if(userMapper.getTeamApprover(userInfo.getTeamId(),"2") == null) {
//                throw new BusinessException(ResultCode.FLOW_LEADER_NULL);
//            }
//            return userMapper.getTeamApprover(userInfo.getTeamId(),"2") ;
//        } else {
//            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
//        }
//    }
//
//    /**
//     * zjy
//     */
//    @Override
//    @Transactional
//    public ResultBean<PersonalReport> frontPersonalReportCheck(String userId, int week){
//        UserInfo currentUser = UserContext.getCurrentUser();
//        if(!currentUser.getUserId().equals(userId)){
//            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
//        }
//        PersonalReport existingDraft = getDraftByUserIdAndWeek(userId, week,LocalDate.now().getYear());
//        if(existingDraft == null){
//            return ResultBean.success(ResultCode.OPEN_WITHOUT_DATA);
//        }
//        FlowRecord flowRecord = flowRecordMapper.selectById(existingDraft.getFlowId());
//        if("1".equals(flowRecord.getReportType()) && "0".equals(flowRecord.getCurrentStatus())){
//            return ResultBean.success(ResultCode.OPEN_WITH_DATA,existingDraft);
//        }
//        return ResultBean.success(ResultCode.NO_OPEN);
//    }
}