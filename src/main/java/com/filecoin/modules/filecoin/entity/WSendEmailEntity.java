package com.filecoin.modules.filecoin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * 
 * @author g20416
 * @email support@filecoinon.com
 * @date 2018-06-27 20:16:41
 */
public class WSendEmailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//序列id
	private Long id;
	//
	private String email;
	//
	private Long userId;
	//
	private Date insertTime;
	//
	private Date sendTime;
	//
	private Integer status;
	//
	private String returnCode;
	//
	private String returnMessage;
	private int type;

	/**
	 * 设置：序列id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：序列id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 获取：
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 设置：
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：
	 */
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	/**
	 * 获取：
	 */
	public Date getInsertTime() {
		return insertTime;
	}
	/**
	 * 设置：
	 */
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	/**
	 * 获取：
	 */
	public Date getSendTime() {
		return sendTime;
	}
	/**
	 * 设置：
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	/**
	 * 获取：
	 */
	public String getReturnCode() {
		return returnCode;
	}
	/**
	 * 设置：
	 */
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
	/**
	 * 获取：
	 */
	public String getReturnMessage() {
		return returnMessage;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
