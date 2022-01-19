package top.codezx.system.service;

import top.codezx.common.web.domain.request.PageDomain;
import top.codezx.system.domain.SysPlace;

import java.util.List;

public interface ISysPlaceService {

    List<SysPlace> page(SysPlace param, PageDomain pageDomain);
}
