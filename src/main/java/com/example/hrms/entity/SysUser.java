package com.example.hrms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user") // 对应数据库表名
public class SysUser {
    @TableId(type = IdType.AUTO)
    private Long id; // 主键
    private String username; // 用户名
    private String password; // 密码（加密）
    private String role; // 角色
    private Long roleId; //关联角色表ID
    private Long empId; // 新增：关联员工ID
    private Integer status; // 新增：状态 1-启用 0-禁用
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}