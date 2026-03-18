package com.example.hrms.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hrms.entity.SysDept;
import com.example.hrms.mapper.DeptMapper;
import com.example.hrms.service.SysDeptService;
import org.springframework.stereotype.Service;

@Service
public class SysDeptServiceImpl extends ServiceImpl<DeptMapper, SysDept> implements SysDeptService {}