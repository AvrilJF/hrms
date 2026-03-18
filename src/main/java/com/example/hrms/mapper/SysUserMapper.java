package com.example.hrms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hrms.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    // 根据用户名查询用户
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    SysUser selectByUsername(String username);
}