package com.example.hrms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.hrms.entity.SysDept;
import com.example.hrms.entity.SysUser;
import com.example.hrms.entity.SysRole;
import com.example.hrms.entity.Employee;
import com.example.hrms.service.SysUserService;
import com.example.hrms.service.SysRoleService;
import com.example.hrms.service.SysDeptService;
import com.example.hrms.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private EmployeeService employeeService;

    // 正确写法（注入接口，面向接口编程）
    @Autowired
    private PasswordEncoder passwordEncoder;
    // 2. 删除用户
    @DeleteMapping("/user/delete/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = sysUserService.removeById(id);
            result.put("code", success ? 200 : 500);
            result.put("msg", success ? "删除成功" : "删除失败");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "删除失败：" + e.getMessage());
        }
        return result;
    }

    // 3. 添加/编辑用户
    @PostMapping("/user/save")
    public Map<String, Object> saveUser(@RequestBody SysUser sysUser) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 密码加密（新增/修改时如果密码不是加密格式则加密）
            if (sysUser.getPassword() != null && !sysUser.getPassword().startsWith("$2a$")) {
                sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
            }
            // 关联角色ID（根据role字段匹配）
            LambdaQueryWrapper<SysRole> roleWrapper = new LambdaQueryWrapper<>();
            roleWrapper.eq(SysRole::getRoleCode, sysUser.getRole());
            SysRole role = sysRoleService.getOne(roleWrapper);
            if (role != null) {
                sysUser.setRoleId(role.getId());
            }

            boolean success = sysUserService.saveOrUpdate(sysUser);
            result.put("code", success ? 200 : 500);
            result.put("msg", success ? "操作成功" : "操作失败");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "操作失败：" + e.getMessage());
        }
        return result;
    }

    // ========== 角色管理接口 ==========
    // 1. 获取所有角色
    @GetMapping("/role/list")
    public Map<String, Object> roleList() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<SysRole> roles = sysRoleService.list();
            result.put("code", 200);
            result.put("data", roles);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询失败：" + e.getMessage());
        }
        return result;
    }

    // 2. 删除角色（排除admin角色）
    @DeleteMapping("/role/delete/{id}")
    public Map<String, Object> deleteRole(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 禁止删除管理员角色
            SysRole role = sysRoleService.getById(id);
            if (role != null && "admin".equals(role.getRoleCode())) {
                result.put("code", 400);
                result.put("msg", "禁止删除系统管理员角色！");
                return result;
            }
            // 检查是否有用户关联该角色
            LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.eq(SysUser::getRoleId, id);
            long count = sysUserService.count(userWrapper);
            if (count > 0) {
                result.put("code", 400);
                result.put("msg", "该角色下还有用户，无法删除！");
                return result;
            }

            boolean success = sysRoleService.removeById(id);
            result.put("code", success ? 200 : 500);
            result.put("msg", success ? "删除成功" : "删除失败");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "删除失败：" + e.getMessage());
        }
        return result;
    }

    // 3. 添加/编辑角色
    @PostMapping("/role/save")
    public Map<String, Object> saveRole(@RequestBody SysRole sysRole) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 角色编码唯一性校验
            LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysRole::getRoleCode, sysRole.getRoleCode());
            if (sysRole.getId() != null) {
                wrapper.ne(SysRole::getId, sysRole.getId()); // 编辑时排除自身
            }
            if (sysRoleService.count(wrapper) > 0) {
                result.put("code", 400);
                result.put("msg", "角色编码已存在！");
                return result;
            }

            boolean success = sysRoleService.saveOrUpdate(sysRole);
            result.put("code", success ? 200 : 500);
            result.put("msg", success ? "操作成功" : "操作失败");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "操作失败：" + e.getMessage());
        }
        return result;
    }

    // 仪表盘统计数据
    @GetMapping("/dashboard/stats")
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> result = new HashMap<>();
        // 总员工数
        long totalUser = sysUserService.count();
        // 在职员工数
        long activeUser = sysUserService.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getRole, "employee"));
        // 部门数
        long deptCount = sysDeptService.count();
        // 平均薪资（模拟数据，实际可从薪资表计算）
        double avgSalary = 15600;
        // 出勤率（模拟：96%）
        double attendanceRate = 0.96;
        // 本月新入职（模拟：3人）
        long newHire = 3;

        result.put("totalUser", totalUser);
        result.put("activeUser", activeUser);
        result.put("deptCount", deptCount);
        result.put("avgSalary", avgSalary);
        result.put("attendanceRate", attendanceRate);
        result.put("newHire", newHire);
        return result;
    }

    // 员工趋势数据
    @GetMapping("/dashboard/trend")
    public Map<String, Object> getEmployeeTrend() {
        Map<String, Object> result = new HashMap<>();
        // 模拟1-6月数据
        result.put("months", Arrays.asList("1月", "2月", "3月", "4月", "5月", "6月"));
        result.put("active", Arrays.asList(12, 13, 14, 15, 15, 15));
        result.put("hire", Arrays.asList(2, 2, 2, 2, 0, 0));
        result.put("leave", Arrays.asList(0, 0, 0, 0, 0, 0));
        return result;
    }

    // 部门分布数据
    @GetMapping("/dashboard/dept-distribution")
    public Map<String, Object> getDeptDistribution() {
        Map<String, Object> result = new HashMap<>();
        result.put("names", Arrays.asList("技术部", "市场部", "财务部", "人力资源部"));
        result.put("values", Arrays.asList(40, 20, 20, 20));
        return result;
    }

    // 待办事项
    @GetMapping("/dashboard/todos")
    public List<Map<String, Object>> getTodos() {
        List<Map<String, Object>> todos = new ArrayList<>();
        todos.add(Map.of("title", "员工合同到期提醒", "desc", "3名员工的合同将在30天内到期", "tag", "紧急", "color", "error"));
        todos.add(Map.of("title", "新员工入职培训", "desc", "2026-03-05 至 2026-03-06", "tag", "进行中", "color","warning"));
        todos.add(Map.of("title", "月度薪资发放", "desc", "2026-03-10 前完成", "tag","计划中", "color","success"));
        return todos;
    }


    // ========== 部门管理接口 ==========
    @GetMapping("/dept/list")
    public Map<String, Object> deptList(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String deptName
    ) {
        Page<SysDept> page = new Page<>(current, size);
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(deptName != null && !deptName.isEmpty(), SysDept::getDeptName, deptName);
        Page<SysDept> deptPage = sysDeptService.page(page, wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("records", deptPage.getRecords());
        result.put("total", deptPage.getTotal());
        return result;
    }

    @PostMapping("/dept/save")
    public Map<String, Object> saveDept(@RequestBody SysDept dept) {
        Map<String, Object> result = new HashMap<>();
        boolean success = sysDeptService.saveOrUpdate(dept);
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "操作成功" : "操作失败");
        return result;
    }

    @DeleteMapping("/dept/delete/{id}")
    public Map<String, Object> deleteDept(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        // 检查是否有子部门
        long count = sysDeptService.count(new LambdaQueryWrapper<SysDept>().eq(SysDept::getParentId, id));
        if (count > 0) {
            result.put("code", 400);
            result.put("msg", "存在子部门，无法删除！");
            return result;
        }
        boolean success = sysDeptService.removeById(id);
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "删除成功" : "删除失败");
        return result;
    }

    // 获取所有部门（用于下拉选择父级）
    @GetMapping("/dept/options")
    public List<SysDept> deptOptions() {
        return sysDeptService.list();
    }


    // ========== 用户管理接口 ==========
    @GetMapping("/user/list")
    public Map<String, Object> userList(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Integer status
    ) {
        Page<SysUser> page = new Page<>(current, size);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(username != null && !username.isEmpty(), SysUser::getUsername, username);
        wrapper.eq(role != null && !role.isEmpty(), SysUser::getRole, role);
        wrapper.eq(status != null, SysUser::getStatus, status);
        Page<SysUser> userPage = sysUserService.page(page, wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("records", userPage.getRecords());
        result.put("total", userPage.getTotal());
        return result;
    }

    // 切换用户状态
    @PostMapping("/user/toggle-status/{id}")
    public Map<String, Object> toggleStatus(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        SysUser user = sysUserService.getById(id);
        if (user == null) {
            result.put("code", 400);
            result.put("msg", "用户不存在");
            return result;
        }
        user.setStatus(user.getStatus() == 1 ? 0 : 1);
        boolean success = sysUserService.updateById(user);
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "状态更新成功" : "状态更新失败");
        return result;
    }

    // 获取员工选项（用于用户关联）
    @GetMapping("/employee/options")
    public List<Employee> employeeOptions() {
        return employeeService.list();
    }
}