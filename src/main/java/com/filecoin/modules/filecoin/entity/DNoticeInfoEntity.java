package com.filecoin.modules.filecoin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * 
 * @author r25437&g20416
 * @email support@filecoinon.com
 * @date 2018-07-06 15:51:45
 */
public class DNoticeInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//序列id
	private Long id;
	//公告内容
	private String noticeContent;
	//公告url
	private String url;
	//排序
	private Integer sort;
	//
	private Date insertTime;
	//
	private Date updateTime;

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
	 * 设置：公告内容
	 */
	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}
	/**
	 * 获取：公告内容
	 */
	public String getNoticeContent() {
		return noticeContent;
	}
	/**
	 * 设置：公告url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：公告url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置：排序
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	/**
	 * 获取：排序
	 */
	public Integer getSort() {
		return sort;
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
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}
