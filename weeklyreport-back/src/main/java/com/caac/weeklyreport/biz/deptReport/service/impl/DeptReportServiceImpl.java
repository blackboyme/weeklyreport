package com.caac.weeklyreport.biz.deptReport.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.biz.deptReport.entity.DeptReport;
import com.caac.weeklyreport.biz.deptReport.entity.dto.DeptReportExcelDTO;
import com.caac.weeklyreport.biz.deptReport.entity.dto.DeptReportWeekDTO;
import com.caac.weeklyreport.biz.deptReport.entity.vo.DeptReportVO;
import com.caac.weeklyreport.biz.deptReport.mapper.DeptReportMapper;
import com.caac.weeklyreport.biz.deptReport.service.IDeptReportService;
import com.caac.weeklyreport.biz.personalReport.entity.dto.PersonalReportExcelDTO;
import com.caac.weeklyreport.biz.teamReport.entity.TeamReport;
import com.caac.weeklyreport.biz.teamReport.entity.dto.TeamReportExcelDTO;
import com.caac.weeklyreport.biz.teamReport.mapper.TeamReportMapper;
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
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 部门周报表 服务实现类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-07-24
 */
@Service
public class DeptReportServiceImpl extends ServiceImpl<DeptReportMapper, DeptReport> implements IDeptReportService {
    @Autowired
    private DeptReportMapper deptReportMapper;

    @Autowired
    private TeamReportMapper teamReportMapper;


    @Override
    @Transactional
    public DeptReport saveDeptReportDraft(DeptReportVO deptReportVO) {
        // 不能修改其他人的周报
        UserInfo userInfo = UserContext.getCurrentUser();

        if(!userInfo.getDeptId().equals(deptReportVO.getDeptId())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }

        int week = deptReportVO.getWeek();
        int year = deptReportVO.getYear();

        DeptReport existingDraft = getDeptDraftByUserIdAndWeek(deptReportVO.getDeptId(), week, year);

        if (existingDraft != null) {
            BeanUtils.copyProperties(deptReportVO, existingDraft);
            existingDraft.setUpdatedAt(LocalDateTime.now());
            existingDraft.setCurrentStatus(CommonConstants.CURRENT_STATUS_DRAFT);
            deptReportMapper.updateById(existingDraft);

            return getDeptReportById(existingDraft.getDrId());
        } else {
            String drId = KeyGeneratorUtil.generateUUID();

            DeptReport deptReport = new DeptReport();
            BeanUtils.copyProperties(deptReportVO, deptReport);
            // 创建新草稿
            deptReport.setDrId(drId);
            deptReport.setUserId(userInfo.getUserId());
            deptReport.setUserName(userInfo.getUserName());
            deptReport.setDeptId(userInfo.getDeptId());
            deptReport.setDeptName(userInfo.getDeptName());
            deptReport.setStartDate(WeekDateUtils.getStartDateOfWeek(week,year));
            deptReport.setEndDate(WeekDateUtils.getEndDateOfWeek(week,year));
            deptReport.setCreatedAt(LocalDateTime.now());
            deptReport.setUpdatedAt(LocalDateTime.now());
            deptReport.setCurrentStatus(CommonConstants.CURRENT_STATUS_DRAFT);
            deptReport.setIsDeleted("0");
            deptReportMapper.insert(deptReport);

            return getDeptReportById(drId);
        }
    }

    @Override
    @Transactional
    public DeptReport saveDeptReport(DeptReportVO deptReportVO) {
        // 不能修改其他人的周报
        UserInfo userInfo = UserContext.getCurrentUser();

        if(!userInfo.getDeptId().equals(deptReportVO.getDeptId())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }

        int week = deptReportVO.getWeek();
        int year = deptReportVO.getYear();

        DeptReport existingDraft = getDeptDraftByUserIdAndWeek(deptReportVO.getDeptId(), week, year);

        if (existingDraft != null) {
            BeanUtils.copyProperties(deptReportVO, existingDraft);
            existingDraft.setUpdatedAt(LocalDateTime.now());
            existingDraft.setCurrentStatus(CommonConstants.CURRENT_STATUS_SUBMIT);
            deptReportMapper.updateById(existingDraft);

            return getDeptReportById(existingDraft.getDrId());
        } else {
            String drId = KeyGeneratorUtil.generateUUID();

            DeptReport deptReport = new DeptReport();
            BeanUtils.copyProperties(deptReportVO, deptReport);
            // 创建新草稿
            deptReport.setDrId(drId);
            deptReport.setUserId(userInfo.getUserId());
            deptReport.setUserName(userInfo.getUserName());
            deptReport.setDeptId(userInfo.getDeptId());
            deptReport.setDeptName(userInfo.getDeptName());
            deptReport.setStartDate(WeekDateUtils.getStartDateOfWeek(week,year));
            deptReport.setEndDate(WeekDateUtils.getEndDateOfWeek(week,year));
            deptReport.setCreatedAt(LocalDateTime.now());
            deptReport.setUpdatedAt(LocalDateTime.now());
            deptReport.setCurrentStatus(CommonConstants.CURRENT_STATUS_SUBMIT);
            deptReport.setIsDeleted("0");
            deptReportMapper.insert(deptReport);

            return getDeptReportById(drId);
        }
    }

    @Override
    public DeptReport getWeeklyReportByTime(int year, int week) {
        UserInfo userInfo = UserContext.getCurrentUser();
        if(!"3".equals(userInfo.getRoleType())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }
        DeptReport deptReport = getDeptDraftByUserIdAndWeek(userInfo.getDeptId(), week,year);
        if(deptReport == null){
            throw new BusinessException(ResultCode.REPORT_IS_NULL);
        }
        return deptReport;
    }

    @Override
    public DeptReportWeekDTO getCurrentStatusAndDeptReport(int year, int week) {
        DeptReportWeekDTO deptReportWeekDTO = new DeptReportWeekDTO();
        UserInfo userInfo = UserContext.getCurrentUser();
        DeptReport currentDeptReport = getDeptDraftByUserIdAndWeek(userInfo.getDeptId(), week, year);
        // 没有暂存部门周报
        if(currentDeptReport == null){
            deptReportWeekDTO.setCurrentStatus(CommonConstants.CURRENT_STATUS_DRAFT);
        } else {// 有暂存或以提交的部门周报
            deptReportWeekDTO.setCurrentStatus(currentDeptReport.getCurrentStatus());
            deptReportWeekDTO.setCurrentWeekDeptReport(currentDeptReport);
        }

        // 当前状态为已提交，前端不允许用户打开
        if (CommonConstants.CURRENT_STATUS_SUBMIT.equals(deptReportWeekDTO.getCurrentStatus())) {
            deptReportWeekDTO.setCanOperate(Boolean.FALSE);
        } else {
            deptReportWeekDTO.setCanOperate(Boolean.TRUE);
        }

        QueryWrapper<TeamReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id", userInfo.getDeptId())
                .eq("week", week)
                .eq("is_deleted", "0")
                .apply("year", year);

        List<TeamReport> teamReports = teamReportMapper.selectList(queryWrapper);
        deptReportWeekDTO.setTeamReports(teamReports);

        return deptReportWeekDTO;
    }

    @Override
    public void exportDeptReportExcel(String deptId, int startWeek, int endWeek,int year, HttpServletResponse response) {
        List<DeptReportExcelDTO> resultList  = deptReportMapper.getExportDeptReport(deptId,startWeek,endWeek,year);

        try {
            //HttpServletResponse消息头参数设置
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
            String fileName = "部门周报第"+startWeek+"-"+endWeek+"周报"+ ".xlsx";
            fileName = new String(fileName.getBytes(), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName );
            EasyExcel.write(response.getOutputStream(), PersonalReportExcelDTO.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 启用自适应
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("部门周报")
                    .doWrite(resultList);
        } catch (Exception e) {
            LogBacks.error(e.getMessage());
        }
    }

    public DeptReport getDeptDraftByUserIdAndWeek(String deptId, int week, int year) {
        QueryWrapper<DeptReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id", deptId)
                .eq("week", week)
                .eq("is_deleted", "0")
                .eq("year", year);
        return deptReportMapper.selectOne(queryWrapper);
    }

    public DeptReport getDeptReportById(String id) {
        return deptReportMapper.selectById(id);
    }
}
