package com.example.hrms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hrms.entity.Employee;
import com.example.hrms.mapper.EmployeeMapper;
import com.example.hrms.service.StatsService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@Service
public class StatsServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements StatsService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<Map<String, Object>> getDeptDistribution() {
        return employeeMapper.getDeptDistribution();
    }

    @Override
    public Map<String, Integer> getGenderDistribution() {
        return employeeMapper.getGenderDistribution();
    }

    @Override
    public Map<String, Integer> getAgeDistribution() {
        return employeeMapper.getAgeDistribution();
    }

    @Override
    public List<Map<String, Object>> getStatusDistribution() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> statusData = employeeMapper.getEmployeeStatusData();

        int onDuty = 0, probation = 0, leave = 0;
        for (Map<String, Object> item : statusData) {
            Integer status = (Integer) item.get("status"); // 直接强转（避免 parseInt 报错）
            if (status == 1) onDuty++;
            else if (status == 2) probation++;
            else if (status == 3) leave++;
        }

        result.add(Map.of("name", "在职", "value", onDuty));
        result.add(Map.of("name", "试用期", "value", probation));
        result.add(Map.of("name", "离职", "value", leave));
        return result;
    }

    @Override
    public Map<String, Object> getDeptSalaryDistribution() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> dbData = employeeMapper.getDeptSalaryDistribution();

        List<String> deptNames = new ArrayList<>();
        List<Double> totalSalaries = new ArrayList<>();
        List<Double> avgSalaries = new ArrayList<>();

        for (Map<String, Object> item : dbData) {
            deptNames.add((String) item.get("deptName"));
            totalSalaries.add(((Number) item.get("totalSalary")).doubleValue());
            avgSalaries.add(((Number) item.get("avgSalary")).doubleValue());
        }

        result.put("deptNames", deptNames);
        result.put("totalSalaries", totalSalaries);
        result.put("avgSalaries", avgSalaries);
        return result;
    }

    @Override
    public Map<String, Object> getEntryLeaveTrend(String timeRange) {
        Map<String, Object> result = new HashMap<>();
        List<String> months = new ArrayList<>();
        List<Integer> entryCount = new ArrayList<>();
        List<Integer> leaveCount = new ArrayList<>();

        // 初始化 12 个月数据（默认 0）
        for (int i = 1; i <= 12; i++) {
            months.add(i + "月");
            entryCount.add(0);
            leaveCount.add(0);
        }

        // 查询数据库中的入离职数据
        List<Map<String, Object>> dbData = employeeMapper.getEntryLeaveTrendData();
        for (Map<String, Object> item : dbData) {
            String month = (String) item.get("month");
            Integer entry = ((Number) item.get("entry_count")).intValue();
            Integer leave = ((Number) item.get("leave_count")).intValue();

            // 解析月份：2026-03 → 3月 → 索引 2（从 0 开始）
            int monthNum = Integer.parseInt(month.split("-")[1]);
            int index = monthNum - 1;

            entryCount.set(index, entry);
            leaveCount.set(index, leave);
        }

        result.put("months", months);
        result.put("entryCount", entryCount);
        result.put("leaveCount", leaveCount);
        return result;
    }

    @Override
    public Map<String, Object> getEmployeeStats(String timeRange) {
        // 初始化返回结果
        Map<String, Object> result = new HashMap<>();

        // 1. 查询原始数据
        List<Map<String, Object>> baseData = employeeMapper.getEmployeeBaseData();
        Integer deptCount = employeeMapper.getDeptCount();

        // 2. 处理空数据（避免除以0）
        if (baseData == null || baseData.isEmpty()) {
            result.put("totalCount", 0);
            result.put("avgAge", 0.0);
            result.put("avgSalary", 0.0);
            result.put("deptCount", deptCount == null ? 0 : deptCount);
            result.put("onDutyCount", 0);
            result.put("onDutyRate", 0.0);
            result.put("totalSalary", 0.0);
            return result;
        }

        // 3. Java 代码做聚合计算（核心！避开 SQL 限制）
        int totalCount = baseData.size(); // 总员工数
        double sumAge = 0.0; // 年龄总和
        double sumSalary = 0.0; // 薪资总和
        int onDutyCount = 0; // 在职人数

        for (Map<String, Object> emp : baseData) {
            // 累加年龄
            Object ageObj = emp.get("age");
            if (ageObj != null) {
                sumAge += Double.parseDouble(ageObj.toString());
            }
            // 累加薪资
            Object salaryObj = emp.get("salary");
            if (salaryObj != null) {
                sumSalary += Double.parseDouble(salaryObj.toString());
            }
            // 统计在职人数（status=1）
            Object statusObj = emp.get("status");
            if (statusObj != null && "1".equals(statusObj.toString())) {
                onDutyCount++;
            }
        }

        // 4. 计算最终指标
        double avgAge = Math.round((sumAge / totalCount) * 10) / 10.0; // 保留1位小数
        double avgSalary = Math.round((sumSalary / totalCount) * 100) / 100.0; // 保留2位小数
        double onDutyRate = Math.round((onDutyCount * 1.0 / totalCount) * 1000) / 10.0; // 保留1位小数

        // 5. 封装结果
        result.put("totalCount", totalCount);
        result.put("avgAge", avgAge);
        result.put("avgSalary", avgSalary);
        result.put("deptCount", deptCount == null ? 0 : deptCount);
        result.put("onDutyCount", onDutyCount);
        result.put("onDutyRate", onDutyRate);
        result.put("totalSalary", sumSalary);
        // 补充前端需要的增长率（写死或按实际逻辑改）
        result.put("totalGrowth", 5);
        result.put("salaryGrowth", 3);

        return result;
    }
}