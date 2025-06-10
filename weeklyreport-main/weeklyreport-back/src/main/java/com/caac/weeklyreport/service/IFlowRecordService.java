package com.caac.weeklyreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.entity.FlowRecord;

import java.util.List;

/**
 * <p>
 * 流程记录表 服务类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
public interface IFlowRecordService extends IService<FlowRecord> {
    FlowRecord createFlowRecord(FlowRecord flowRecord);
    FlowRecord getFlowRecordById(String id);
    List<FlowRecord> getAllFlowRecords();
    FlowRecord updateFlowRecord(FlowRecord flowRecord);
    void deleteFlowRecord(String id);
}
