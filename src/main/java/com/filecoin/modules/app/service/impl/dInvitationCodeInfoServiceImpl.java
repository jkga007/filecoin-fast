package com.filecoin.modules.app.service.impl;
import java.util.List;
import java.util.Map;

import com.filecoin.modules.app.dao.DInvitationCodeInfoDao;
import com.filecoin.modules.app.entity.dInvitationCodeInfo;
import com.filecoin.modules.app.service.DInvitationCodeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dInvitationCodeInfoService")
public class dInvitationCodeInfoServiceImpl implements DInvitationCodeInfoService{
    @Autowired
    private DInvitationCodeInfoDao dInvitationCodeInfoDao;
    @Override
    public long getdInvitationCodeInfoRowCount(){
        return dInvitationCodeInfoDao.getdInvitationCodeInfoRowCount();
    }
    @Override
    public List<dInvitationCodeInfo> selectdInvitationCodeInfo(){
        return dInvitationCodeInfoDao.selectdInvitationCodeInfo();
    }
    @Override
    public dInvitationCodeInfo selectdInvitationCodeInfoByObj(dInvitationCodeInfo obj){
        return dInvitationCodeInfoDao.selectdInvitationCodeInfoByObj(obj);
    }
    @Override
    public Integer selectCountbyInvitationCode(Map<String,Object> map){
        return dInvitationCodeInfoDao.selectCountbyInvitationCode(map);
    }
    @Override
    public dInvitationCodeInfo selectdInvitationCodeInfoById(Object id){
        return dInvitationCodeInfoDao.selectdInvitationCodeInfoById(id);
    }
    @Override
    public int insertdInvitationCodeInfo(dInvitationCodeInfo value){
        return dInvitationCodeInfoDao.insertdInvitationCodeInfo(value);
    }
    @Override
    public int insertNonEmptydInvitationCodeInfo(dInvitationCodeInfo value){
        return dInvitationCodeInfoDao.insertNonEmptydInvitationCodeInfo(value);
    }
    @Override
    public int deletedInvitationCodeInfoById(Object id){
        return dInvitationCodeInfoDao.deletedInvitationCodeInfoById(id);
    }
    @Override
    public int updatedInvitationCodeInfoById(dInvitationCodeInfo enti){
        return dInvitationCodeInfoDao.updatedInvitationCodeInfoById(enti);
    }
    @Override
    public int updateNonEmptydInvitationCodeInfoById(dInvitationCodeInfo enti){
        return dInvitationCodeInfoDao.updateNonEmptydInvitationCodeInfoById(enti);
    }

    public DInvitationCodeInfoDao getdInvitationCodeInfoDao() {
        return this.dInvitationCodeInfoDao;
    }

    public void setdInvitationCodeInfoDao(DInvitationCodeInfoDao dInvitationCodeInfoDao) {
        this.dInvitationCodeInfoDao = dInvitationCodeInfoDao;
    }

}