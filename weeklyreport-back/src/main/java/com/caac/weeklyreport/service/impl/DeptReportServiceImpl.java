package com.caac.weeklyreport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.entity.DeptReport;
import com.caac.weeklyreport.mapper.DeptReportMapper;
import com.caac.weeklyreport.service.IDeptReportService;
import org.springframework.stereotype.Service;

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
}
