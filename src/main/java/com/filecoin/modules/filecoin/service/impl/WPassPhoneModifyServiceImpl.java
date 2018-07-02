package com.filecoin.modules.filecoin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.filecoin.modules.filecoin.dao.WPassPhoneModifyDao;
import com.filecoin.modules.filecoin.entity.WPassPhoneModifyEntity;
import com.filecoin.modules.filecoin.service.WPassPhoneModifyService;



@Service("wPassPhoneModifyService")
public class WPassPhoneModifyServiceImpl implements WPassPhoneModifyService {
	@Autowired
	private WPassPhoneModifyDao wPassPhoneModifyDao;
	
	@Override
	public WPassPhoneModifyEntity queryObject(Long id){
		return wPassPhoneModifyDao.queryObject(id);
	}
	
	@Override
	public List<WPassPhoneModifyEntity> queryList(Map<String, Object> map){
		return wPassPhoneModifyDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return wPassPhoneModifyDao.queryTotal(map);
	}
	
	@Override
	public void save(WPassPhoneModifyEntity wPassPhoneModify){
		wPassPhoneModifyDao.save(wPassPhoneModify);
	}
	
	@Override
	public void update(WPassPhoneModifyEntity wPassPhoneModify){
		wPassPhoneModifyDao.update(wPassPhoneModify);
	}
	
	@Override
	public void delete(Long id){
		wPassPhoneModifyDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		wPassPhoneModifyDao.deleteBatch(ids);
	}
	
}
