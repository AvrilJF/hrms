package com.example.hrms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hrms.entity.Employee;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

//@Repository
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

    //    // 状态分布
    @Select("SELECT status FROM employee WHERE status != 3")
    List<Map<String, Object>> getEmployeeStatusData();


    // 部门薪资分布
    @Select("SELECT d.dept_name as deptName, " +
            "SUM(e.salary) as totalSalary, " +
            "AVG(e.salary) as avgSalary " +
            "FROM employee e LEFT JOIN sys_dept d ON e.dept_id = d.id " +
            "WHERE e.status != 3 GROUP BY d.dept_name")
    List<Map<String, Object>> getDeptSalaryDistribution();

//    // 入离职趋势
    @Select("SELECT month, entry_count, leave_count FROM v_entry_leave_trend")
    List<Map<String, Object>> getEntryLeaveTrendData();

    // 替换原来的 getEmployeeStats 方法，只查询需要的原始字段
    @Select("SELECT id, age, salary, status FROM employee WHERE status != 3")
    List<Map<String, Object>> getEmployeeBaseData();
    // 修复后写法（统计所有部门，或用你表中实际存在的状态字段）
    @Select("SELECT COUNT(id) AS deptCount FROM sys_dept")
    Integer getDeptCount();
}
