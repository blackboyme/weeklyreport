package com.caac.weeklyreport.controller;

import com.caac.weeklyreport.entity.FlowHistory;
import com.caac.weeklyreport.service.IFlowHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 流程历史记录表 前端控制器
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@RestController
@RequestMapping("/api/v1/flow-history")
public class FlowHistoryController {
    
    private final IFlowHistoryService flowHistoryService;

    public FlowHistoryController(IFlowHistoryService flowHistoryService) {
        this.flowHistoryService = flowHistoryService;
    }

    @PostMapping
    public ResponseEntity<FlowHistory> createFlowHistory(@RequestBody FlowHistory flowHistory) {
        return ResponseEntity.ok(flowHistoryService.createFlowHistory(flowHistory));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlowHistory> getFlowHistory(@PathVariable String id) {
        FlowHistory flowHistory = flowHistoryService.getFlowHistoryById(id);
        return flowHistory != null ? ResponseEntity.ok(flowHistory) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<FlowHistory>> getAllFlowHistories() {
        return ResponseEntity.ok(flowHistoryService.getAllFlowHistories());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlowHistory> updateFlowHistory(@PathVariable String id, @RequestBody FlowHistory flowHistory) {
        flowHistory.setHistoryId(id);
        return ResponseEntity.ok(flowHistoryService.updateFlowHistory(flowHistory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlowHistory(@PathVariable String id) {
        flowHistoryService.deleteFlowHistory(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/flow/{flowId}")
    public ResponseEntity<List<FlowHistory>> getFlowHistoriesByFlowId(@PathVariable String flowId) {
        return ResponseEntity.ok(flowHistoryService.getFlowHistoriesByFlowId(flowId));
    }
}
