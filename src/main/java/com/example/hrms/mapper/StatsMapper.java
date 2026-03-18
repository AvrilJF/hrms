package com.example.hrms.mapper;

import com.example.hrms.entity.Stats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 统计Mapper（仅返回主实体，避免跨包访问子实体）
 */
@Mapper
public interface StatsMapper {
    // 1. 查询核心指标
    @Select("""
        SELECT
            COUNT(*) AS total_employee,
            ROUND(AVG(TIMESTAMPDIFF(YEAR, birth_date, NOW())), 0) AS avg_age,
            ROUND(AVG(salary), 0) AS avg_salary,
            SUM(salary) AS total_salary,
            COUNT(CASE WHEN status = 1 THEN 1 END) AS on_job_employee,
            (SELECT COUNT(*) FROM sys_dept) AS total_dept
        FROM employee
        """)
    Stats getCoreStats();

    // 2. 查询部门分布（结果封装到Stats的deptDistribution字段）
    @Select("""
        SELECT
            d.dept_name,
            COUNT(e.id) AS employee_count,
            ROUND(IFNULL(COUNT(e.id) / (SELECT COUNT(*) FROM employee) * 100, 0), 1) AS percentage
        FROM sys_dept d
        LEFT JOIN employee e ON d.id = e.dept_id AND e.status = 1
        GROUP BY d.id, d.dept_name
        """)
    Stats getDeptDistribution();

    // 3. 查询性别分布（结果封装到Stats的genderDistribution字段）
    @Select("""
        SELECT
            gender,
            COUNT(*) AS count,
            ROUND(COUNT(*) / (SELECT COUNT(*) FROM employee WHERE status = 1) * 100, 1) AS percentage
        FROM employee
        WHERE status = 1
        GROUP BY gender
        """)
    Stats getGenderDistribution();

    // 4. 查询年龄分布（结果封装到Stats的ageDistribution字段）
    @Select("""
        SELECT
            CASE
                WHEN TIMESTAMPDIFF(YEAR, birth_date, NOW()) < 25 THEN '25岁以下'
                WHEN TIMESTAMPDIFF(YEAR, birth_date, NOW()) BETWEEN 25 AND 30 THEN '25-30岁'
                WHEN TIMESTAMPDIFF(YEAR, birth_date, NOW()) BETWEEN 31 AND 40 THEN '31-40岁'
                ELSE '40岁以上'
            END AS age_group,
            COUNT(*) AS count,
            ROUND(COUNT(*) / (SELECT COUNT(*) FROM employee WHERE status = 1) * 100, 1) AS percentage
        FROM employee
        WHERE status = 1 AND birth_date IS NOT NULL
        GROUP BY age_group
        """)
    Stats getAgeDistribution();

    // 5. 查询状态分布（结果封装到Stats的statusDistribution字段）
    @Select("""
        SELECT
            CASE WHEN status = 1 THEN '在职' ELSE '离职' END AS status_name,
            COUNT(*) AS count,
            ROUND(COUNT(*) / (SELECT COUNT(*) FROM employee) * 100, 1) AS percentage
        FROM employee
        GROUP BY status
        """)
    Stats getStatusDistribution();

    // 6. 查询部门薪资（结果封装到Stats的deptSalary字段）
    @Select("""
        SELECT
            d.dept_name,
            ROUND(AVG(e.salary), 0) AS avg_salary,
            SUM(e.salary) AS total_salary
        FROM sys_dept d
        LEFT JOIN employee e ON d.id = e.dept_id AND e.status = 1
        GROUP BY d.id, d.dept_name
        """)
    Stats getDeptSalary();

    // 7. 查询入离职趋势（结果封装到Stats的entryLeaveTrend字段）
    @Select("""
        SELECT
            DATE_FORMAT(create_time, '%Y-%m') AS month,
            COUNT(CASE WHEN status = 1 THEN 1 END) AS entry_count,
            COUNT(CASE WHEN status = 0 THEN 1 END) AS leave_count
        FROM employee
        GROUP BY month
        ORDER BY month DESC LIMIT 12
        ${timeCondition}
        """)
    Stats getEntryLeaveTrend(@Param("timeCondition") String timeCondition);
}