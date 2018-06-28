package com.filecoin.modules.filecoin.dao;

import com.filecoin.modules.filecoin.entity.SysUserExtendEntity;
import com.filecoin.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author g20416
 * @email support@filecoinon.com
 * @date 2018-06-27 23:22:53
 */
@Mapper
public interface SysUserExtendDao extends BaseDao<SysUserExtendEntity> {
    SysUserExtendEntity queryObjectByUserId(Long userId);
}
