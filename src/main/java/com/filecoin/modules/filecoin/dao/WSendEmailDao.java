package com.filecoin.modules.filecoin.dao;

import com.filecoin.modules.filecoin.entity.WSendEmailEntity;
import com.filecoin.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author g20416
 * @email support@filecoinon.com
 * @date 2018-06-27 20:16:41
 */
@Mapper
public interface WSendEmailDao extends BaseDao<WSendEmailEntity> {
    /**
     * 查询一条注册待发送邮件
     * @return
     */
    WSendEmailEntity queryOneNearBy();

    /**
     * 查询一条修改密码待发送邮件
     * @return
     */
    WSendEmailEntity queryOneEditPassNearBy();

    /**
     * 查询一条找回密码待发送邮件
     * @return
     */
    WSendEmailEntity queryOneCallBackPassNearBy();
}
