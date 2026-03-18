//package com.example.hrms.controller;
//
//import com.google.code.kaptcha.impl.DefaultKaptcha;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import javax.imageio.ImageIO;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.ServletOutputStream;
//import java.awt.image.BufferedImage;
//
//@RestController
//@RequestMapping("/captcha")
//public class CaptchaController {
//
//    @Autowired
//    private DefaultKaptcha defaultKaptcha;
//
//    @GetMapping("/image")
//    public void getCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        // 1. 生成验证码文本
//        String captchaText = defaultKaptcha.createText();
//        // 2. 存入session（用于后续校验）
//        request.getSession().setAttribute("kaptchaCode", captchaText);
//        // 3. 生成验证码图片
//        BufferedImage image = defaultKaptcha.createImage(captchaText);
//        // 4. 响应图片
//        response.setContentType("image/jpeg");
//        ServletOutputStream out = response.getOutputStream();
//        ImageIO.write(image, "jpg", out);
//        out.flush();
//        out.close();
//    }
//}