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

import com.filecoin.modules.filecoin.entity.WPassPhoneModifyEntity;
import com.filecoin.modules.filecoin.service.WPassPhoneModifyService;
import com.filecoin.common.utils.PageUtils;
import com.filecoin.common.utils.Query;
import com.filecoin.common.utils.JsonResult;




/**
 * 
 * 
 * @author g20416
 * @email support@filecoinon.com
 * @date 2018-07-02 21:29:04
 */
@RestController
@RequestMapping("/wpassphonemodify")
public class WPassPhoneModifyController {
	@Autowired
	private WPassPhoneModifyService wPassPhoneModifyService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("filecoin:wpassphonemodify:list")
	public JsonResult list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<WPassPhoneModifyEntity> wPassPhoneModifyList = wPassPhoneModifyService.queryList(query);
		int total = wPassPhoneModifyService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(wPassPhoneModifyList, total, query.getLimit(), query.getPage());
		
		return JsonResult.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("filecoin:wpassphonemodify:info")
	public JsonResult info(@PathVariable("id") Long id){
		WPassPhoneModifyEntity wPassPhoneModify = wPassPhoneModifyService.queryObject(id);
		
		return JsonResult.ok().put("wPassPhoneModify", wPassPhoneModify);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("filecoin:wpassphonemodify:save")
	public JsonResult save(@RequestBody WPassPhoneModifyEntity wPassPhoneModify){
		wPassPhoneModifyService.save(wPassPhoneModify);
		
		return JsonResult.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("filecoin:wpassphonemodify:update")
	public JsonResult update(@RequestBody WPassPhoneModifyEntity wPassPhoneModify){
		wPassPhoneModifyService.update(wPassPhoneModify);
		
		return JsonResult.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("filecoin:wpassphonemodify:delete")
	public JsonResult delete(@RequestBody Long[] ids){
		wPassPhoneModifyService.deleteBatch(ids);
		
		return JsonResult.ok();
	}
	
}
