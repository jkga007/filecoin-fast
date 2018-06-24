package com.filecoin.modules.oss.dao;

import com.filecoin.modules.oss.entity.SysOssEntity;
import com.filecoin.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传
 * 
 * @author r25437,g20416
 * @email support@filecoinon.com
 * @date 2017-03-25 12:13:26
 */
@Mapper
public interface SysOssDao extends BaseDao<SysOssEntity> {
	
}
