package com.caac.weeklyreport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caac.weeklyreport.entity.FlowRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 流程记录表 Mapper 接口
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@Mapper
public interface FlowRecordMapper extends BaseMapper<FlowRecord> {

    int updateStatus(@Param("flowId") String flowId, @Param("status") String status,
                     @Param("approverId") String approverId,@Param("approverName")String approverName);

}
