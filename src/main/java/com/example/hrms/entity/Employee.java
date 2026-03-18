package com.example.hrms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("employee")
public class Employee {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String empName;
    private Long deptId;
    private LocalDateTime createTime;

    private Integer gender; // 1-男 2-女
    private Integer age;
    private BigDecimal salary;
    private Integer status; // 1-在职 2-试用期 3-离职
    private LocalDateTime entryTime;
    private LocalDateTime leaveTime;
    private LocalDateTime updateTime;
}