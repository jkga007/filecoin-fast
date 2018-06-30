package com.filecoin.modules.filecoin.controller;

import com.filecoin.common.utils.JsonResult;
import com.filecoin.common.utils.PageUtils;
import com.filecoin.common.utils.Query;
import com.filecoin.modules.filecoin.entity.WSendEmailEntity;
import com.filecoin.modules.filecoin.service.WSendEmailService;
import com.filecoin.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;




/**
 * 
 * 
 * @author g20416
 * @email support@filecoinon.com
 * @date 2018-06-27 20:16:41
 */
@RestController
@RequestMapping("/wsendemail")
public class WSendEmailController extends AbstractController {
	@Autowired
	private WSendEmailService wSendEmailService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("filecoin:wsendemail:list")
	public JsonResult list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<WSendEmailEntity> wSendEmailList = wSendEmailService.queryList(query);
		int total = wSendEmailService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(wSendEmailList, total, query.getLimit(), query.getPage());
		
		return JsonResult.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("filecoin:wsendemail:info")
	public JsonResult info(@PathVariable("id") Long id){
		WSendEmailEntity wSendEmail = wSendEmailService.queryObject(id);
		
		return JsonResult.ok().put("wSendEmail", wSendEmail);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("filecoin:wsendemail:save")
	public JsonResult save(@RequestBody WSendEmailEntity wSendEmail){
		wSendEmailService.save(wSendEmail);
		
		return JsonResult.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("filecoin:wsendemail:update")
	public JsonResult update(@RequestBody WSendEmailEntity wSendEmail){
		wSendEmailService.update(wSendEmail);
		
		return JsonResult.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("filecoin:wsendemail:delete")
	public JsonResult delete(@RequestBody Long[] ids){
		wSendEmailService.deleteBatch(ids);
		
		return JsonResult.ok();
	}
	
}
