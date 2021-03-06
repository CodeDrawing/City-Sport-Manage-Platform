package top.codezx.system.service.impl;

import top.codezx.common.plugin.logging.aop.enums.LoggingType;
import top.codezx.common.plugin.logging.aop.enums.RequestMethod;
import top.codezx.common.tools.SecurityUtil;
import top.codezx.common.tools.ServletUtil;
import top.codezx.system.domain.SysLog;
import top.codezx.system.domain.SysUser;
import top.codezx.system.mapper.SysLogMapper;
import top.codezx.system.service.ISysLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Describe: 日 志 服 务 接 口 实 现
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@Service
public class SysLogServiceImpl implements ISysLogService {

    @Resource
    private SysLogMapper sysLogMapper;

    @Override
    public boolean save(SysLog sysLog) {
        sysLog.setOperateAddress(ServletUtil.getRemoteHost());
        sysLog.setMethod(ServletUtil.getRequestURI());
        sysLog.setCreateTime(LocalDateTime.now());
        sysLog.setRequestMethod(RequestMethod.valueOf(ServletUtil.getMethod()));
        sysLog.setOperateUrl(ServletUtil.getRequestURI());
        sysLog.setBrowser(ServletUtil.getBrowser());
        sysLog.setRequestBody(ServletUtil.getQueryParam());
        sysLog.setSystemOs(ServletUtil.getSystem());
        SysUser currentUser = SecurityUtil.currentUser();
        sysLog.setOperateName(null != currentUser ? currentUser.getUsername() : "未登录用户");
        int result = sysLogMapper.insert(sysLog);
        return result > 0;
    }

    @Override
    public List<SysLog> data(LoggingType loggingType, LocalDateTime startTime, LocalDateTime endTime) {
        return sysLogMapper.selectList(loggingType, startTime, endTime);
    }

    @Override
    public SysLog getById(String id) {
        return sysLogMapper.getById(id);
    }

    @Override
    public List<SysLog> selectTopLoginLog(String operateName) {
        return sysLogMapper.selectTopLoginLog(operateName);
    }

}
