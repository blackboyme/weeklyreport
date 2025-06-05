package com.caac.weeklyreport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.entity.FlowRecord;
import com.caac.weeklyreport.mapper.FlowRecordMapper;
import com.caac.weeklyreport.service.IFlowRecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 流程记录表 服务实现类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@Service
public class FlowRecordServiceImpl extends ServiceImpl<FlowRecordMapper, FlowRecord> implements IFlowRecordService {

    private final FlowRecordMapper flowRecordMapper;

    public FlowRecordServiceImpl(FlowRecordMapper flowRecordMapper) {
        this.flowRecordMapper = flowRecordMapper;
    }

    @Override
    public FlowRecord createFlowRecord(FlowRecord flowRecord) {
        flowRecord.setFlowId(UUID.randomUUID().toString());
        flowRecord.setCreatedAt(LocalDateTime.now());
        flowRecord.setUpdatedAt(LocalDateTime.now());
        flowRecord.setIsDeleted("0");
        flowRecordMapper.insert(flowRecord);
        return getFlowRecordById(flowRecord.getFlowId());
    }

    @Override
    public FlowRecord getFlowRecordById(String id) {
        return flowRecordMapper.selectById(id);
    }

    @Override
    public List<FlowRecord> getAllFlowRecords() {
        QueryWrapper<FlowRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", "0");
        return flowRecordMapper.selectList(queryWrapper);
    }

    @Override
    public FlowRecord updateFlowRecord(FlowRecord flowRecord) {
        flowRecord.setUpdatedAt(LocalDateTime.now());
        flowRecordMapper.updateById(flowRecord);
        return getFlowRecordById(flowRecord.getFlowId());
    }

    @Override
    public void deleteFlowRecord(String id) {
        FlowRecord flowRecord = new FlowRecord();
        flowRecord.setFlowId(id);
        flowRecord.setIsDeleted("1");
        flowRecord.setUpdatedAt(LocalDateTime.now());
        flowRecordMapper.updateById(flowRecord);
    }
}
