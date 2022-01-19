package top.codezx.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.codezx.system.domain.SysPlace;

import java.util.List;
@Mapper
public interface SysPlaceMapper {

    List<SysPlace> selectList(SysPlace param);

}
