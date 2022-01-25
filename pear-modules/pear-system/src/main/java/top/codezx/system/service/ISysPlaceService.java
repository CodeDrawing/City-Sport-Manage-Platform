package top.codezx.system.service;

import com.github.pagehelper.PageInfo;
import top.codezx.common.web.domain.request.PageDomain;
import top.codezx.common.web.domain.response.Result;
import top.codezx.system.domain.SysArrivalInfo;
import top.codezx.system.domain.SysPlace;
import top.codezx.system.domain.SysUser;

import java.util.Date;
import java.util.List;

public interface ISysPlaceService {

    PageInfo<SysPlace> page(SysPlace param, PageDomain pageDomain);

    SysUser arrivalLogin(String username);

    SysArrivalInfo alreadyHaveTheDate(String date,String placeName);
    //    数据库中没有今天该场所的信息，那么就使用这个方法进行添加记录
    int insertArrivalInfo(SysArrivalInfo sysArrivalInfo);

    boolean updateUnder18(SysArrivalInfo sysArrivalInfo);
    boolean updateUnder18To30(SysArrivalInfo sysArrivalInfo);
    boolean updateUnder31To60(SysArrivalInfo sysArrivalInfo);
    boolean updateAbove61(SysArrivalInfo sysArrivalInfo);

    boolean updateManNumber(SysArrivalInfo sysArrivalInfo);
    boolean updateWomanNumber(SysArrivalInfo sysArrivalInfo);

    SysPlace selectById(String placeId);
    boolean insert(SysPlace sysPlace);
    boolean deleteById(String placeId);
    boolean batchRemove(String[] ids);

    boolean updateById(SysPlace sysPlace);


}
