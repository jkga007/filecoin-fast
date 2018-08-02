package com.filecoin.modules.app.service.impl;


import com.filecoin.common.exception.FileCoinException;
import com.filecoin.common.validator.Assert;
import com.filecoin.modules.app.dao.UserDao;
import com.filecoin.modules.app.entity.UserEntity;
import com.filecoin.modules.app.service.UserService;
import com.filecoin.modules.sys.dao.SysUserDao;
import com.filecoin.modules.sys.entity.SysUserEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private SysUserDao sysUserDao;

    @Override
    public UserEntity queryObject(Long userId) {
        return userDao.queryObject(userId);
    }

    @Override
    public List<UserEntity> queryList(Map<String, Object> map) {
        return userDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return userDao.queryTotal(map);
    }

    @Override
    public void save(String mobile, String password) {
        UserEntity user = new UserEntity();
        user.setMobile(mobile);
        user.setUsername(mobile);
        user.setPassword(DigestUtils.sha256Hex(password));
        user.setCreateTime(new Date());
        userDao.save(user);
    }

    @Override
    public void update(UserEntity user) {
        userDao.update(user);
    }

    @Override
    public void delete(Long userId) {
        userDao.delete(userId);
    }

    @Override
    public void deleteBatch(Long[] userIds) {
        userDao.deleteBatch(userIds);
    }

    @Override
    public UserEntity queryByMobile(String mobile) {
        return userDao.queryByMobile(mobile);
    }

    @Override
    public SysUserEntity queryByMobile2(String mobile) {
        return sysUserDao.queryByMobile(mobile);
    }

    @Override
    public long login(String mobile, String password) {
        SysUserEntity user = queryByMobile2(mobile);
        Assert.isNull(user, "手机号或密码错误");
        
        //账号不存在、密码错误
        if (!user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex())) {
            throw new FileCoinException("手机号或密码错误");
        }

        return user.getUserId();
    }
}
