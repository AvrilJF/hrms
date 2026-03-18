package com.example.hrms.controller;

import com.example.hrms.service.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * 统计管理接口（适配原有路由规范）
 */
@RestController
@RequestMapping("/admin/stats")
public class StatsController {
    @Autowired
    private StatsService statsService;

    // 部门分布
    @GetMapping("/dept")
    public Map<String, Object> getDeptDistribution() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", statsService.getDeptDistribution());
        return result;
    }

    // 性别分布
    @GetMapping("/gender")
    public Map<String, Object> getGenderDistribution() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", statsService.getGenderDistribution());
        return result;
    }

    // 年龄分布
    @GetMapping("/age")
    public Map<String, Object> getAgeDistribution() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", statsService.getAgeDistribution());
        return result;
    }

    // 状态分布
    @GetMapping("/status")
    public Map<String, Object> getStatusDistribution() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", statsService.getStatusDistribution());
        return result;
    }

    // 部门薪资
    @GetMapping("/salary")
    public Map<String, Object> getDeptSalaryDistribution() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", statsService.getDeptSalaryDistribution());
        return result;
    }

    // 入离职趋势
    @GetMapping("/trend")
    public Map<String, Object> getEntryLeaveTrend(@RequestParam(required = false) String timeRange) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> trendData = statsService.getEntryLeaveTrend(timeRange);
            result.put("code", 200);
            result.put("msg", "success");
            result.put("data", trendData);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "查询失败：" + e.getMessage());
            result.put("data", new HashMap<>());
        }
        return result;
    }

    // 员工统计
    @GetMapping("/employeeStats")
    public Map<String, Object> getEmployeeStats(@RequestParam(required = false) String timeRange) {
        Map<String, Object> result = new HashMap<>();
        try {
            String finalTimeRange = (timeRange == null || timeRange.trim().isEmpty()) ? "month" : timeRange;
            Map<String, Object> statsData = statsService.getEmployeeStats(finalTimeRange);

            result.put("code", 200);
            result.put("msg", "success");
            result.put("data", statsData);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "查询失败：" + e.getMessage());
            result.put("data", new HashMap<>());
        }
        return result;
    }
}