package com.example.hrms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.hrms.entity.Employee;
import java.util.List;
import java.util.Map;

/**
 * 统计管理服务（整合所有查询结果到主实体）
 */
// 注意：这里是 interface，不是 class
public interface StatsService extends IService<Employee> {
    // 只定义方法签名，不写实现
    List<Map<String, Object>> getDeptDistribution();
    Map<String, Integer> getGenderDistribution();
    Map<String, Integer> getAgeDistribution();
    Map<String, Integer> getStatusDistribution();
    Map<String, Object> getDeptSalaryDistribution();
    Map<String, Object> getEntryLeaveTrend(String timeRange);
    Map<String, Object> getEmployeeStats(String timeRange);
}