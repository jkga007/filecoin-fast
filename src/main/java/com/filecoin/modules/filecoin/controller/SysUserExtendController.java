package com.filecoin.modules.filecoin.controller;

import com.filecoin.common.utils.JsonResult;
import com.filecoin.common.utils.PageUtils;
import com.filecoin.common.utils.Query;
import com.filecoin.modules.filecoin.entity.SysUserExtendEntity;
import com.filecoin.modules.filecoin.service.SysUserExtendService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @author g20416
 * @email support@filecoinon.com
 * @date 2018-06-27 23:22:53
 */
@RestController
@RequestMapping("/sysUserExtend")
public class SysUserExtendController {
    @Autowired
    private SysUserExtendService sysUserExtendService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public JsonResult list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<SysUserExtendEntity> sysUserExtendList = sysUserExtendService.queryList(query);
        int total = sysUserExtendService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(sysUserExtendList, total, query.getLimit(), query.getPage());

        return JsonResult.ok().put("page", pageUtil);
    }

    @RequestMapping("/getSomeCount")
    public JsonResult getSomeCount() {
        List<SysUserExtendEntity> extens = sysUserExtendService.queryList(null);
        int bandWidthI = 0;
        int storageLenI = 0;
        for (SysUserExtendEntity exten : extens) {
            String bandWidth = exten.getBandWidth();
            String storageLen = exten.getStorageLen();
            if (StringUtils.isNotBlank(bandWidth)) {
                int i = Integer.parseInt(bandWidth);
                bandWidthI = bandWidthI + i;
            }
            if (StringUtils.isNotBlank(storageLen)) {
                int j = Integer.parseInt(storageLen);
                storageLenI = storageLenI + j;
            }
        }
        int size = extens.size();

        return JsonResult.ok().put("pcs", "" + size).put("pcs", "" + size).put("bindwidth", "" + bandWidthI).put("storageLen", "" + storageLenI);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public JsonResult info(@PathVariable("id") Long id) {
        SysUserExtendEntity sysUserExtend = sysUserExtendService.queryObject(id);

        return JsonResult.ok().put("sysUserExtend", sysUserExtend);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public JsonResult save(@RequestBody SysUserExtendEntity sysUserExtend) {
        sysUserExtendService.save(sysUserExtend);

        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public JsonResult update(@RequestBody SysUserExtendEntity sysUserExtend) {
        sysUserExtendService.update(sysUserExtend);

        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public JsonResult delete(@RequestBody Long[] ids) {
        sysUserExtendService.deleteBatch(ids);

        return JsonResult.ok();
    }

}
