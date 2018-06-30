package com.filecoin.modules.filecoin.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.filecoin.modules.filecoin.entity.SysUserExtendEntity;
import com.filecoin.modules.filecoin.service.SysUserExtendService;
import com.filecoin.common.utils.PageUtils;
import com.filecoin.common.utils.Query;
import com.filecoin.common.utils.JsonResult;




/**
 * 
 * 
 * @author g20416
 * @email support@filecoinon.com
 * @date 2018-06-27 23:22:53
 */
@RestController
@RequestMapping("/sysuserextend")
public class SysUserExtendController {
	@Autowired
	private SysUserExtendService sysUserExtendService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("filecoin:sysuserextend:list")
	public JsonResult list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<SysUserExtendEntity> sysUserExtendList = sysUserExtendService.queryList(query);
		int total = sysUserExtendService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(sysUserExtendList, total, query.getLimit(), query.getPage());
		
		return JsonResult.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("filecoin:sysuserextend:info")
	public JsonResult info(@PathVariable("id") Long id){
		SysUserExtendEntity sysUserExtend = sysUserExtendService.queryObject(id);
		
		return JsonResult.ok().put("sysUserExtend", sysUserExtend);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("filecoin:sysuserextend:save")
	public JsonResult save(@RequestBody SysUserExtendEntity sysUserExtend){
		sysUserExtendService.save(sysUserExtend);
		
		return JsonResult.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("filecoin:sysuserextend:update")
	public JsonResult update(@RequestBody SysUserExtendEntity sysUserExtend){
		sysUserExtendService.update(sysUserExtend);
		
		return JsonResult.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("filecoin:sysuserextend:delete")
	public JsonResult delete(@RequestBody Long[] ids){
		sysUserExtendService.deleteBatch(ids);
		
		return JsonResult.ok();
	}
	
}
