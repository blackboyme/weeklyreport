package com.caac.weeklyreport.biz.dept.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.caac.weeklyreport.biz.dept.entity.Dept;
import com.caac.weeklyreport.biz.dept.mapper.DeptMapper;
import com.caac.weeklyreport.biz.dept.service.IDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.biz.team.entity.Team;
import com.caac.weeklyreport.biz.team.mapper.TeamMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-07-26
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {
    private final DeptMapper deptMapper;

    public DeptServiceImpl(DeptMapper deptMapper) {
        this.deptMapper = deptMapper;
    }

    @Override
    public List<Dept> getAllDepts() {
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", "0");
        return deptMapper.selectList(queryWrapper);
    }
}
