package top.codezx.system.service.impl;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codezx.common.web.domain.request.PageDomain;
import top.codezx.system.domain.SysArrivalInfo;
import top.codezx.system.domain.SysPlace;
import top.codezx.system.domain.SysUser;
import top.codezx.system.mapper.SysPlaceMapper;
import top.codezx.system.service.ISysPlaceService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static top.codezx.common.web.domain.response.Result.decide;

@Service
public class SysPlaceServiceImpl implements ISysPlaceService {

    @Resource
    SysPlaceMapper sysPlaceMapper;


    public PageInfo<SysPlace> page(SysPlace param, PageDomain pageDomain){
        PageHelper.startPage(pageDomain.getPage(), pageDomain.getLimit());
        List<SysPlace> sysPlaces = sysPlaceMapper.selectList(param);
        return new PageInfo<>(sysPlaces);
    }

    @Override
    public SysUser arrivalLogin(String username) {
        SysUser sysUser = sysPlaceMapper.arrivalLogin(username);
        return sysUser;
    }

    @Override
    public SysArrivalInfo alreadyHaveTheDate(String date,String placeName) {
        SysArrivalInfo sysArrivalInfo = sysPlaceMapper.alreadyHaveTheDate(date,placeName);
//        boolean result=false;
//        if(sysArrivalInfos.size()!=0){
//            //list的长度不等于0 ，说明已经有了，所以返回true，否则返回false
//            result=true;
//        }
        return sysArrivalInfo;
    }

    @Override
    public int insertArrivalInfo(SysArrivalInfo sysArrivalInfo) {
        int i = sysPlaceMapper.insertArrivalInfo(sysArrivalInfo);
        return 0;
    }

    @Override
    public boolean updateUnder18(SysArrivalInfo sysArrivalInfo) {
        int i = sysPlaceMapper.updateUnder18(sysArrivalInfo);
        if(i==1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean updateUnder18To30(SysArrivalInfo sysArrivalInfo) {
        int i = sysPlaceMapper.updateUnder18To30(sysArrivalInfo);
        if(i==1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean updateUnder31To60(SysArrivalInfo sysArrivalInfo) {
        int i = sysPlaceMapper.updateUnder31To60(sysArrivalInfo);
        if(i==1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean updateAbove61(SysArrivalInfo sysArrivalInfo) {
        int i = sysPlaceMapper.updateAbove61(sysArrivalInfo);
        if(i==1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean updateManNumber(SysArrivalInfo sysArrivalInfo) {
        int i = sysPlaceMapper.updateManNumber(sysArrivalInfo);
        if(i==1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean updateWomanNumber(SysArrivalInfo sysArrivalInfo) {
        int i = sysPlaceMapper.updateWomanNumber(sysArrivalInfo);
        if(i==1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public SysPlace selectById(String placeId) {
        SysPlace sysPlace = sysPlaceMapper.selectById(placeId);
        return sysPlace;
    }

    @Override
    public boolean insert(SysPlace sysPlace) {
        boolean result = sysPlaceMapper.insert(sysPlace);
        return result;
    }

    @Override
    public boolean deleteById(String placeId) {
        boolean result = sysPlaceMapper.deleteById(placeId);
        return result;
    }

    /**
     * Describe: 根据 id 批量删除用户数据
     * Param: ids
     * Return: Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchRemove(String[] ids) {
        boolean result = sysPlaceMapper.deleteByIds(ids);
        return result;
    }

    @Override
    public boolean updateById(SysPlace sysPlace) {
        boolean result = sysPlaceMapper.updateById(sysPlace);
        return result;
    }

}
