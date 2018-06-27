package com.filecoin.modules.filecoin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.filecoin.modules.filecoin.dao.WSendEmailDao;
import com.filecoin.modules.filecoin.entity.WSendEmailEntity;
import com.filecoin.modules.filecoin.service.WSendEmailService;



@Service("wSendEmailService")
public class WSendEmailServiceImpl implements WSendEmailService {
	@Autowired
	private WSendEmailDao wSendEmailDao;
	
	@Override
	public WSendEmailEntity queryObject(Long id){
		return wSendEmailDao.queryObject(id);
	}
	
	@Override
	public List<WSendEmailEntity> queryList(Map<String, Object> map){
		return wSendEmailDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return wSendEmailDao.queryTotal(map);
	}
	
	@Override
	public void save(WSendEmailEntity wSendEmail){
		wSendEmailDao.save(wSendEmail);
	}
	
	@Override
	public void update(WSendEmailEntity wSendEmail){
		wSendEmailDao.update(wSendEmail);
	}
	
	@Override
	public void delete(Long id){
		wSendEmailDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		wSendEmailDao.deleteBatch(ids);
	}

	@Override
	public WSendEmailEntity queryOneNearBy() {
		return wSendEmailDao.queryOneNearBy();
	}
}
