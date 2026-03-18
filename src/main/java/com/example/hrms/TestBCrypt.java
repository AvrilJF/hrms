package com.example.hrms;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBCrypt {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 生成 hr123 的加密密码
        System.out.println("hr123 加密后：" + encoder.encode("hr123"));
        // 生成 employee123 的加密密码
        System.out.println("employee123 加密后：" + encoder.encode("employee123"));
    }
}
