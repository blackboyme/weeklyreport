package com.caac.weeklyreport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.entity.FlowHistory;
import com.caac.weeklyreport.mapper.FlowHistoryMapper;
import com.caac.weeklyreport.service.IFlowHistoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 流程历史记录表 服务实现类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@Service
public class FlowHistoryServiceImpl extends ServiceImpl<FlowHistoryMapper, FlowHistory> implements IFlowHistoryService {

    private final FlowHistoryMapper flowHistoryMapper;

    public FlowHistoryServiceImpl(FlowHistoryMapper flowHistoryMapper) {
        this.flowHistoryMapper = flowHistoryMapper;
    }

    @Override
    public FlowHistory createFlowHistory(FlowHistory flowHistory) {
        flowHistory.setHistoryId(UUID.randomUUID().toString());
        flowHistory.setCreatedAt(LocalDateTime.now());
        flowHistory.setUpdatedAt(LocalDateTime.now());
        flowHistory.setIsDeleted("0");
        flowHistoryMapper.insert(flowHistory);
        return getFlowHistoryById(flowHistory.getHistoryId());
    }

    @Override
    public FlowHistory getFlowHistoryById(String id) {
        return flowHistoryMapper.selectById(id);
    }

    @Override
    public List<FlowHistory> getAllFlowHistories() {
        QueryWrapper<FlowHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", "0");
        return flowHistoryMapper.selectList(queryWrapper);
    }

    @Override
    public FlowHistory updateFlowHistory(FlowHistory flowHistory) {
        flowHistory.setUpdatedAt(LocalDateTime.now());
        flowHistoryMapper.updateById(flowHistory);
        return getFlowHistoryById(flowHistory.getHistoryId());
    }

    @Override
    public void deleteFlowHistory(String id) {
        FlowHistory flowHistory = new FlowHistory();
        flowHistory.setHistoryId(id);
        flowHistory.setIsDeleted("1");
        flowHistory.setUpdatedAt(LocalDateTime.now());
        flowHistoryMapper.updateById(flowHistory);
    }

    @Override
    public List<FlowHistory> getFlowHistoriesByFlowId(String flowId) {
        QueryWrapper<FlowHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flow_id", flowId)
                   .eq("is_deleted", "0")
                   .orderByDesc("created_at");
        return flowHistoryMapper.selectList(queryWrapper);
    }
}
