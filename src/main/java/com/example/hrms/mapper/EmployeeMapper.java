package com.example.hrms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hrms.entity.Employee;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
//@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
    // 部门分布
    @Select("SELECT d.dept_name as name, COUNT(e.id) as value FROM employee e LEFT JOIN sys_dept d ON e.dept_id = d.id WHERE e.status != 3 GROUP BY d.dept_name")
    List<Map<String, Object>> getDeptDistribution();

    // 性别分布
    @Select("SELECT " +
            "SUM(CASE WHEN gender = 1 THEN 1 ELSE 0 END) as male, " +
            "SUM(CASE WHEN gender = 2 THEN 1 ELSE 0 END) as female " +
            "FROM employee WHERE status != 3")
    Map<String, Integer> getGenderDistribution();

    // 年龄分布
    @Select("SELECT " +
            "SUM(CASE WHEN age BETWEEN 20 AND 25 THEN 1 ELSE 0 END) as age20_25, " +
            "SUM(CASE WHEN age BETWEEN 26 AND 30 THEN 1 ELSE 0 END) as age26_30, " +
            "SUM(CASE WHEN age BETWEEN 31 AND 35 THEN 1 ELSE 0 END) as age31_35, " +
            "SUM(CASE WHEN age BETWEEN 36 AND 40 THEN 1 ELSE 0 END) as age36_40, " +
            "SUM(CASE WHEN age > 40 THEN 1 ELSE 0 END) as age41Plus " +
            "FROM employee WHERE status != 3")
    Map<String, Integer> getAgeDistribution();

    // 状态分布
    @Select("SELECT " +
            "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as onDuty, " +
            "SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) as probation, " +
            "SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END) as leave " +
            "FROM employee")
    Map<String, Integer> getStatusDistribution();

    // 部门薪资分布
    @Select("SELECT d.dept_name as deptName, " +
            "SUM(e.salary) as totalSalary, " +
            "AVG(e.salary) as avgSalary " +
            "FROM employee e LEFT JOIN sys_dept d ON e.dept_id = d.id " +
            "WHERE e.status != 3 GROUP BY d.dept_name")
    List<Map<String, Object>> getDeptSalaryDistribution();

    // 入离职趋势
    @Select("SELECT DATE_FORMAT(entry_time, '%m月') as month, COUNT(id) as entryCount " +
            "FROM employee WHERE entry_time IS NOT NULL AND entry_time LIKE #{time} GROUP BY DATE_FORMAT(entry_time, '%m月') " +
            "UNION ALL " +
            "SELECT DATE_FORMAT(leave_time, '%m月') as month, COUNT(id) as leaveCount " +
            "FROM employee WHERE leave_time IS NOT NULL AND leave_time LIKE #{time} GROUP BY DATE_FORMAT(leave_time, '%m月')")
    List<Map<String, Object>> getEntryLeaveTrend(@Param("time") String time);

    // 员工统计
//    @Select("SELECT " +
//            "  COUNT(id) AS totalCount, " +
//            "  ROUND(AVG(age), 1) AS avgAge, " +
//            "  ROUND(AVG(salary), 2) AS avgSalary, " +
//            "  (SELECT COUNT(id) FROM sys_dept WHERE status = 1) AS deptCount, " +
//            "  SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS onDutyCount, " +
//            "  ROUND((SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) / COUNT(id)) * 100, 1) AS onDutyRate, " +
//            "  SUM(salary) AS totalSalary " +
//            "FROM employee " +
//            "WHERE status != 3 " +
//            "GROUP BY 1,2,3,4,5,6,7") // 加 GROUP BY 包裹所有列，兼容严格模式
//    Map<String, Object> getEmployeeStats();
    // 替换原来的 getEmployeeStats 方法，只查询需要的原始字段
    @Select("SELECT id, age, salary, status FROM employee WHERE status != 3")
    List<Map<String, Object>> getEmployeeBaseData();

    // 单独查询部门数量
    @Select("SELECT COUNT(id) AS deptCount FROM sys_dept WHERE status = 1")
    Integer getDeptCount();
}
