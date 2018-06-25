package com.filecoin.modules.app.entity;
public class dInvitationCodeInfo {
    private String invitationCode;//邀请码
    private Long userId;//归属用户id
    private String status;//状态 00-未使用 01-已使用
    private java.util.Date updatetime;//更新时间
    public dInvitationCodeInfo() {
        super();
    }
    public dInvitationCodeInfo(String invitationCode,Long userId,String status,java.util.Date updatetime) {
        super();
        this.invitationCode = invitationCode;
        this.userId = userId;
        this.status = status;
        this.updatetime = updatetime;
    }
    public String getInvitationCode() {
        return this.invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public java.util.Date getUpdatetime() {
        return this.updatetime;
    }

    public void setUpdatetime(java.util.Date updatetime) {
        this.updatetime = updatetime;
    }

}
