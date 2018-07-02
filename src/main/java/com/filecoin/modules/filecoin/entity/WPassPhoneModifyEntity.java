package com.filecoin.modules.filecoin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * 
 * @author g20416
 * @email support@filecoinon.com
 * @date 2018-07-02 21:29:04
 */
public class WPassPhoneModifyEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//序列id
	private Long id;
	//用户id
	private Long userId;
	//插入时间
	private Date insertTime;
	//修改时间
	private Date updateTime;
	//状态0待确认1已确认
	private Integer status;
	//原值
	private String oldValue;
	//新值
	private String newValue;
	//1邮件密码修改,2邮件密码找回,3邮件手机号码修改
	private Integer type;
	//插入时间戳,获取当前long时间,用于验证超时处理
	private Long timestamp;

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
	 * 设置：用户id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户id
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：插入时间
	 */
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	/**
	 * 获取：插入时间
	 */
	public Date getInsertTime() {
		return insertTime;
	}
	/**
	 * 设置：修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：状态0待确认1已确认
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态0待确认1已确认
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：原值
	 */
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	/**
	 * 获取：原值
	 */
	public String getOldValue() {
		return oldValue;
	}
	/**
	 * 设置：新值
	 */
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	/**
	 * 获取：新值
	 */
	public String getNewValue() {
		return newValue;
	}
	/**
	 * 设置：1邮件密码修改,2邮件密码找回,3邮件手机号码修改
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：1邮件密码修改,2邮件密码找回,3邮件手机号码修改
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：插入时间戳,获取当前long时间,用于验证超时处理
	 */
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * 获取：插入时间戳,获取当前long时间,用于验证超时处理
	 */
	public Long getTimestamp() {
		return timestamp;
	}
}
