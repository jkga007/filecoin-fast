package com.filecoin.modules.filecoin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.filecoin.modules.filecoin.dao.WSendMessageDao;
import com.filecoin.modules.filecoin.entity.WSendMessageEntity;
import com.filecoin.modules.filecoin.service.WSendMessageService;



@Service("wSendMessageService")
public class WSendMessageServiceImpl implements WSendMessageService {
	@Autowired
	private WSendMessageDao wSendMessageDao;
	
	@Override
	public WSendMessageEntity queryObject(Long id){
		return wSendMessageDao.queryObject(id);
	}
	
	@Override
	public List<WSendMessageEntity> queryList(Map<String, Object> map){
		return wSendMessageDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return wSendMessageDao.queryTotal(map);
	}
	
	@Override
	public void save(WSendMessageEntity wSendMessage){
		wSendMessageDao.save(wSendMessage);
	}
	
	@Override
	public void update(WSendMessageEntity wSendMessage){
		wSendMessageDao.update(wSendMessage);
	}
	
	@Override
	public void delete(Long id){
		wSendMessageDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		wSendMessageDao.deleteBatch(ids);
	}
	
}
