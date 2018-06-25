package com.filecoin.modules.filecoin.service.impl;

import com.filecoin.common.utils.InvitationCodeGnerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.filecoin.modules.filecoin.dao.DInvitationCodeInfoDao;
import com.filecoin.modules.filecoin.entity.DInvitationCodeInfoEntity;
import com.filecoin.modules.filecoin.service.DInvitationCodeInfoService;



@Service("dInvitationCodeInfoService")
public class DInvitationCodeInfoServiceImpl implements DInvitationCodeInfoService {
	@Autowired
	private DInvitationCodeInfoDao dInvitationCodeInfoDao;
	
	@Override
	public DInvitationCodeInfoEntity queryObject(String invitationCode){
		return dInvitationCodeInfoDao.queryObject(invitationCode);
	}
	
	@Override
	public List<DInvitationCodeInfoEntity> queryList(Map<String, Object> map){
		return dInvitationCodeInfoDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dInvitationCodeInfoDao.queryTotal(map);
	}
	
	@Override
	public void save(DInvitationCodeInfoEntity dInvitationCodeInfo){
		dInvitationCodeInfoDao.save(dInvitationCodeInfo);
	}
	
	@Override
	public void update(DInvitationCodeInfoEntity dInvitationCodeInfo){
		dInvitationCodeInfoDao.update(dInvitationCodeInfo);
	}
	
	@Override
	public void delete(String invitationCode){
		dInvitationCodeInfoDao.delete(invitationCode);
	}
	
	@Override
	public void deleteBatch(String[] invitationCodes){
		dInvitationCodeInfoDao.deleteBatch(invitationCodes);
	}

	@Override
	public Integer selectCountbyInvitationCode(Map<String, Object> map) {
		return dInvitationCodeInfoDao.selectCountbyInvitationCode(map);
	}

	@Override
	public void createInvitationCodeByUser(Long userId){
		synchronized (DInvitationCodeInfoServiceImpl.class){
			while(true) {
				String invitationCode = InvitationCodeGnerateUtil.toSerialCode(userId);
				Map<String,Object> paramMap = new HashMap<>();
				paramMap.put("invitationCode",invitationCode);
				int count = queryTotal(paramMap);
				if(count ==0) {
					DInvitationCodeInfoEntity dInvitationCodeInfoEntity = new DInvitationCodeInfoEntity();
					dInvitationCodeInfoEntity.setInvitationCode(invitationCode);
					dInvitationCodeInfoEntity.setStatus("00");
					dInvitationCodeInfoEntity.setUserId(userId);
					save(dInvitationCodeInfoEntity);
					break;
				}
			}
		}
	}
}
