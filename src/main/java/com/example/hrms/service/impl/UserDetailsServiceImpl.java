package com.example.hrms.service.impl;

import com.example.hrms.entity.SysUser;
import com.example.hrms.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // 必须加 @Service，让 Spring 管理这个 Bean
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper; // 你自己的用户 Mapper

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 根据用户名从数据库查询用户
        SysUser sysUser = sysUserMapper.selectByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 2. 封装为 Spring Security 要求的 UserDetails 对象
        return User.withUsername(sysUser.getUsername())
                .password(sysUser.getPassword()) // 数据库里存的是 BCrypt 加密后的密码
                .authorities("ROLE_USER") // 这里可以根据实际角色设置权限
                .build();
    }
}