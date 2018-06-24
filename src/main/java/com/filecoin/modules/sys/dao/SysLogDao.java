package com.filecoin.modules.sys.dao;

import com.filecoin.modules.sys.entity.SysLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统日志
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-08 10:40:56
 */
@Mapper
public interface SysLogDao extends BaseDao<SysLogEntity> {
	
}
