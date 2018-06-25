package com.filecoin.modules.filecoin.dao;

import com.filecoin.modules.filecoin.entity.DInvitationCodeInfoEntity;
import com.filecoin.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 
 * 
 * @author r25437
 * @email support@filecoinon.com
 * @date 2018-06-25 11:42:54
 */
@Mapper
public interface DInvitationCodeInfoDao extends BaseDao<DInvitationCodeInfoEntity> {
    /**
     * 获得邀请码已注册用户数量
     * @return
     */
    Integer selectCountbyInvitationCode(Map<String,Object> map);
}
