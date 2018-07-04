package com.filecoin.modules.sys.oauth2;

import java.io.Serializable;

public class PassWordModify implements Serializable {
    private String oldPassWord;
    private String newPassWord;
    private String newPassWordConfirm;

    public String getOldPassWord() {
        return oldPassWord;
    }

    public void setOldPassWord(String oldPassWord) {
        this.oldPassWord = oldPassWord;
    }

    public String getNewPassWord() {
        return newPassWord;
    }

    public void setNewPassWord(String newPassWord) {
        this.newPassWord = newPassWord;
    }

    public String getNewPassWordConfirm() {
        return newPassWordConfirm;
    }

    public void setNewPassWordConfirm(String newPassWordConfirm) {
        this.newPassWordConfirm = newPassWordConfirm;
    }
}
