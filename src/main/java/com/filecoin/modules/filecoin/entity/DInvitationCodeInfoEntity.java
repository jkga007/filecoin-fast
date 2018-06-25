package com.filecoin.modules.filecoin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * 
 * @author r25437
 * @email support@filecoinon.com
 * @date 2018-06-25 11:42:54
 */
public class DInvitationCodeInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//邀请码
	private String invitationCode;
	//归属用户id
	private Long userId;
	//状态 00-未使用 01-已使用
	private String status;
	//更新时间
	private Date updatetime;

	/**
	 * 设置：邀请码
	 */
	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}
	/**
	 * 获取：邀请码
	 */
	public String getInvitationCode() {
		return invitationCode;
	}
	/**
	 * 设置：归属用户id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：归属用户id
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：状态 00-未使用 01-已使用
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取：状态 00-未使用 01-已使用
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdatetime() {
		return updatetime;
	}
}
