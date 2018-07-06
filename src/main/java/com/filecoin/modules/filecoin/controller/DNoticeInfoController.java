package com.filecoin.modules.filecoin.controller;

import com.filecoin.common.utils.JsonResult;
import com.filecoin.common.utils.PageUtils;
import com.filecoin.common.utils.Query;
import com.filecoin.modules.filecoin.entity.DNoticeInfoEntity;
import com.filecoin.modules.filecoin.service.DNoticeInfoService;
import com.filecoin.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @author r25437&g20416
 * @email support@filecoinon.com
 * @date 2018-07-06 15:51:45
 */
@RestController
@RequestMapping("/dnoticeinfo")
public class DNoticeInfoController extends AbstractController {
    @Autowired
    private DNoticeInfoService dNoticeInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("filecoin:dnoticeinfo:list")
    public JsonResult list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<DNoticeInfoEntity> dNoticeInfoList = dNoticeInfoService.queryList(query);
        int total = dNoticeInfoService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(dNoticeInfoList, total, query.getLimit(), query.getPage());

        return JsonResult.ok().put("page", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("filecoin:dnoticeinfo:info")
    public JsonResult info(@PathVariable("id") Long id) {
        DNoticeInfoEntity dNoticeInfo = dNoticeInfoService.queryObject(id);

        return JsonResult.ok().put("dNoticeInfo", dNoticeInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("filecoin:dnoticeinfo:save")
    public JsonResult save(@RequestBody DNoticeInfoEntity dNoticeInfo) {
        dNoticeInfoService.save(dNoticeInfo);

        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("filecoin:dnoticeinfo:update")
    public JsonResult update(@RequestBody DNoticeInfoEntity dNoticeInfo) {
        dNoticeInfoService.update(dNoticeInfo);

        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("filecoin:dnoticeinfo:delete")
    public JsonResult delete(@RequestBody Long[] ids) {
        dNoticeInfoService.deleteBatch(ids);

        return JsonResult.ok();
    }

    /**
     * 获取所有公告
     */
    @PostMapping("/getAllNotice")
    public JsonResult getAllNotice() {

        List<DNoticeInfoEntity> dNoticeInfoEntities = dNoticeInfoService.queryAll();

        return JsonResult.ok().put("notices", dNoticeInfoEntities);
    }

}
