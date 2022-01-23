package top.codezx.schedule.controller;

import com.github.pagehelper.PageInfo;
import top.codezx.common.constant.ControllerConstant;
import top.codezx.common.web.base.BaseController;
import top.codezx.common.web.domain.request.PageDomain;
import top.codezx.common.web.domain.response.module.ResultTable;
import top.codezx.schedule.domain.ScheduleLog;
import top.codezx.schedule.service.IScheduleLogService;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Describe: 定时任务日志控制器
 * Author: 就眠仪式
 * createTime: 2019/10/23
 */
@RestController
@Api(tags = {"任务日志"})
@RequestMapping(ControllerConstant.API_SCHEDULE_PREFIX + "log")
public class ScheduleLogController extends BaseController {

    /**
     * Describe: 定时任务日志服务
     */
    @Resource
    private IScheduleLogService scheduleLogService;

    /**
     * Describe: 获取定时任务日志列表视图
     * Param: modelAndView
     * Return: 定时任务日志列表视图
     */
    @GetMapping("main")
    @PreAuthorize("hasPermission('/schdule/log/main','sch:log:main')")
    public ModelAndView main() {
        return jumpPage("schedule/log/main");
    }

    /**
     * Describe: 获取定时任务日志列表数据
     * Param: PageDomain
     * Return: Result
     */
    @GetMapping("data")
    @PreAuthorize("hasPermission('/schdule/log/data','sch:log:data')")
    public ResultTable data(ScheduleLog scheduleLogBean, PageDomain pageDomain) {
        PageInfo<ScheduleLog> pageInfo = scheduleLogService.page(scheduleLogBean, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }
}
