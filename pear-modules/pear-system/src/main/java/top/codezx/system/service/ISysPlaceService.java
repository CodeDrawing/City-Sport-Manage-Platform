package top.codezx.system.service;

import com.github.pagehelper.PageInfo;
import top.codezx.common.web.domain.request.PageDomain;
import top.codezx.common.web.domain.response.Result;
import top.codezx.system.domain.SysPlace;
import top.codezx.system.domain.SysUser;

import java.util.List;

public interface ISysPlaceService {

    PageInfo<SysPlace> page(SysPlace param, PageDomain pageDomain);

    SysUser arrivalLogin(String username, String password);


}
