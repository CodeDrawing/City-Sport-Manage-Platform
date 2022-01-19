package top.codezx.system.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import top.codezx.common.constant.ControllerConstant;
import top.codezx.common.plugin.logging.aop.annotation.Logging;
import top.codezx.common.plugin.logging.aop.enums.BusinessType;
import top.codezx.common.web.base.BaseController;
import top.codezx.common.web.domain.request.PageDomain;
import top.codezx.common.web.domain.response.module.ResultTable;
import top.codezx.system.domain.SysPlace;
import top.codezx.system.domain.SysUser;
import top.codezx.system.service.ISysPlaceService;

import javax.annotation.Resource;

@RestController
@Api(tags = {"场所管理"})
@RequestMapping(ControllerConstant.API_SYSTEM_PREFIX + "place")
public class SysPlaceController extends BaseController {
    /**
     * Describe: 基础路径
     */
    private static String MODULE_PATH = "system/place/";

    @Resource
    private ISysPlaceService iSysPlaceService;


    /**
     * Describe: 获取场所列表视图
     * Param ModelAndView
     * Return 场所列表视图
     */
    @GetMapping("main")
    @ApiOperation(value = "获取场所列表视图")
    @PreAuthorize("hasPermission('/system/place/main','sys:place:main')")
    public ModelAndView main() {
        return jumpPage(MODULE_PATH + "main");
    }


    /**
     * Describe: 获取场所列表数据
     * Param ModelAndView
     * Return 场所列表数据
     */
    @GetMapping("data")
    @ApiOperation(value = "获取场所列表数据")
    @PreAuthorize("hasPermission('/system/place/data','sys:place:data')")
    @Logging(title = "查询场所", describe = "查询场所", type = BusinessType.QUERY)
    public ResultTable data(PageDomain pageDomain, SysPlace param) {
        PageInfo<SysPlace> pageInfo = iSysPlaceService.page(param, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }




}
