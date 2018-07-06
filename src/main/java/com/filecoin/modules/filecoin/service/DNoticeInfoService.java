package com.filecoin.modules.filecoin.service;

import com.filecoin.modules.filecoin.entity.DNoticeInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * @author r25437&g20416
 * @email support@filecoinon.com
 * @date 2018-07-06 15:51:45
 */
public interface DNoticeInfoService {

    DNoticeInfoEntity queryObject(Long id);

    List<DNoticeInfoEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(DNoticeInfoEntity dNoticeInfo);

    void update(DNoticeInfoEntity dNoticeInfo);

    void delete(Long id);

    void deleteBatch(Long[] ids);

    List<DNoticeInfoEntity> queryAll();
}
