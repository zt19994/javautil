package com.util.mailutil;

import java.util.Date;

/**
 * 邮箱服务器设置
 *
 * @author zhongtao on 2019/1/2
 */
public class EmailServer {
    /**
     * 邮箱服务器ID
     */
    private Long serverId;
    /**
     * 发信邮箱
     */
    private String sendMail;
    /**
     * 发信人
     */
    private String sendName;
    /**
     * SMTP账号
     */
    private String smtpAccount;
    /**
     * SMTP密码
     */
    private String smtpPass;
    /**
     * SMTP服务器
     */
    private String smtpServer;
    /**
     * SMTP端口号
     */
    private String smtpPort;
    /**
     * 是否开启
     */
    private String isOpen;
    /**
     * 是否验证
     */
    private String isCheck;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifyTime;
    /**
     * 是否删除
     */
    private String delFlag;

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getSendMail() {
        return sendMail;
    }

    public void setSendMail(String sendMail) {
        this.sendMail = sendMail;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getSmtpAccount() {
        return smtpAccount;
    }

    public void setSmtpAccount(String smtpAccount) {
        this.smtpAccount = smtpAccount;
    }

    public String getSmtpPass() {
        return smtpPass;
    }

    public void setSmtpPass(String smtpPass) {
        this.smtpPass = smtpPass;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    /**
     * 获取创建时间
     *
     * @return 创建时间
     * @author Songhl
     */
    public Date getCreateTime() {
        //如果创建时间不为空
        if (this.createTime != null) {
            //返回创建时间
            return new Date(this.createTime.getTime());
        } else {
            //返回空
            return null;
        }
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     * @author Songhl
     */
    public void setCreateTime(Date createTime) {
        //如果创建时间不为空
        if (createTime != null) {
            //获取创建时间值
            Date tEmp = (Date) createTime.clone();
            //如果不为空
            if (tEmp != null) {
                //赋值给创建时间
                this.createTime = tEmp;
            }
        }
    }

    /**
     * 获取修改时间
     *
     * @return 修改时间
     * @author Songhl
     */
    public Date getModifyTime() {
        //如果修改时间不为空
        if (this.modifyTime != null) {
            //返回修改时间
            return new Date(this.modifyTime.getTime());
        } else {
            //返回空
            return null;
        }
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     * @author Songhl
     */
    public void setModifyTime(Date modifyTime) {
        //如果修改时间不为空
        if (modifyTime != null) {
            //获取修改时间
            Date tEmp = (Date) modifyTime.clone();
            //如果修改时间不为空
            if (tEmp != null) {
                //设置为修改时间
                this.modifyTime = tEmp;
            }
        }
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
