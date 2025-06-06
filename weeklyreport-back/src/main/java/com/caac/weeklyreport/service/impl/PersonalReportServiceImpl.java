package com.caac.weeklyreport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.common.ResultCode;
import com.caac.weeklyreport.entity.FlowHistory;
import com.caac.weeklyreport.entity.FlowRecord;
import com.caac.weeklyreport.entity.PersonalReport;
import com.caac.weeklyreport.entity.UserInfo;
import com.caac.weeklyreport.exception.BusinessException;
import com.caac.weeklyreport.mapper.PersonalReportMapper;
import com.caac.weeklyreport.service.IPersonalReportService;
import com.caac.weeklyreport.util.KeyGeneratorUtil;
import com.caac.weeklyreport.util.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        personalReportMapper.updateById(personalReport);
    }

    @Override
    @Transactional
    public PersonalReport savePersonalReportDraft(PersonalReport personalReport) {
        // 不能修改其他人的周报
        UserInfo userInfo = UserContext.getCurrentUser();
        if(!userInfo.getUserId().equals(personalReport.getUserId())){
            throw new BusinessException(ResultCode.ACCESS_ILLEGAL);
        }

        PersonalReport existingDraft = getDraftByUserIdAndWeek(personalReport.getUserId(), personalReport.getWeek());

        if (existingDraft != null) {
            // 更新草稿
            personalReport.setPrId(existingDraft.getPrId());
            personalReport.setUpdatedAt(LocalDateTime.now());
            personalReport.setIsDeleted("0");
            personalReportMapper.updateById(personalReport);
            return getPersonalReportById(existingDraft.getPrId());
        } else {
            String flowId = KeyGeneratorUtil.generateUUID();
            String prId = KeyGeneratorUtil.generateUUID();

            // 创建新草稿
            personalReport.setPrId(prId);
            personalReport.setFlowId(flowId);
            personalReport.setCreatedAt(LocalDateTime.now());
            personalReport.setUpdatedAt(LocalDateTime.now());
            personalReport.setIsDeleted("0");
            personalReportMapper.insert(personalReport);

            //创建新流程
            FlowRecord flowRecord = new FlowRecord();
            flowRecord.setFlowId(flowId);
            flowRecord.setReportId(prId);
            flowRecord.setReportType("1"); //1-个人周报,2-团队周报,3-部门周报
            flowRecord.setCurrentStatus("0"); //0-草稿,1-待审核,2-已审核,3-已退回
            flowRecord.setCurrentStage("1"); //1-员工提交,2-团队审核,3-部门审核
            flowRecord.setSubmitterId(userInfo.getUserId());
            flowRecord.setSubmitterName(userInfo.getUserName());
            flowRecord.setCreatedAt(LocalDateTime.now());
            flowRecord.setUpdatedAt(LocalDateTime.now());
            flowRecord.setIsDeleted("0");

            //创建新历史记录
            FlowHistory flowHistory = new FlowHistory();
            flowHistory.setFlowId(KeyGeneratorUtil.generateUUID());
            flowHistory.setReportId(prId);
            flowHistory.setReportType("1");//1-个人周报,2-团队周报,3-部门周报
            flowHistory.setOperation("1");//1-保存为草稿,2-提交,3-通过,4-退回


            return getPersonalReportById(personalReport.getPrId());
        }
    }

    @Override
    public PersonalReport getDraftByUserIdAndWeek(String userId, int week) {
        QueryWrapper<PersonalReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("week", week)
                   .eq("status", "draft")
                   .eq("is_deleted", "0");
        return personalReportMapper.selectOne(queryWrapper);
    }
}