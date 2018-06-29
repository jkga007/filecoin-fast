package com.filecoin.modules.filecoin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.filecoin.common.utils.Constant;
import com.filecoin.modules.sys.controller.AbstractController;
import com.filecoin.modules.sys.entity.SysUserEntity;
import com.filecoin.modules.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.filecoin.modules.filecoin.entity.WSendMessageEntity;
import com.filecoin.modules.filecoin.service.WSendMessageService;
import com.filecoin.common.utils.PageUtils;
import com.filecoin.common.utils.Query;
import com.filecoin.common.utils.JsonResult;




/**
 * 
 * 
 * @author r25437&g20416
 * @email support@filecoinon.com
 * @date 2018-06-27 16:59:15
 */
@RestController
@RequestMapping("/filecoin/wsendmessage")
public class WSendMessageController extends AbstractController{
	@Autowired
	private WSendMessageService wSendMessageService;

	@Autowired
	private SysUserService sysUserService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("filecoin:wsendmessage:list")
	public JsonResult list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<WSendMessageEntity> wSendMessageList = wSendMessageService.queryList(query);
		int total = wSendMessageService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(wSendMessageList, total, query.getLimit(), query.getPage());
		
		return JsonResult.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("filecoin:wsendmessage:info")
	public JsonResult info(@PathVariable("id") Long id){
		WSendMessageEntity wSendMessage = wSendMessageService.queryObject(id);
		
		return JsonResult.ok().put("wSendMessage", wSendMessage);
	}

	/**
	 * 验证码验证
	 */
	@RequestMapping("/validate")
	public JsonResult validate(@PathVariable("mobile") String mobile,@PathVariable("id") Long userId,
							   @PathVariable("identifyingCode") String identifyingCode){
		//提取验证码信息
		Map<String,Object> parasMap = new HashMap<>();
		parasMap.put("mobile",mobile);
		parasMap.put("userId",userId);
		parasMap.put("identifyingCode",identifyingCode);
		List<WSendMessageEntity> wSendMessageEntityList = wSendMessageService.queryList(parasMap);
		if(wSendMessageEntityList!=null && wSendMessageEntityList.size()>0){
			//
		}else{
			return JsonResult.error("验证码无效，请重新输入！");
		}
		return JsonResult.ok();
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("filecoin:wsendmessage:save")
	public JsonResult save(@PathVariable("mobile") String mobile,@PathVariable("id") Long userId){
		//获取当前用户状态及用户信息
		SysUserEntity sysUser=sysUserService.queryObject(userId);
		if(sysUser!=null){
			return JsonResult.error("无此用户，请确认用户信息是否正确！");
		}else if(sysUser.getStatus()!=Constant.UserStatus.NEED_BIND_MOBILE.getValue()){
			return JsonResult.error("用户当前状态不允许进行手机验证码下发，请稍后再试！");
		}

		//根据手机号码获取是否已经被注册
		SysUserEntity sysUserEntity=sysUserService.queryByMobile(mobile);
		if(sysUserEntity!=null){
			return JsonResult.error("该手机号码已被注册，请更换手机号码");
		}

		//手机号码15分钟内下发过短信，不让注册
		Map<String,Object> parasMap = new HashMap<>();
		parasMap.put("mobile",mobile);
		parasMap.put("insertTimeInt",15);
		List<WSendMessageEntity> wSendMessageEntityList = wSendMessageService.queryList(parasMap);
		if(wSendMessageEntityList!=null && wSendMessageEntityList.size()>0){
			return JsonResult.error("该手机号码尚在验证阶段，请更换手机号码");
		}

		WSendMessageEntity wSendMessageEntity = new WSendMessageEntity();
		wSendMessageEntity.setUserId(userId);
		wSendMessageEntity.setMobile(mobile);
		wSendMessageEntity.setStatus(Constant.SendStatus.NEED_SEND.getValue());
		Random generate = new Random();
		int nextInt = generate.nextInt(999999);
		if(nextInt<100000) nextInt = nextInt + 100000;
		wSendMessageEntity.setIdentifyingCode(nextInt);
		wSendMessageService.save(wSendMessageEntity);
		
		return JsonResult.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("filecoin:wsendmessage:update")
	public JsonResult update(@RequestBody WSendMessageEntity wSendMessage){
		wSendMessageService.update(wSendMessage);
		
		return JsonResult.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("filecoin:wsendmessage:delete")
	public JsonResult delete(@RequestBody Long[] ids){
		wSendMessageService.deleteBatch(ids);
		
		return JsonResult.ok();
	}
	
}
