package top.codezx.system.mapper;


import org.apache.ibatis.annotations.Mapper;
import top.codezx.system.domain.SysArrivalInfo;
import top.codezx.system.domain.SysPlace;
import top.codezx.system.domain.SysUser;

import java.util.Date;
import java.util.List;


@Mapper
public interface SysPlaceMapper {
    /**
     * 获取场馆信息
     * @param param
     * @return 场馆列表
     */
    List<SysPlace> selectList(SysPlace param);

    /**
     * 根据用户名来获取用户信息
     * @param username
     * @return
     */
    SysUser arrivalLogin(String username);

    SysArrivalInfo alreadyHaveTheDate(String date,String placeName);

    int updateUnder18(SysArrivalInfo sysArrivalInfo);
    int updateUnder18To30(SysArrivalInfo sysArrivalInfo);
    int updateUnder31To60(SysArrivalInfo sysArrivalInfo);
    int updateAbove61(SysArrivalInfo sysArrivalInfo);
    SysPlace selectById(String placeId);


}
