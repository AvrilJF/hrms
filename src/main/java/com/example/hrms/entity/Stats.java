package com.example.hrms.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 统计管理核心实体（子实体保持默认权限，仅主实体对外暴露）
 */
@Data
public class Stats {
    // 核心指标（截图6个卡片）
    private Integer totalEmployee; // 总员工数
    private Integer avgAge; // 平均年龄
    private BigDecimal avgSalary; // 平均薪资
    private BigDecimal totalSalary; // 薪资总支出
    private Integer onJobEmployee; // 在职员工数
    private Integer totalDept; // 部门总数

    // 维度分析（子实体为包级私有）
    private List<DeptDistribution> deptDistribution; // 部门分布
    private List<GenderDistribution> genderDistribution; // 性别分布
    private List<AgeDistribution> ageDistribution; // 年龄分布
    private List<StatusDistribution> statusDistribution; // 状态分布
    private List<DeptSalary> deptSalary; // 部门薪资
    private List<EntryLeaveTrend> entryLeaveTrend; // 入离职趋势

    // 所有子实体保持默认权限（包级私有）
    @Data
    class DeptDistribution {
        private String deptName;
        private Integer employeeCount;
        private Double percentage;
    }

    @Data
    class GenderDistribution {
        private String gender;
        private Integer count;
        private Double percentage;
    }

    @Data
    class AgeDistribution {
        private String ageGroup;
        private Integer count;
        private Double percentage;
    }

    @Data
    class StatusDistribution {
        private String statusName;
        private Integer count;
        private Double percentage;
    }

    @Data
    class DeptSalary {
        private String deptName;
        private BigDecimal avgSalary;
        private BigDecimal totalSalary;
    }

    @Data
    class EntryLeaveTrend {
        private String month;
        private Integer entryCount;
        private Integer leaveCount;
    }
}