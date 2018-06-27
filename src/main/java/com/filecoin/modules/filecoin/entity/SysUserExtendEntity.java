package com.filecoin.modules.filecoin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * 
 * @author g20416
 * @email support@filecoinon.com
 * @date 2018-06-27 23:22:53
 */
public class SysUserExtendEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//序列id
	private Long id;
	//用户id
	private Long userId;
	//真实姓名
	private String trueName;
	//身份证号
	private String iccid;
	//矿机地址
	private String minerMachineAddr;
	//矿机环境
	private String minerMachineEnv;
	//在下时长
	private String onLineTime;
	//存储大小
	private String storageLen;
	//带宽大小
	private String bandWidth;

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
	 * 设置：真实姓名
	 */
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	/**
	 * 获取：真实姓名
	 */
	public String getTrueName() {
		return trueName;
	}
	/**
	 * 设置：身份证号
	 */
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	/**
	 * 获取：身份证号
	 */
	public String getIccid() {
		return iccid;
	}
	/**
	 * 设置：矿机地址
	 */
	public void setMinerMachineAddr(String minerMachineAddr) {
		this.minerMachineAddr = minerMachineAddr;
	}
	/**
	 * 获取：矿机地址
	 */
	public String getMinerMachineAddr() {
		return minerMachineAddr;
	}
	/**
	 * 设置：矿机环境
	 */
	public void setMinerMachineEnv(String minerMachineEnv) {
		this.minerMachineEnv = minerMachineEnv;
	}
	/**
	 * 获取：矿机环境
	 */
	public String getMinerMachineEnv() {
		return minerMachineEnv;
	}
	/**
	 * 设置：在下时长
	 */
	public void setOnLineTime(String onLineTime) {
		this.onLineTime = onLineTime;
	}
	/**
	 * 获取：在下时长
	 */
	public String getOnLineTime() {
		return onLineTime;
	}
	/**
	 * 设置：存储大小
	 */
	public void setStorageLen(String storageLen) {
		this.storageLen = storageLen;
	}
	/**
	 * 获取：存储大小
	 */
	public String getStorageLen() {
		return storageLen;
	}
	/**
	 * 设置：带宽大小
	 */
	public void setBandWidth(String bandWidth) {
		this.bandWidth = bandWidth;
	}
	/**
	 * 获取：带宽大小
	 */
	public String getBandWidth() {
		return bandWidth;
	}
}
