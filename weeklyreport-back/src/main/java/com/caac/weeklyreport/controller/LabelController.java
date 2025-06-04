package com.caac.weeklyreport.controller;

import com.caac.weeklyreport.entity.Label;
import com.caac.weeklyreport.service.LabelService;
import com.caac.weeklyreport.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelController {

    private final LabelService labelService;

    @Autowired
    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    private String getCurrentUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            return TokenUtil.getUserIdFromToken(token);
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<Label> createLabel(@RequestBody Label label, HttpServletRequest request) {
        String userId = getCurrentUserId(request);
        label.setUserId(userId);
        Label createdLabel = labelService.createLabel(label);
        return new ResponseEntity<>(createdLabel, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Label> getLabelById(@PathVariable String id, HttpServletRequest request) {
        String userId = getCurrentUserId(request);
        Label label = labelService.getLabelById(id, userId);
        return label != null ? new ResponseEntity<>(label, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Label>> getAllLabels(HttpServletRequest request) {
        String userId = getCurrentUserId(request);
        List<Label> labels = labelService.getAllLabelsByUserId(userId);
        return new ResponseEntity<>(labels, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Label> updateLabel(@PathVariable String id, @RequestBody Label label, HttpServletRequest request) {
        String userId = getCurrentUserId(request);
        label.setId(id);
        Label updatedLabel = labelService.updateLabel(label, userId);
        return updatedLabel != null ? new ResponseEntity<>(updatedLabel, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable String id, HttpServletRequest request) {
        String userId = getCurrentUserId(request);
        boolean removed = labelService.deleteLabel(id, userId);
        return removed ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
