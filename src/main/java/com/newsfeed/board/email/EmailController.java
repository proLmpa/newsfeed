package com.newsfeed.board.email;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class EmailController {

    EmailService emailService;
    @PostMapping("/email")
    @ResponseBody
    public String emailConfirm(String email)throws Exception{
        String confirm = emailService.sendSimpleMessage(email);
        return confirm;
    }
}
