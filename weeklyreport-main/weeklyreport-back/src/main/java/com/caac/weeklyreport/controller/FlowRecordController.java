package com.caac.weeklyreport.controller;

import com.caac.weeklyreport.entity.FlowRecord;
import com.caac.weeklyreport.service.IFlowRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 流程记录表 前端控制器
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@RestController
@RequestMapping("/api/v1/flow-record")
public class FlowRecordController {
    
    private final IFlowRecordService flowRecordService;

    public FlowRecordController(IFlowRecordService flowRecordService) {
        this.flowRecordService = flowRecordService;
    }

    @PostMapping
    public ResponseEntity<FlowRecord> createFlowRecord(@RequestBody FlowRecord flowRecord) {
        return ResponseEntity.ok(flowRecordService.createFlowRecord(flowRecord));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlowRecord> getFlowRecord(@PathVariable String id) {
        FlowRecord flowRecord = flowRecordService.getFlowRecordById(id);
        return flowRecord != null ? ResponseEntity.ok(flowRecord) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<FlowRecord>> getAllFlowRecords() {
        return ResponseEntity.ok(flowRecordService.getAllFlowRecords());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlowRecord> updateFlowRecord(@PathVariable String id, @RequestBody FlowRecord flowRecord) {
        flowRecord.setFlowId(id);
        return ResponseEntity.ok(flowRecordService.updateFlowRecord(flowRecord));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlowRecord(@PathVariable String id) {
        flowRecordService.deleteFlowRecord(id);
        return ResponseEntity.ok().build();
    }
}
