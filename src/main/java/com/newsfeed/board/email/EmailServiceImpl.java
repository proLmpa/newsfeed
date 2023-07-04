package com.newsfeed.board.email;

import com.newsfeed.board.email.redis.RedisUtill;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    JavaMailSender emailSender;

    @Autowired
    RedisUtill redisUtill;

    public static final String ePw = createKey();
    private MimeMessage createMessage(String to )throws  Exception{
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
    public static String createKey(){
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();

    }

    @Override
    public String sendSimpleMessage(String to )throws Exception{
        MimeMessage message = createMessage(to);
        try{
            redisUtill.setDataExpire(ePw,to,60*3L); //인증번호의 유효시간 3분
            emailSender.send(message);
        }catch (MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw;
    }
    public String verifyEmail(String key) throws ChangeSetPersister.NotFoundException {
        String memberEmail = redisUtill.getData(key);
        if (memberEmail == null) {
            throw new ChangeSetPersister.NotFoundException();
        }
        redisUtill.deleteData(key);
        return ePw;
    }

}
