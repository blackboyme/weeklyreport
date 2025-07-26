package com.caac.weeklyreport.biz.dept.service;

import com.caac.weeklyreport.biz.dept.entity.Dept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-07-26
 */
public interface IDeptService extends IService<Dept> {
    List<Dept> getAllDepts();
}
