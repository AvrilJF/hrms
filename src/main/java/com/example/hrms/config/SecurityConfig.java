package com.example.hrms.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

//import com.example.hrms.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 注入自定义的 UserDetailsService
    @Autowired
    private UserDetailsService userDetailsService;

    // 密码加密器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 安全过滤链
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 关闭 CSRF（新写法）
                .csrf(csrf -> csrf.disable())
                // 授权配置（新写法）
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/captcha", "/admin/**").permitAll()
                        .anyRequest().authenticated()
                )
                // 禁用表单登录（新写法）
                .formLogin(form -> form.disable())
                // 启用 HTTP Basic 认证（新写法）
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}