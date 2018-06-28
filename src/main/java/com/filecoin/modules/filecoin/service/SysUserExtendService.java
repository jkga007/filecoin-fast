package com.filecoin.modules.filecoin.service;

import com.filecoin.common.utils.JsonResult;
import com.filecoin.modules.filecoin.entity.SysUserExtendEntity;
import com.filecoin.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author g20416
 * @email support@filecoinon.com
 * @date 2018-06-27 23:22:53
 */
public interface SysUserExtendService {
	
	SysUserExtendEntity queryObject(Long id);
	
	List<SysUserExtendEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysUserExtendEntity sysUserExtend);
	
	void update(SysUserExtendEntity sysUserExtend);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	SysUserExtendEntity queryObjectByUserId(Long userId);

	void saveOrUpdate(SysUserExtendEntity sysUserExtend);

	void saveOrUpdateAndEditUser(SysUserExtendEntity sysUserExtend, SysUserEntity user);
	JsonResult saveOrUpdateAndActive(SysUserExtendEntity sysUserExtend, SysUserEntity user);
}
