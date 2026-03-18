package com.example.hrms.controller;

import com.example.hrms.entity.SysUser;
import com.example.hrms.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private SysUserService sysUserService;

    // 登录接口：POST /api/login
    @PostMapping("/login")//限定该接口仅接收 POST 请求，访问路径为 /login
    public Map<String, Object> login(@RequestBody SysUser loginUser) {//@RequestBody：核心注解，作用是将前端传入的 JSON 格式请求体 自动解析并封装为 SysUser 对象
        Map<String, Object> result = new HashMap<>();//灵活封装返回结果（如状态码、提示信息、用户数据），前端可直接解析 JSON 格式
        try {
            // 调用登录校验（已包含用户名/密码校验 + 密码脱敏）
            SysUser user = sysUserService.login(loginUser.getUsername(), loginUser.getPassword());
            if (user != null) {
                result.put("code", 200);
                result.put("msg", "登录成功");
                result.put("data", user); // 返回用户信息（含角色）
//                Map<String, Object> data = new HashMap<>();
//                data.put("id", user.getId());
//                data.put("username", user.getUsername());
//                data.put("role", user.getRole()); // 必须返回role字段
//                result.put("data", data);
            } else {
                result.put("code", 400);
                result.put("msg", "登录失败，请检查账号密码！");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "服务器异常：" + e.getMessage());
        }
        return result;
    }
}