package com.example.hrms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.hrms.entity.SysUser;
//接口
public interface SysUserService extends IService<SysUser> {//IService<SysUser> 是 MyBatis-Plus 提供的通用接口（包含增删改查）
    // 定义login方法规则：所有实现这个接口的类，都必须写一个叫 login 的方法，这个方法要能接收用户名和密码，并且返回一个 “用户对象”
    SysUser login(String username, String password);
}