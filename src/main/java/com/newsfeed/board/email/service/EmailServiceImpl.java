package com.newsfeed.board.email.service;

import com.newsfeed.board.email.repository.EmailService;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    JavaMailSender emailSender;


    public static String ePw = "";
    private MimeMessage createMessage(String to )throws  Exception{
        if (to == null) {
            throw new IllegalArgumentException("이메일 주소가 비어있습니다.");
        }
        System.out.println("보내는 대상:" + to);
        System.out.println("인증번호 :" +ePw);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO,to); //보내는 대상
        message.setSubject("점메추 인증번호가 도착했습니다.");//제목

        String msgg = "";
        msgg+= "<div style='margin:100px;'>";
        msgg+= "<h1> 안녕하세요  점메추 입니다!!! </h1>";
        msgg+= "<br>";
        msgg+= "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msgg+= "<br>";
        msgg+= "<p>감사합니다!<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>회원가입 코드입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+= ePw+"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("rkdqja369@gmail.com","점메추"));//보내는 사람

        return message;
    }
    //인증코드만들기
//    public static String createKey(){
//
//        StringBuffer key = new StringBuffer();
//        Random rnd = new Random();
//
//        for (int i = 0; i < 6; i++) { // 인증코드 6자리
//            key.append((rnd.nextInt(10)));
//        }
//        return key.toString();
//
//    }

    @Override
    public String sendSimpleMessage(String to )throws Exception{
        if (to == null) {
            return null;
        }

        StringBuffer key = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        ePw = key.toString();

        MimeMessage message = createMessage(to);
        try{
            emailSender.send(message);
        }catch (MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException("이메일 발송중오류가 나타났습니다. 이메일을 다시 확인해주세요 ");
        }
        return ePw;
    }

}
