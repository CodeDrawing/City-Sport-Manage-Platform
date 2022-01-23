package top.codezx.secure.process;

import top.codezx.common.plugin.logging.aop.enums.BusinessType;
import top.codezx.common.plugin.logging.aop.enums.LoggingType;
import top.codezx.common.tools.SecurityUtil;
import top.codezx.common.tools.SequenceUtil;
import top.codezx.secure.session.SecureSessionService;
import top.codezx.system.domain.SysLog;
import top.codezx.system.domain.SysUser;
import top.codezx.system.service.ISysLogService;
import top.codezx.system.service.ISysUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class SecureRememberMeHandler implements AuthenticationSuccessHandler {

    @Resource
    private ISysLogService sysLogService;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private SessionRegistry sessionRegistry;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 记录日志
        SysLog sysLog = new SysLog();
        sysLog.setId(SequenceUtil.makeStringId());
        sysLog.setTitle("Remember Me");
        sysLog.setDescription("登录成功");
        sysLog.setBusinessType(BusinessType.OTHER);
        sysLog.setSuccess(true);
        sysLog.setLoggingType(LoggingType.LOGIN);
        sysLogService.save(sysLog);

        // 更新用户
        SysUser sysUser = new SysUser();
        // 获取最近登录时间
        LocalDateTime now = LocalDateTime.now();
        sysUser.setUserId(((SysUser) SecurityUtil.currentUser()).getUserId());
        sysUser.setLastTime(now);
        sysUserService.update(sysUser);

        SysUser currentUser = (SysUser) authentication.getPrincipal();
        currentUser.setLastTime(now);
        request.getSession().setAttribute("currentUser", currentUser);

        SecureSessionService.expiredSession(request, sessionRegistry);

        // 注册新的SessionInformation
        sessionRegistry.registerNewSession(request.getSession().getId(), authentication.getPrincipal());
    }
}
