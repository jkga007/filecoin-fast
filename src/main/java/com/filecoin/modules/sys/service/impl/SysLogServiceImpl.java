package com.filecoin.modules.sys.service.impl;

import com.filecoin.common.utils.HttpContextUtils;
import com.filecoin.common.utils.IPUtils;
import com.filecoin.modules.sys.entity.SysLogEntity;
import com.filecoin.modules.sys.entity.SysUserEntity;
import com.filecoin.modules.sys.service.SysLogService;
import com.filecoin.modules.sys.dao.SysLogDao;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;



@Service("sysLogService")
public class SysLogServiceImpl implements SysLogService {
	@Autowired
	private SysLogDao sysLogDao;
	
	@Override
	public SysLogEntity queryObject(Long id){
		return sysLogDao.queryObject(id);
	}
	
	@Override
	public List<SysLogEntity> queryList(Map<String, Object> map){
		return sysLogDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysLogDao.queryTotal(map);
	}
	
	@Override
	public void save(SysLogEntity sysLog){
		sysLogDao.save(sysLog);
	}
	
	@Override
	public void delete(Long id){
		sysLogDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		sysLogDao.deleteBatch(ids);
	}

	@Override
	public void saveLog(String className,String userName,String params,long time,String operation) {
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setMethod(className);
		sysLog.setParams(params);
		//获取request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		//设置IP地址
		sysLog.setIp(IPUtils.getIpAddr(request));
		//用户名
		sysLog.setUsername(userName);

		sysLog.setTime(time);
		sysLog.setCreateDate(new Date());
		sysLog.setOperation(operation);
		//保存系统日志
		sysLogDao.save(sysLog);
	}
}
