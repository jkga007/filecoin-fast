package com.filecoin.modules.filecoin.service;

import com.filecoin.modules.filecoin.entity.WSendMessageEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author r25437&g20416
 * @email support@filecoinon.com
 * @date 2018-06-27 16:59:15
 */
public interface WSendMessageService {
	
	WSendMessageEntity queryObject(Long id);
	
	List<WSendMessageEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(WSendMessageEntity wSendMessage);
	
	void update(WSendMessageEntity wSendMessage);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}
