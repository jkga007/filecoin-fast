package com.filecoin.modules.filecoin.service;

import com.filecoin.modules.filecoin.entity.DInvitationCodeInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author r25437
 * @email support@filecoinon.com
 * @date 2018-06-25 11:42:54
 */
public interface DInvitationCodeInfoService {
	
	DInvitationCodeInfoEntity queryObject(String invitationCode);
	
	List<DInvitationCodeInfoEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DInvitationCodeInfoEntity dInvitationCodeInfo);
	
	void update(DInvitationCodeInfoEntity dInvitationCodeInfo);
	
	void delete(String invitationCode);
	
	void deleteBatch(String[] invitationCodes);

	/**
	 * 获得邀请码已注册用户数量
	 * @return
	 */
	Integer selectCountbyInvitationCode(Map<String,Object> map);

	/**
	 * 激活成功后，生成用户对应的邀请码
	 * @param userId
	 * @return
	 */
	void createInvitationCodeByUser(Long userId);
}
