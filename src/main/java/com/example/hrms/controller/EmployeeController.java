package com.example.hrms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.hrms.entity.Employee;
import com.example.hrms.service.EmployeeService;
import com.example.hrms.utils.ExcelUtil;
import com.example.hrms.utils.Result;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final ExcelUtil excelUtil;

    @GetMapping("/list")
    public Result<IPage<Employee>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String empName) {
        Page<Employee> page = new Page<>(pageNum, pageSize);
        IPage<Employee> empPage = employeeService.page(page,
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Employee>()
                        .like(empName != null, "emp_name", empName)
                        .eq("is_delete", 0));
        return Result.success(empPage);
    }

    @GetMapping("/{empId}")
    public Result<Employee> getById(@PathVariable Long empId) {
        Employee emp = employeeService.getById(empId);
        return emp != null ? Result.success(emp) : Result.error("员工不存在");
    }

    @PostMapping
    public Result<?> add(@RequestBody Employee employee) {
        return Result.success(employeeService.save(employee));
    }

    @PutMapping
    public Result<?> update(@RequestBody Employee employee) {
        return Result.success(employeeService.updateById(employee));
    }

    @DeleteMapping("/{empId}")
    public Result<?> delete(@PathVariable Long empId) {
        return Result.success(employeeService.removeById(empId));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        List<Employee> list = employeeService.list(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Employee>().eq("is_delete", 0));
        excelUtil.exportExcel(response, list, Employee.class, "员工列表");
    }
}