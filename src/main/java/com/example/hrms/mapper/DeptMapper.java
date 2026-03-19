package com.example.hrms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hrms.entity.SysDept; // 确保你的 Dept 实体类存在
import org.apache.ibatis.annotations.Mapper;

/**
 * 手动创建的部门 Mapper，避免代码生成器的兼容问题
 */
//@Mapper
public interface DeptMapper extends BaseMapper<SysDept> {
    // 只保留 BaseMapper 继承，无其他自定义方法（先保证启动）
}