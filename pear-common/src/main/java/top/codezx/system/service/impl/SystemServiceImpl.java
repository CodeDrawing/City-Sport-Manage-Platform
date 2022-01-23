package top.codezx.system.service.impl;


import top.codezx.system.service.SystemService;
import top.codezx.system.domain.*;
import top.codezx.system.mapper.*;
import top.codezx.system.service.ISysLogService;
import org.springframework.stereotype.Service;
import top.codezx.system.domain.SysLog;
import top.codezx.system.domain.SysRole;
import top.codezx.system.domain.SysUser;
import top.codezx.system.mapper.SysPowerMapper;
import top.codezx.system.mapper.SysRoleMapper;
import top.codezx.system.mapper.SysUserMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * Describe: 对外开放的公用服务
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@Service
public class SystemServiceImpl implements SystemService {

    @Resource
    private ISysLogService sysLogService;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysPowerMapper sysPowerMapper;


    @Override
    public SysUser getUserByName(String username) {
        SysUser sysUser = sysUserMapper.selectByUsername(username);
        if (sysUser != null) {
            sysUser.setPowerList(sysPowerMapper.selectByUsername(username));
        }
        return sysUser;
    }

    @Override
    public SysUser getUserById(String id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public List<SysRole> getRolesByUsername(String username) {
        return sysRoleMapper.selectByUsername(username);
    }

    @Override
    public Boolean saveLog(SysLog log) {
        return sysLogService.save(log);
    }
}
