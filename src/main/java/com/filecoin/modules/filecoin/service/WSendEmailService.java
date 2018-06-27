package com.filecoin.modules.filecoin.service;

import com.filecoin.modules.filecoin.entity.WSendEmailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author g20416
 * @email support@filecoinon.com
 * @date 2018-06-27 20:16:41
 */
public interface WSendEmailService {
	
	WSendEmailEntity queryObject(Long id);
	
	List<WSendEmailEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(WSendEmailEntity wSendEmail);
	
	void update(WSendEmailEntity wSendEmail);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	WSendEmailEntity queryOneNearBy();
}
