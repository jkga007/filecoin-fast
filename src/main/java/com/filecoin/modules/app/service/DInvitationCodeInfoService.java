package com.filecoin.modules.app.service;
import java.util.List;
import java.util.Map;

import com.filecoin.modules.app.entity.dInvitationCodeInfo;
public interface DInvitationCodeInfoService{
	/**
	 * 获得dInvitationCodeInfo数据的总行数
	 * @return
	 */
    long getdInvitationCodeInfoRowCount();
	/**
	 * 获得dInvitationCodeInfo数据集合
	 * @return
	 */
    List<dInvitationCodeInfo> selectdInvitationCodeInfo();
	/**
	 * 获得一个dInvitationCodeInfo对象,以参数dInvitationCodeInfo对象中不为空的属性作为条件进行查询
	 * @param obj
	 * @return
	 */
    dInvitationCodeInfo selectdInvitationCodeInfoByObj(dInvitationCodeInfo obj);
	/**
	 * 获得邀请码已注册用户数量
	 * @return
	 */
	Integer selectCountbyInvitationCode(Map<String,Object> map);
	/**
	 * 通过dInvitationCodeInfo的id获得dInvitationCodeInfo对象
	 * @param id
	 * @return
	 */
    dInvitationCodeInfo selectdInvitationCodeInfoById(Object id);
	/**
	 * 插入dInvitationCodeInfo到数据库,包括null值
	 * @param value
	 * @return
	 */
    int insertdInvitationCodeInfo(dInvitationCodeInfo value);
	/**
	 * 插入dInvitationCodeInfo中属性值不为null的数据到数据库
	 * @param value
	 * @return
	 */
    int insertNonEmptydInvitationCodeInfo(dInvitationCodeInfo value);
	/**
	 * 通过dInvitationCodeInfo的id删除dInvitationCodeInfo
	 * @param id
	 * @return
	 */
    int deletedInvitationCodeInfoById(Object id);
	/**
	 * 通过dInvitationCodeInfo的id更新dInvitationCodeInfo中的数据,包括null值
	 * @param enti
	 * @return
	 */
    int updatedInvitationCodeInfoById(dInvitationCodeInfo enti);
	/**
	 * 通过dInvitationCodeInfo的id更新dInvitationCodeInfo中属性不为null的数据
	 * @param enti
	 * @return
	 */
    int updateNonEmptydInvitationCodeInfoById(dInvitationCodeInfo enti);
}