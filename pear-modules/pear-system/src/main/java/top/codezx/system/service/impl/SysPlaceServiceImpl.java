package top.codezx.system.service.impl;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import top.codezx.common.web.domain.request.PageDomain;
import top.codezx.system.domain.SysPlace;
import top.codezx.system.mapper.SysPlaceMapper;
import top.codezx.system.service.ISysPlaceService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysPlaceServiceImpl implements ISysPlaceService {

    @Resource
    SysPlaceMapper sysPlaceMapper;

    public PageInfo<SysPlace> page(SysPlace param, PageDomain pageDomain){
        PageHelper.startPage(pageDomain.getPage(), pageDomain.getLimit());
        List<SysPlace> sysPlaces = sysPlaceMapper.selectList(param);
        return new PageInfo<>(sysPlaces);
    };


}
