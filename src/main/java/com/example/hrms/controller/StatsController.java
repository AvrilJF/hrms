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
    public Map<String, Object> getEntryLeaveTrend(@RequestParam(defaultValue = "month") String timeRange) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", statsService.getEntryLeaveTrend(timeRange));
        return result;
    }

    // 员工统计
//    @GetMapping("/employeeStats")
//    public Map<String, Object> getEmployeeStats(@RequestParam(defaultValue = "month") String timeRange) {
//        Map<String, Object> result = new HashMap<>();
//        result.put("code", 200);
//        result.put("msg", "success");
//        result.put("data", statsService.getEmployeeStats(timeRange));
//        return result;
//    }
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
//    @GetMapping("/employeeStats")
//    public Map<String, Object> getEmployeeStats(@RequestParam(required = false) String timeRange) {
//        // 1. 初始化返回结果（兜底，避免返回null）
//        Map<String, Object> result = new HashMap<>();
//        try {
//            // 2. 兼容 timeRange 为空的情况（前端传空/不传都不报错）
//            String finalTimeRange = (timeRange == null || timeRange.trim().isEmpty()) ? "month" : timeRange;
//
//            // 3. 调用Service查询（加非空判断）
//            Map<String, Object> statsData = statsService.getEmployeeStats(finalTimeRange);
//
//            // 4. 如果查询结果为空，返回默认数据（避免前端渲染崩溃）
//            if (statsData == null || statsData.isEmpty()) {
//                statsData = new HashMap<>();
//                statsData.put("totalCount", 0);
//                statsData.put("avgAge", 0);
//                statsData.put("avgSalary", 0);
//                statsData.put("deptCount", 0);
//                statsData.put("onDutyCount", 0);
//                statsData.put("onDutyRate", 0);
//                statsData.put("totalSalary", 0);
//                statsData.put("totalGrowth", 0);
//                statsData.put("salaryGrowth", 0);
//            }
//            // 5. 封装成功结果
//            result.put("code", 200);
//            result.put("msg", "success");
//            result.put("data", statsData);
//        } catch (Exception e) {
//            // 6. 捕获所有异常，打印日志+返回友好提示（关键！能看到具体错在哪）
//            e.printStackTrace(); // 后端控制台会打印详细报错信息
//            result.put("code", 500);
//            result.put("msg", "查询统计数据失败：" + e.getMessage());
//            result.put("data", new HashMap<>()); // 返回空data，前端不崩溃
//        }
//        return result;
//    }


//    @GetMapping("/employeeStats") // 方法路径
//    public Map<String, Object> getEmployeeStats() {
//        // 临时写死数据，测试接口是否能访问
//        Map<String, Object> result = new HashMap<>();
//        result.put("code", 200);
//        result.put("msg", "success");
//        Map<String, Object> data = new HashMap<>();
//        data.put("totalCount", 10);
//        data.put("avgAge", 30);
//        data.put("avgSalary", 9000);
//        result.put("data", data);
//        return result;
//    }
}