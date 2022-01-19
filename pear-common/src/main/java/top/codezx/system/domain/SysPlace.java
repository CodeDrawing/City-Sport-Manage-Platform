package top.codezx.system.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SysPlace {
    Integer placeId;
    String placeName;
    Integer createAdministratorsId;
    String sportProject;
    Date createData;
    String notice;
    Integer managerId;


}
