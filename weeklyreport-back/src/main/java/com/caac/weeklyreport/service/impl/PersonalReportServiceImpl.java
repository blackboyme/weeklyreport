package com.caac.weeklyreport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.entity.PersonalReport;
import com.caac.weeklyreport.mapper.PersonalReportMapper;
import com.caac.weeklyreport.service.IPersonalReportService;
import org.springframework.stereotype.Service;

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

    private final PersonalReportMapper personalReportMapper;

    public PersonalReportServiceImpl(PersonalReportMapper personalReportMapper) {
        this.personalReportMapper = personalReportMapper;
    }

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
    }
}