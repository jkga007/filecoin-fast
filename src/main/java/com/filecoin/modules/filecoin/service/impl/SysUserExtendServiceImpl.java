package com.filecoin.modules.filecoin.service.impl;

import com.filecoin.common.utils.JsonResult;
import com.filecoin.modules.filecoin.service.DInvitationCodeInfoService;
import com.filecoin.modules.sys.dao.SysUserDao;
import com.filecoin.modules.sys.entity.SysUserEntity;
import com.filecoin.modules.sys.service.SysUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.filecoin.modules.filecoin.dao.SysUserExtendDao;
import com.filecoin.modules.filecoin.entity.SysUserExtendEntity;
import com.filecoin.modules.filecoin.service.SysUserExtendService;
import org.springframework.transaction.annotation.Transactional;


@Service("sysUserExtendService")
public class SysUserExtendServiceImpl implements SysUserExtendService {
	@Autowired
	private SysUserExtendDao sysUserExtendDao;
	@Autowired
	private SysUserDao userDao;
	@Autowired
	private DInvitationCodeInfoService dInvitationCodeInfoService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	
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

	@Override
	public SysUserExtendEntity queryObjectByUserId(Long userId) {
		return sysUserExtendDao.queryObjectByUserId(userId);
	}

	@Override
	@Transactional
	public void saveOrUpdate(SysUserExtendEntity sysUserExtend) {
		if(sysUserExtend.getId() != null){
			update(sysUserExtend);
		}else{
			save(sysUserExtend);
		}
	}

	@Override
	@Transactional
	public void saveOrUpdateAndEditUser(SysUserExtendEntity sysUserExtend, SysUserEntity user) {
		saveOrUpdate(sysUserExtend);
		userDao.update(user);
	}

	@Override
	public JsonResult saveOrUpdateAndActive(SysUserExtendEntity sysUserExtend, SysUserEntity user) {
		//修改扩展信息
		saveOrUpdate(sysUserExtend);
		//修改用户信息
		userDao.update(user);
		//生成激活码
		dInvitationCodeInfoService.createInvitationCodeByUser(user.getUserId());
		//登陆
		JsonResult jsonResult = sysUserTokenService.createToken(user.getUserId());
		return jsonResult;
	}
}
