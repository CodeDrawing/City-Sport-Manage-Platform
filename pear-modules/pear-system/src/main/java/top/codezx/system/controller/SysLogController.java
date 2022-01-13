package top.codezx.system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import top.codezx.common.constant.ControllerConstant;
import top.codezx.common.plugin.logging.aop.enums.LoggingType;
import top.codezx.common.web.base.BaseController;
import top.codezx.common.web.domain.request.PageDomain;
import top.codezx.common.web.domain.response.module.ResultTable;
import top.codezx.system.domain.SysLog;
import top.codezx.system.service.ISysLogService;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import top.codezx.common.web.domain.response.ResultController;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * Describe: 日志控制器
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@RestController
@Api(tags = {"系统日志"})
@RequestMapping(ControllerConstant.API_SYSTEM_PREFIX + "log")
public class SysLogController extends BaseController {

    @Resource
    private ISysLogService sysLogService;

    /**
     * Describe: 行为日志视图
     * Param: null
     * Return: ModelAndView
     */
    @GetMapping("main")
    @PreAuthorize("hasPermission('/system/log/main','sys:log:main')")
    public ModelAndView main() {
        return jumpPage("system/log/main");
    }

    /**
     * Describe: 操作日志数据
     * Param: null
     * Return: ResultTable
     */
    @GetMapping("operateLog")
    @PreAuthorize("hasPermission('/system/log/operateLog','sys:log:operateLog')")
    public ResultTable operateLog(PageDomain pageDomain, LocalDateTime startTime, LocalDateTime endTime) {
        PageHelper.startPage(pageDomain.getPage(), pageDomain.getLimit());
        PageInfo<SysLog> pageInfo = new PageInfo<>(sysLogService.data(LoggingType.OPERATE, startTime, endTime));
        return ResultController.pageTable(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * Describe: 登录日志数据
     * Param: null
     * Return: ModelAndView
     */
    @GetMapping("loginLog")
    @PreAuthorize("hasPermission('/system/log/loginLog','sys:log:loginLog')")
    public ResultTable loginLog(PageDomain pageDomain, LocalDateTime startTime, LocalDateTime endTime) {
        PageHelper.startPage(pageDomain.getPage(), pageDomain.getLimit());
        PageInfo<SysLog> pageInfo = new PageInfo<>(sysLogService.data(LoggingType.LOGIN, startTime, endTime));
        return ResultController.pageTable(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * Describe: 日志详情
     * Param: null
     * Return: ModelAndView
     */
    @GetMapping("/info")
    @PreAuthorize("hasPermission('/system/log/info','sys:log:info')")
    public ModelAndView details() {
        return jumpPage("system/log/info");
    }

}
