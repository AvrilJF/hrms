package com.example.hrms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hrms.entity.Employee;
import com.example.hrms.mapper.EmployeeMapper;
import com.example.hrms.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {}
