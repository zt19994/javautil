package com.util.mailutil;


import com.util.traceutil.StackTraceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.Properties;
import java.util.UUID;

/**
 * 邮件工具类
 *
 * @author zt1994 2020/4/2 17:27
 */
public class MailUtil {

    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

    /**
     * smtp服务器
     */
    private String host = "";

    /**
     * 端口
     */
    private String port = "";

    /**
     * 发件人地址
     */
    private String from = "";

    /**
     * 收件人地址
     */
    private String to = "";

    /**
     * 附件地址
     */
    private String affix = "";

    /**
     * 附件名称
     */
    private String affixName = "";

    /**
     * 用户名
     */
    private String user = "";

    /**
     * 密码
     */
    private String pwd = "";

    /**
     * 邮件标题
     */
    private String title = "";

    /**
     * 邮件内容
     */
    private String content = "";


    public MailUtil(String host, String port, String user, String pwd) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }


    /**
     * 发送内容不带文件
     *
     * @param from
     * @param to
     * @param title
     * @param content
     */
    public void sendText(String from, String to, String title, String content) {
        this.from = from;
        this.to = to;
        this.title = title;
        this.content = content;

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        // 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        // 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
        props.put("mail.smtp.auth", "true");

        // 用刚刚设置好的props对象构建一个session
        Session session = Session.getDefaultInstance(props);

        // 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
        // 用（你可以在控制台（console)上看到发送邮件的过程）
        session.setDebug(true);

        // 用session为参数定义消息对象
        MimeMessage message = new MimeMessage(session);
        try {
            // 加载发件人地址
            message.setFrom(new InternetAddress(from));
            // 加载收件人地址
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // 加载标题
            message.setSubject(title);
            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 设置邮件的文本内容
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setText(content);
            multipart.addBodyPart(contentPart);
            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();
            // 发送邮件
            Transport transport = session.getTransport("smtp");
            // 连接服务器的邮箱
            transport.connect(host, user, pwd);
            // 把邮件发送出去
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            logger.info("错误：{}", StackTraceUtil.getStackTraceInfo(e));
        }
    }


    /**
     * 发送内容带邮件
     *
     * @param from
     * @param to
     * @param title
     * @param content
     * @param affix
     * @param affixName
     */
    public void sendSource(String from, String to, String title, String content, String affix, String affixName) {
        this.from = from;
        this.to = to;
        this.title = title;
        this.content = content;
        this.affix = affix;
        this.affixName = affixName;
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        // 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        // 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
        props.put("mail.smtp.auth", "true");

        // 用刚刚设置好的props对象构建一个session
        Session session = Session.getDefaultInstance(props);

        // 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
        // 用（你可以在控制台（console)上看到发送邮件的过程）
        session.setDebug(true);

        // 用session为参数定义消息对象
        MimeMessage message = new MimeMessage(session);
        try {
            // 加载发件人地址
            message.setFrom(new InternetAddress(from));
            // 加载收件人地址
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // 加载标题
            message.setSubject(title);
            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 设置邮件的文本内容
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setText(content);
            multipart.addBodyPart(contentPart);

            // 添加附件
            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(affix);
            // 添加附件的内容
            messageBodyPart.setDataHandler(new DataHandler(source));
            // 添加附件的标题
            // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
            sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
            messageBodyPart.setFileName("=?GBK?B?" + enc.encode(affixName.getBytes()) + "?=");
            multipart.addBodyPart(messageBodyPart);

            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();
            // 发送邮件
            Transport transport = session.getTransport("smtp");
            // 连接服务器的邮箱
            transport.connect(host, user, pwd);
            // 把邮件发送出去
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            logger.info("错误：{}", StackTraceUtil.getStackTraceInfo(e));
        }
    }


    /**
     * 发送会议邀请
     *
     * @param from
     * @param to
     * @param title
     * @param content
     * @param startTime
     * @param endTime
     */
    public void sendOutLookMail(String from, String to, String title, String content, String startTime, String endTime) {
        this.from = from;
        this.to = to;
        this.title = title;
        this.content = content;
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        // 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        // 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
        props.put("mail.smtp.auth", "true");

        // 用刚刚设置好的props对象构建一个session
        Session session = Session.getDefaultInstance(props);

        // 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
        // 用（你可以在控制台（console)上看到发送邮件的过程）
        session.setDebug(true);

        // 用session为参数定义消息对象
        MimeMessage message = new MimeMessage(session);
        try {
            // 加载发件人地址
            message.setFrom(new InternetAddress(from));
            // 加载收件人地址
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // 加载标题
            message.setSubject(title);
            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 设置邮件的文本内容
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setText(content);
            multipart.addBodyPart(contentPart);

            StringBuffer buffer = new StringBuffer();
            String uuid = UUID.randomUUID().toString();
            logger.info("mail uuid" + uuid);
            buffer.append("BEGIN:VCALENDAR\n"
                    + "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n"
                    + "VERSION:2.0\n"
                    + "METHOD:REQUEST\n"
                    + "BEGIN:VEVENT\n"
                    + "SEQUENCE:0\n"
                    + "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:" + to + "\n"
                    + "ORGANIZER:MAILTO:" + from + "\n"
                    + "DTSTART;TZID=Asia/Shanghai:" + startTime + "\n"
                    + "DTEND;TZID=Asia/Shanghai:" + endTime + "\n"
                    + "UID:" + uuid + "\n"//如果id相同的话，outlook会认为是同一个会议请求，所以使用uuid。
                    + "CATEGORIES:SuccessCentral Reminder\n"
                    + "DESCRIPTION:This the description of the meeting\n"
                    + "SUMMARY:meeting request\n" + "PRIORITY:5\n"
                    + "CLASS:PUBLIC\n"
                    + "BEGIN:VALARM\n"
                    + "TRIGGER:-PT15M\n"
                    + "ACTION:DISPLAY\n"
                    + "DESCRIPTION:Reminder\n"
                    + "END:VALARM\n"
                    + "END:VEVENT\n" + "END:VCALENDAR");
            BodyPart messageBodyPart = new MimeBodyPart();
            // 测试下来如果不这么转换的话，会以纯文本的形式发送过去，
            //如果没有method=REQUEST;charset=\"UTF-8\"，outlook会议附件的形式存在，而不是直接打开就是一个会议请求
            messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(buffer.toString(),
                    "text/calendar;method=REQUEST;charset=\"UTF-8\"")));
            multipart.addBodyPart(messageBodyPart);

            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();
            // 发送邮件
            Transport transport = session.getTransport("smtp");
            // 连接服务器的邮箱
            transport.connect(host, user, pwd);
            // 把邮件发送出去
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            logger.info("错误：{}", StackTraceUtil.getStackTraceInfo(e));
        }
    }

}
