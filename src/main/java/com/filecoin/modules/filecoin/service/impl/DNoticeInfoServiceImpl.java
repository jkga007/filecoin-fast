package com.filecoin.modules.filecoin.service.impl;

import com.filecoin.modules.filecoin.dao.DNoticeInfoDao;
import com.filecoin.modules.filecoin.entity.DNoticeInfoEntity;
import com.filecoin.modules.filecoin.service.DNoticeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("dNoticeInfoService")
public class DNoticeInfoServiceImpl implements DNoticeInfoService {
    @Autowired
    private DNoticeInfoDao dNoticeInfoDao;

    @Override
    public DNoticeInfoEntity queryObject(Long id) {
        return dNoticeInfoDao.queryObject(id);
    }

    @Override
    public List<DNoticeInfoEntity> queryList(Map<String, Object> map) {
        return dNoticeInfoDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return dNoticeInfoDao.queryTotal(map);
    }

    @Override
    public void save(DNoticeInfoEntity dNoticeInfo) {
        dNoticeInfoDao.save(dNoticeInfo);
    }

    @Override
    public void update(DNoticeInfoEntity dNoticeInfo) {
        dNoticeInfoDao.update(dNoticeInfo);
    }

    @Override
    public void delete(Long id) {
        dNoticeInfoDao.delete(id);
    }

    @Override
    public void deleteBatch(Long[] ids) {
        dNoticeInfoDao.deleteBatch(ids);
    }

    @Override
    public List<DNoticeInfoEntity> queryAll() {
        return dNoticeInfoDao.queryAll();
    }
}
