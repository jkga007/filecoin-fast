package com.filecoin.modules.filecoin.entity;

import java.io.Serializable;

public class RegistEntity implements Serializable{
    private String email;
    private String vcode;
    private String passwd;
    private String captcha;
    private String registType;
    private Long userId;
    private String trueName;
    private String iccid;
    private String phone;
    private String phoneYzm;
    private String minerMachineAddr;
    private String minerMachineEnv;
    private String onLineTime;
    private String storageLen;
    private String bandWidth;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getRegistType() {
        return registType;
    }

    public void setRegistType(String registType) {
        this.registType = registType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneYzm() {
        return phoneYzm;
    }

    public void setPhoneYzm(String phoneYzm) {
        this.phoneYzm = phoneYzm;
    }

    public String getMinerMachineAddr() {
        return minerMachineAddr;
    }

    public void setMinerMachineAddr(String minerMachineAddr) {
        this.minerMachineAddr = minerMachineAddr;
    }

    public String getMinerMachineEnv() {
        return minerMachineEnv;
    }

    public void setMinerMachineEnv(String minerMachineEnv) {
        this.minerMachineEnv = minerMachineEnv;
    }

    public String getOnLineTime() {
        return onLineTime;
    }

    public void setOnLineTime(String onLineTime) {
        this.onLineTime = onLineTime;
    }

    public String getStorageLen() {
        return storageLen;
    }

    public void setStorageLen(String storageLen) {
        this.storageLen = storageLen;
    }

    public String getBandWidth() {
        return bandWidth;
    }

    public void setBandWidth(String bandWidth) {
        this.bandWidth = bandWidth;
    }
}
