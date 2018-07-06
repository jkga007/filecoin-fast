package com.filecoin.modules.filecoin.dao;

import com.filecoin.modules.filecoin.entity.DNoticeInfoEntity;
import com.filecoin.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author r25437&g20416
 * @email support@filecoinon.com
 * @date 2018-07-06 15:51:45
 */
@Mapper
public interface DNoticeInfoDao extends BaseDao<DNoticeInfoEntity> {
    List<DNoticeInfoEntity> queryAll();
}
