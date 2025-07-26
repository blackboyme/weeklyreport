package com.caac.weeklyreport.biz.teamReport.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.biz.personalReport.entity.PersonalReport;
import com.caac.weeklyreport.biz.personalReport.entity.dto.PersonalReportExcelDTO;
import com.caac.weeklyreport.biz.personalReport.mapper.PersonalReportMapper;
import com.caac.weeklyreport.biz.teamReport.entity.TeamReport;
import com.caac.weeklyreport.biz.teamReport.entity.dto.TeamReportExcelDTO;
import com.caac.weeklyreport.biz.teamReport.entity.dto.TeamReportWeekDTO;
import com.caac.weeklyreport.biz.teamReport.entity.vo.TeamReportVO;
import com.caac.weeklyreport.biz.teamReport.mapper.TeamReportMapper;
import com.caac.weeklyreport.biz.teamReport.service.ITeamReportService;
import com.caac.weeklyreport.biz.user.entity.UserInfo;
import com.caac.weeklyreport.common.ResultCode;
import com.caac.weeklyreport.common.enums.CommonConstants;
import com.caac.weeklyreport.exception.BusinessException;
import com.caac.weeklyreport.util.KeyGeneratorUtil;
import com.caac.weeklyreport.util.LogBacks;
import com.caac.weeklyreport.util.UserContext;
import com.caac.weeklyreport.util.WeekDateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 团队周报表 服务实现类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-07-24
 */
@Service
public class TeamReportServiceImpl extends ServiceImpl<TeamReportMapper, TeamReport> implements ITeamReportService {
    @Autowired
    private TeamReportMapper teamReportMapper;

    @Autowired
    private PersonalReportMapper personalReportMapper;

    @Transactional
    @Override
    public TeamReport saveTeamReportDraft(TeamReportVO teamReportVO){
        // 不能修改其他人的周报
        UserInfo userInfo = UserContext.getCurrentUser();
        if(!"2".equals(userInfo.getRoleType())) {
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }
        if(!userInfo.getTeamId().equals(teamReportVO.getTeamId())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }

        if(!userInfo.getUserId().equals(teamReportVO.getUserId())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }

        int week = teamReportVO.getWeek();
        int year = teamReportVO.getYear();

        TeamReport existingDraft = getTeamDraftByUserIdAndWeek(teamReportVO.getTeamId(), week, year);

        if (existingDraft != null) {
            // 当前状态为已通过审批，不能修改
            if(CommonConstants.CURRENT_STATUS_SUBMIT.equals(existingDraft.getCurrentStatus())) {
                throw new BusinessException(ResultCode.FLOW_ACCESS_DENY_SUBMIT);
            }

            BeanUtils.copyProperties(teamReportVO, existingDraft);
            existingDraft.setUpdatedAt(LocalDateTime.now());
            teamReportMapper.updateById(existingDraft);


            return getTeamReportById(existingDraft.getTrId());
        } else {
            String trId = KeyGeneratorUtil.generateUUID();

            TeamReport teamReport = new TeamReport();
            BeanUtils.copyProperties(teamReportVO, teamReport);
            // 创建新草稿
            teamReport.setTrId(trId);
            teamReport.setUserName(userInfo.getUserName());
            teamReport.setTeamId(userInfo.getTeamId());
            teamReport.setTeamName(userInfo.getTeamName());
            teamReport.setDeptId(userInfo.getDeptId());
            teamReport.setDeptName(userInfo.getDeptName());
            teamReport.setStartDate(WeekDateUtils.getStartDateOfWeek(week,year));
            teamReport.setEndDate(WeekDateUtils.getEndDateOfWeek(week,year));
            teamReport.setCreatedAt(LocalDateTime.now());
            teamReport.setUpdatedAt(LocalDateTime.now());
            teamReport.setCurrentStatus(CommonConstants.CURRENT_STATUS_DRAFT);
            teamReport.setIsDeleted("0");
            teamReportMapper.insert(teamReport);

            return getTeamReportById(trId);
        }

    }

    @Transactional
    @Override
    public TeamReport submitTeamReport(TeamReportVO teamReportVO){
        // 不能修改其他人的周报
        UserInfo userInfo = UserContext.getCurrentUser();
        if(!"2".equals(userInfo.getRoleType())) {
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }

        if(!userInfo.getTeamId().equals(teamReportVO.getTeamId())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }

        if(!userInfo.getUserId().equals(teamReportVO.getUserId())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }

        int week = teamReportVO.getWeek();
        int year = teamReportVO.getYear();

        TeamReport existingDraft = getTeamDraftByUserIdAndWeek(teamReportVO.getTeamId(), week, year);

        if (existingDraft != null) {
            // 当前状态为已通过审批，不能修改
            if(CommonConstants.CURRENT_STATUS_SUBMIT.equals(existingDraft.getCurrentStatus())) {
                throw new BusinessException(ResultCode.FLOW_ACCESS_DENY_SUBMIT);
            }

            BeanUtils.copyProperties(teamReportVO, existingDraft);
            existingDraft.setUpdatedAt(LocalDateTime.now());
            existingDraft.setCurrentStatus(CommonConstants.CURRENT_STATUS_SUBMIT);
            teamReportMapper.updateById(existingDraft);


            return getTeamReportById(existingDraft.getTrId());
        } else {
            String trId = KeyGeneratorUtil.generateUUID();

            TeamReport teamReport = new TeamReport();
            BeanUtils.copyProperties(teamReportVO, teamReport);
            // 创建新草稿
            teamReport.setTrId(trId);
            teamReport.setUserName(userInfo.getUserName());
            teamReport.setTeamId(userInfo.getTeamId());
            teamReport.setTeamName(userInfo.getTeamName());
            teamReport.setDeptId(userInfo.getDeptId());
            teamReport.setDeptName(userInfo.getDeptName());
            teamReport.setStartDate(WeekDateUtils.getStartDateOfWeek(week,year));
            teamReport.setEndDate(WeekDateUtils.getEndDateOfWeek(week,year));
            teamReport.setCreatedAt(LocalDateTime.now());
            teamReport.setUpdatedAt(LocalDateTime.now());
            teamReport.setCurrentStatus(CommonConstants.CURRENT_STATUS_SUBMIT);
            teamReport.setIsDeleted("0");
            teamReportMapper.insert(teamReport);

            return getTeamReportById(trId);
        }

    }


    @Override
    public TeamReportWeekDTO getCurrentStatusAndWeeklyReport(int year, int week) {
        TeamReportWeekDTO teamReportWeekDTO = new TeamReportWeekDTO();
        UserInfo userInfo = UserContext.getCurrentUser();
        TeamReport currentTeamReport = getTeamDraftByUserIdAndWeek(userInfo.getTeamId(), week, year);
        // 没有暂存团队周报
        if(currentTeamReport == null){
            teamReportWeekDTO.setCurrentStatus(CommonConstants.CURRENT_STATUS_DRAFT);
        } else {// 有暂存或以提交的团队周报
            teamReportWeekDTO.setCurrentStatus(currentTeamReport.getCurrentStatus());
            teamReportWeekDTO.setCurrentWeekTeamReport(currentTeamReport);
        }

        // 当前状态为已提交，前端不允许用户打开
        if (CommonConstants.CURRENT_STATUS_SUBMIT.equals(teamReportWeekDTO.getCurrentStatus())) {
            teamReportWeekDTO.setCanOperate(Boolean.FALSE);
        } else {
            teamReportWeekDTO.setCanOperate(Boolean.TRUE);
        }

        TeamReport lastWeekTeamReport = null;
        if (week == 1) {
            int lastYearTotalWeek = WeekDateUtils.getTotalWeeksInYear(year-1);
            lastWeekTeamReport = getTeamDraftByUserIdAndWeek(userInfo.getTeamId(), lastYearTotalWeek, year-1);
        } else {
            lastWeekTeamReport = getTeamDraftByUserIdAndWeek(userInfo.getTeamId(), week-1, year);
        }
        teamReportWeekDTO.setLastWeekTeamReport(lastWeekTeamReport);

        QueryWrapper<PersonalReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", userInfo.getTeamId())
                .eq("current_status", CommonConstants.CURRENT_STATUS_SUBMIT)
                .eq("week", week)
                .eq("year", year);
        List<PersonalReport> personalReports = personalReportMapper.selectList(queryWrapper);
        // 封装拼接数据
        StringBuilder major =  new StringBuilder();
        StringBuilder specialMajor = new StringBuilder();
        StringBuilder construction = new StringBuilder();
        StringBuilder specialConstruction = new StringBuilder();
        StringBuilder others = new StringBuilder();
        StringBuilder specialOthers = new StringBuilder();
        StringBuilder nextMajor= new StringBuilder();
        StringBuilder nextSpecialMajor = new StringBuilder();
        StringBuilder nextConstruction = new StringBuilder();
        StringBuilder nextSpecialConstruction = new StringBuilder();
        StringBuilder nextOthers = new StringBuilder();
        StringBuilder nextSpecialOthers = new StringBuilder();

        if(!personalReports.isEmpty()) {
            for (int i = 0; i < personalReports.size(); i++) {
                PersonalReport report = personalReports.get(i);
                String userNamePart = " (" + report.getUserName() + ")";
                boolean isLast = (personalReports.size() == 1 || i == personalReports.size() - 1);
                String separator = isLast ? "" : System.lineSeparator();

                appendIfNotEmpty(major, report.getMajor(), userNamePart, separator);
                appendIfNotEmpty(specialMajor, report.getSpecialMajor(), userNamePart, separator);
                appendIfNotEmpty(construction, report.getConstruction(), userNamePart, separator);
                appendIfNotEmpty(specialConstruction, report.getSpecialConstruction(), userNamePart, separator);
                appendIfNotEmpty(others, report.getOthers(), userNamePart, separator);
                appendIfNotEmpty(specialOthers, report.getSpecialOthers(), userNamePart, separator);
                appendIfNotEmpty(nextMajor, report.getNextMajor(), userNamePart, separator);
                appendIfNotEmpty(nextSpecialMajor, report.getNextSpecialMajor(), userNamePart, separator);
                appendIfNotEmpty(nextConstruction, report.getNextConstruction(), userNamePart, separator);
                appendIfNotEmpty(nextSpecialConstruction, report.getNextSpecialConstruction(), userNamePart, separator);
                appendIfNotEmpty(nextOthers, report.getNextOthers(), userNamePart, separator);
                appendIfNotEmpty(nextSpecialOthers, report.getNextSpecialOthers(), userNamePart, separator);
            }

            PersonalReport currentWeekPersonalReport = new PersonalReport();
            currentWeekPersonalReport.setMajor(major.toString());
            currentWeekPersonalReport.setSpecialMajor(specialMajor.toString());
            currentWeekPersonalReport.setConstruction(construction.toString());
            currentWeekPersonalReport.setSpecialConstruction(specialConstruction.toString());
            currentWeekPersonalReport.setOthers(others.toString());
            currentWeekPersonalReport.setSpecialOthers(specialOthers.toString());
            currentWeekPersonalReport.setNextMajor(nextMajor.toString());
            currentWeekPersonalReport.setNextSpecialMajor(nextSpecialMajor.toString());
            currentWeekPersonalReport.setNextConstruction(nextConstruction.toString());
            currentWeekPersonalReport.setNextSpecialConstruction(nextSpecialConstruction.toString());
            currentWeekPersonalReport.setNextOthers(nextOthers.toString());
            currentWeekPersonalReport.setNextSpecialOthers(nextSpecialOthers.toString());
            teamReportWeekDTO.setCurrentWeekPersonalReport(currentWeekPersonalReport);
        }

        return teamReportWeekDTO;
    }

    @Override
    public TeamReport getWeeklyReportByTime(int year, int week, String teamId) {
        UserInfo userInfo = UserContext.getCurrentUser();
        if(!teamId.equals(userInfo.getTeamId())) {
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }
        TeamReport teamReport = getTeamDraftByUserIdAndWeek(teamId, week,year);
        if(teamReport == null){
            throw new BusinessException(ResultCode.REPORT_IS_NULL);
        }
        return teamReport;
    }

    @Override
    public void exportTeamReportExcel(String teamId, int startWeek, int endWeek,int year, HttpServletResponse response) {
        List<TeamReportExcelDTO> resultList  = teamReportMapper.getExportTeamReport(teamId,startWeek,endWeek,year);

        try {
            //HttpServletResponse消息头参数设置
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
            String fileName = "团队周报第"+startWeek+"-"+endWeek+"周报"+ ".xlsx";
            fileName = new String(fileName.getBytes(), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName );
            EasyExcel.write(response.getOutputStream(), PersonalReportExcelDTO.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 启用自适应
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("团队周报")
                    .doWrite(resultList);
        } catch (Exception e) {
            LogBacks.error(e.getMessage());
        }
    }

    public TeamReport getTeamDraftByUserIdAndWeek(String teamId, int week, int year)  {
        QueryWrapper<TeamReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId)
                .eq("week", week)
                .eq("is_deleted", "0")
                .eq("year", year);
        return teamReportMapper.selectOne(queryWrapper);
    }

    public TeamReport getTeamReportById(String id) {
        return teamReportMapper.selectById(id);
    }

    // 辅助方法
    private void appendIfNotEmpty(StringBuilder builder, String value, String suffix, String separator) {
        if (value != null && !value.trim().isEmpty()) {
            builder.append(value).append(suffix).append(separator);
        }
    }
}
