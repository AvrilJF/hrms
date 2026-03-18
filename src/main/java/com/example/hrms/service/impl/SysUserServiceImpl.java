package com.example.hrms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hrms.entity.SysUser;
import com.example.hrms.mapper.SysUserMapper;
import com.example.hrms.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 登录校验逻辑
    @Override
    public SysUser login(String username, String password) {
        // 1. 从数据库查询用户（通过用户名）：sysUserMapper.selectByUsername(username)：调用 Mapper 层的 SQL（@Select("SELECT * FROM sys_user WHERE username = #{username}")）从数据库查用户；
        SysUser user = sysUserMapper.selectByUsername(username);
        if (user == null) {
            return null; // 用户名不存在
        }
        // 2. 校验密码（BCrypt解密对比：前端输入的明文密码 vs 数据库加密密码）
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null; // 密码错误
        }
        // 3. 校验通过，返回用户（脱敏：清空密码）
        user.setPassword(null);
        return user;
    }
}