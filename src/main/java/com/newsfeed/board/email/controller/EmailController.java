package com.newsfeed.board.email.controller;

import com.newsfeed.board.email.repository.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EmailController {

    EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/email")
    @ResponseBody
    public String emailConfirm(@RequestParam String email)throws Exception{
        String confirm = emailService.sendSimpleMessage(email);
        System.out.println("전달받는 이메일: " + email);
        return confirm;
    }
}
// 권한부여 user와 certification의 연관관계 생각해서 작성할것