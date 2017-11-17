package com.my.utils.email;

import com.alibaba.fastjson.JSONObject;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Longe on 2017.08.01.
 */
public class EmailUtil {
    public static void postEmail(String jsonStr) {
        JSONObject obj = JSONObject.parseObject(jsonStr);
        //主机
        String host = obj.get("host").toString();
        //接收者
        String to = obj.get("to").toString();
        //发送者
        String from = obj.get("from").toString();
        //主题
        String subject = obj.get("subject").toString();
        //正文
        String messageText = obj.get("messageText").toString();
        boolean sessionDebug = false;
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        //props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        // 系统需要的信息
        Session session = Session.getDefaultInstance(props, null);
        // 一次对话，一个session ，这个session 要获取固定的发送邮件信息
        session.setDebug(sessionDebug);
        try {
            MimeMessage meg = new MimeMessage(session); // 生成消息实例
            meg.setFrom(new InternetAddress(from)); // 指定发件人
            InternetAddress[] address = {new InternetAddress(to)};  // 生成收件人地址数组
            meg.setRecipients(Message.RecipientType.TO, address);   // 指定收件人数组
            meg.setSubject(subject);    // 指定主题
            meg.setText(messageText);   // 指定正文
            meg.setSentDate(new Date());    // 指定发送时间
            meg.saveChanges();  // 保存信息
            Transport transport = session.getTransport("smtp");// 产生传输对象
            transport.connect("smtp.163.com", "mchotdog_011", "mchotdog");
            // 连接到你自己的主机
            transport.sendMessage(meg, meg.getAllRecipients());// 开始发送
            System.out.println("send   over");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public static void getEmail() {
        String host = "pop3.163.com";
        String username = "mchotdog_011";
        String password = "mchotdog";
        // Create empty properties
        try{
            Properties props = new Properties();
            // Get session
            Session session = Session.getDefaultInstance(props, null);
            // Get the store
            Store store = session.getStore("pop3");
            store.connect(host, username, password);
            // Get folder
            Folder folder = store.getDefaultFolder();
            if(folder == null) throw new Exception("No default folder");
            folder = folder.getFolder("INBOX");
            if(folder == null) throw new Exception("No POP3 INBOX");
            folder.open(Folder.READ_ONLY);
            // Get directory
            Message message[] = folder.getMessages();
            for (int i = 0, n = message.length; i < n; i++) {
                System.out.println(message[i]);
                System.out.println(i + ": " + message[i].getFrom() + "\t" + message[i].getSubject());
            }
            // Close connection
            folder.close(false);
            store.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
