package com.caac.weeklyreport.biz.deptReport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.biz.deptReport.entity.DeptReport;
import com.caac.weeklyreport.biz.deptReport.entity.vo.DeptReportVO;
import com.caac.weeklyreport.biz.deptReport.mapper.DeptReportMapper;
import com.caac.weeklyreport.biz.deptReport.service.IDeptReportService;
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


    @Override
    @Transactional
    public DeptReport saveDeptReportDraft(DeptReportVO deptReportVO) {
        // 不能修改其他人的周报
        UserInfo userInfo = UserContext.getCurrentUser();

        if(!userInfo.getDeptId().equals(deptReportVO.getDeptId())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }

        DeptReport existingDraft = getDeptDraftByUserIdAndWeek(deptReportVO.getDeptId(), deptReportVO.getWeek(), LocalDate.now().getYear());

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
            deptReport.setStartDate(WeekDateUtils.getStartDateOfWeek(deptReport.getWeek()));
            deptReport.setEndDate(WeekDateUtils.getEndDateOfWeek(deptReport.getWeek()));
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

        DeptReport existingDraft = getDeptDraftByUserIdAndWeek(deptReportVO.getDeptId(), deptReportVO.getWeek(), LocalDate.now().getYear());

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
            deptReport.setStartDate(WeekDateUtils.getStartDateOfWeek(deptReport.getWeek()));
            deptReport.setEndDate(WeekDateUtils.getEndDateOfWeek(deptReport.getWeek()));
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

    public DeptReport getDeptDraftByUserIdAndWeek(String deptId, int week, int year) {
        QueryWrapper<DeptReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id", deptId)
                .eq("week", week)
                .eq("is_deleted", "0")
                .apply("YEAR(created_at) = {0}", year);
        return deptReportMapper.selectOne(queryWrapper);
    }

    public DeptReport getDeptReportById(String id) {
        return deptReportMapper.selectById(id);
    }
}
