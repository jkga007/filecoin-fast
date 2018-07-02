package com.filecoin.modules.filecoin.service;

import com.filecoin.modules.filecoin.entity.WPassPhoneModifyEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author g20416
 * @email support@filecoinon.com
 * @date 2018-07-02 21:29:04
 */
public interface WPassPhoneModifyService {
	
	WPassPhoneModifyEntity queryObject(Long id);
	
	List<WPassPhoneModifyEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(WPassPhoneModifyEntity wPassPhoneModify);
	
	void update(WPassPhoneModifyEntity wPassPhoneModify);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}
