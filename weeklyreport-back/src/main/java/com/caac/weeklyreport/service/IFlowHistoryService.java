package com.caac.weeklyreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.entity.FlowHistory;

import java.util.List;

/**
 * <p>
 * 流程历史记录表 服务类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
public interface IFlowHistoryService extends IService<FlowHistory> {
    FlowHistory createFlowHistory(FlowHistory flowHistory);
    FlowHistory getFlowHistoryById(String id);
    List<FlowHistory> getAllFlowHistories();
    FlowHistory updateFlowHistory(FlowHistory flowHistory);
    void deleteFlowHistory(String id);
    List<FlowHistory> getFlowHistoriesByFlowId(String flowId);
}
