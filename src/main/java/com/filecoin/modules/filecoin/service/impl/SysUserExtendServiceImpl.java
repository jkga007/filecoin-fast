package com.filecoin.modules.filecoin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.filecoin.modules.filecoin.dao.SysUserExtendDao;
import com.filecoin.modules.filecoin.entity.SysUserExtendEntity;
import com.filecoin.modules.filecoin.service.SysUserExtendService;



@Service("sysUserExtendService")
public class SysUserExtendServiceImpl implements SysUserExtendService {
	@Autowired
	private SysUserExtendDao sysUserExtendDao;
	
	@Override
	public SysUserExtendEntity queryObject(Long id){
		return sysUserExtendDao.queryObject(id);
	}
	
	@Override
	public List<SysUserExtendEntity> queryList(Map<String, Object> map){
		return sysUserExtendDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysUserExtendDao.queryTotal(map);
	}
	
	@Override
	public void save(SysUserExtendEntity sysUserExtend){
		sysUserExtendDao.save(sysUserExtend);
	}
	
	@Override
	public void update(SysUserExtendEntity sysUserExtend){
		sysUserExtendDao.update(sysUserExtend);
	}
	
	@Override
	public void delete(Long id){
		sysUserExtendDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		sysUserExtendDao.deleteBatch(ids);
	}
	
}
