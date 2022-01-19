package top.codezx.system.service;

import com.github.pagehelper.PageInfo;
import top.codezx.common.web.domain.request.PageDomain;
import top.codezx.system.domain.SysPlace;

import java.util.List;

public interface ISysPlaceService {

    PageInfo<SysPlace> page(SysPlace param, PageDomain pageDomain);



}
