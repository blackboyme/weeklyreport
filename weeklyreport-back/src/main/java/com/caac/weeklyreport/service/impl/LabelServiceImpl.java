package com.caac.weeklyreport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.entity.Label;
import com.caac.weeklyreport.mapper.LabelMapper;
import com.caac.weeklyreport.service.LabelService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LabelServiceImpl extends ServiceImpl<LabelMapper, Label> implements LabelService {

    @Override
    public Label createLabel(Label label) {
        label.setId(UUID.randomUUID().toString());
        label.setCreatedAt(LocalDateTime.now());
        label.setUpdatedAt(LocalDateTime.now());
        this.save(label);
        return label;
    }

    @Override
    public Label getLabelById(String id, String userId) {
        QueryWrapper<Label> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).eq("userId", userId);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<Label> getAllLabelsByUserId(String userId) {
        QueryWrapper<Label> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        return this.list(queryWrapper);
    }

    @Override
    public Label updateLabel(Label label, String userId) {
        QueryWrapper<Label> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", label.getId()).eq("userId", userId);
        label.setUpdatedAt(LocalDateTime.now());
        if (this.update(label, queryWrapper)) {
            return this.getById(label.getId());
        }
        return null;
    }

    @Override
    public boolean deleteLabel(String id, String userId) {
        QueryWrapper<Label> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).eq("userId", userId);
        return this.remove(queryWrapper);
    }

    @Override
    public Label getUpdatedLabel(Label label, String userId) {
        Label existingLabel = this.getLabelById(label.getId(), userId);
        if (existingLabel != null) {
            existingLabel.setName(label.getName());
            existingLabel.setUpdatedAt(LocalDateTime.now());
            boolean updated = this.updateById(existingLabel);
            if (updated) {
                return existingLabel;
            }
        }
        return null;
    }
}
