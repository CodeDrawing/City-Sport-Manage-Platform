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
    String placeId;
    String placeName;
    String createAdministratorsId;
    String sportProject;
    Date createData;
    String notice;
    String managerId;
    String deptId;
    String deptName;
    String userName;
    String QRInfo;


}
