package com.example.hrms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hrms.entity.Employee;
import com.example.hrms.mapper.EmployeeMapper;
import com.example.hrms.service.StatsService;
import org.springframework.stereotype.Service;

//import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public Map<String, Integer> getStatusDistribution() {
        return employeeMapper.getStatusDistribution();
    }

    @Override
    public Map<String, Object> getDeptSalaryDistribution() {
        List<Map<String, Object>> list = employeeMapper.getDeptSalaryDistribution();
        List<String> deptNames = new ArrayList<>();
        List<BigDecimal> totalSalaries = new ArrayList<>();
        List<BigDecimal> avgSalaries = new ArrayList<>();

        for (Map<String, Object> map : list) {
            deptNames.add(map.get("deptName").toString());
            totalSalaries.add((BigDecimal) map.get("totalSalary"));
            avgSalaries.add((BigDecimal) map.get("avgSalary"));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("deptNames", deptNames);
        result.put("totalSalaries", totalSalaries);
        result.put("avgSalaries", avgSalaries);
        return result;
    }

    @Override
    public Map<String, Object> getEntryLeaveTrend(String timeRange) {
        // 拼接时间筛选条件（月度：当前月，季度：当前季度，年度：当前年）
        LocalDateTime now = LocalDateTime.now();
        String time = "";
        if ("month".equals(timeRange)) {
            time = now.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "%";
        } else if ("quarter".equals(timeRange)) {
            int quarter = (now.getMonthValue() - 1) / 3 + 1;
            time = now.getYear() + "-" + (quarter * 3 - 2) + "%";
        } else if ("year".equals(timeRange)) {
            time = now.getYear() + "%";
        }

        List<Map<String, Object>> list = employeeMapper.getEntryLeaveTrend(time);
        Map<String, Integer> entryMap = new HashMap<>();
        Map<String, Integer> leaveMap = new HashMap<>();
        List<String> months = Arrays.asList("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月");
        List<Integer> entryCount = new ArrayList<>();
        List<Integer> leaveCount = new ArrayList<>();

        // 初始化数据
        for (String month : months) {
            entryMap.put(month, 0);
            leaveMap.put(month, 0);
        }

        // 填充数据
        for (Map<String, Object> map : list) {
            String month = map.get("month").toString();
            if (map.containsKey("entryCount")) {
                entryMap.put(month, Integer.parseInt(map.get("entryCount").toString()));
            }
            if (map.containsKey("leaveCount")) {
                leaveMap.put(month, Integer.parseInt(map.get("leaveCount").toString()));
            }
        }

        // 组装返回数据
        for (String month : months) {
            entryCount.add(entryMap.get(month));
            leaveCount.add(leaveMap.get(month));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("months", months);
        result.put("entryCount", entryCount);
        result.put("leaveCount", leaveCount);
        return result;
    }

//    @Override
//    public Map<String, Object> getEmployeeStats(String timeRange) {
//        Map<String, Object> stats = employeeMapper.getEmployeeStats();
//        // 模拟增长率（实际项目中需从历史数据计算）
//        stats.put("totalGrowth", 5);
//        stats.put("salaryGrowth", 3);
//        return stats;
//    }
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