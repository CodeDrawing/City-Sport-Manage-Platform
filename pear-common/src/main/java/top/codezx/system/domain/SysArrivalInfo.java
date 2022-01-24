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
public class SysArrivalInfo {
  String arrivalInfoId;
  Integer theNumberOfMan;
  Integer theNumberOfWoman;
  Integer Under18;
  Integer the18To30;
  Integer the31To60;
  Integer above61;
  Integer theNumberOfPeople;
  Date date;
  String placeName;
  String placeId;
}
