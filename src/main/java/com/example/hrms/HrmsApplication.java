package com.example.hrms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.hrms.mapper") // 扫描Mapper接口
public class HrmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(HrmsApplication.class, args);
        System.out.println("=====HRMS系统启动成功=====http://localhost:8080/api=====");
    }
}