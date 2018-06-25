package com.filecoin.modules.filecoin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.filecoin.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.filecoin.modules.filecoin.entity.DInvitationCodeInfoEntity;
import com.filecoin.modules.filecoin.service.DInvitationCodeInfoService;
import com.filecoin.common.utils.PageUtils;
import com.filecoin.common.utils.Query;
import com.filecoin.common.utils.JsonResult;




/**
 * 
 * 
 * @author r25437
 * @email support@filecoinon.com
 * @date 2018-06-25 11:42:54
 */
@RestController
@RequestMapping("/filecoin/dinvitationcodeinfo")
public class DInvitationCodeInfoController extends AbstractController {
	@Autowired
	private DInvitationCodeInfoService dInvitationCodeInfoService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("filecoin:dinvitationcodeinfo:list")
	public JsonResult list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<DInvitationCodeInfoEntity> dInvitationCodeInfoList = dInvitationCodeInfoService.queryList(query);
		int total = dInvitationCodeInfoService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(dInvitationCodeInfoList, total, query.getLimit(), query.getPage());
		
		return JsonResult.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{invitationCode}")
	@RequiresPermissions("filecoin:dinvitationcodeinfo:info")
	public JsonResult info(@PathVariable("invitationCode") String invitationCode){
		DInvitationCodeInfoEntity dInvitationCodeInfo = dInvitationCodeInfoService.queryObject(invitationCode);
		
		return JsonResult.ok().put("dInvitationCodeInfo", dInvitationCodeInfo);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("filecoin:dinvitationcodeinfo:save")
	public JsonResult save(@RequestBody DInvitationCodeInfoEntity dInvitationCodeInfo){
		dInvitationCodeInfoService.save(dInvitationCodeInfo);
		
		return JsonResult.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("filecoin:dinvitationcodeinfo:update")
	public JsonResult update(@RequestBody DInvitationCodeInfoEntity dInvitationCodeInfo){
		dInvitationCodeInfoService.update(dInvitationCodeInfo);
		
		return JsonResult.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("filecoin:dinvitationcodeinfo:delete")
	public JsonResult delete(@RequestBody String[] invitationCodes){
		dInvitationCodeInfoService.deleteBatch(invitationCodes);
		
		return JsonResult.ok();
	}

	/*
	 *根据邀请码及当前登录用户获取邀请码已经邀请人数量
	 */
	@PostMapping("getCountByInvitationCode")
	public JsonResult getCountByInvitationCode(){
		Long userId = getUserId();
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("userId",userId);
		int count = dInvitationCodeInfoService.selectCountbyInvitationCode(paramMap);
		return JsonResult.ok().put("count",count);
	}
	
}
