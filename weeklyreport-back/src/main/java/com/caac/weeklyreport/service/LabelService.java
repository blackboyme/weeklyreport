package com.caac.weeklyreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.entity.Label;

import java.util.List;

public interface LabelService extends IService<Label> {
    Label createLabel(Label label);
    Label getLabelById(String id, String userId);
    List<Label> getAllLabelsByUserId(String userId);
    Label updateLabel(Label label, String userId);
    boolean deleteLabel(String id, String userId);
    Label getUpdatedLabel(Label label, String userId);
}
